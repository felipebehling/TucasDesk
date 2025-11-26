#!/bin/sh

# This script is the entrypoint for the frontend Docker container.
# Its purpose is to wait for LocalStack to be fully initialized,
# then load the dynamic AWS resource configuration, and finally
# start the Nginx server to serve the React application.

set -e

# Define the shared directory and the files we need to check
SHARED_DIR="/tmp/localstack"
ENV_FILE="${SHARED_DIR}/.env.localstack"
HEALTH_FILE="${SHARED_DIR}/initialized"

echo "Frontend entrypoint started."
echo "Waiting for LocalStack initialization to complete..."

# Wait until the 'initialized' flag file appears in the shared volume.
# This indicates that the LocalStack resource creation script has finished.
# We use a timeout to avoid waiting indefinitely.
TIMEOUT=120 # 2 minutes
WAIT_INTERVAL=5 # 5 seconds
ELAPSED=0

while [ ! -f "${HEALTH_FILE}" ]; do
  if [ "${ELAPSED}" -ge "${TIMEOUT}" ]; then
    echo "Timeout reached. LocalStack did not initialize in time."
    exit 1
  fi
  sleep "${WAIT_INTERVAL}"
  ELAPSED=$((ELAPSED + WAIT_INTERVAL))
  echo "Still waiting for LocalStack... (${ELAPSED}s / ${TIMEOUT}s)"
done

echo "LocalStack is initialized. Sourcing dynamic environment variables."

# Source the .env file created by the LocalStack init script.
# This will export the dynamic Cognito variables into this container's environment.
if [ -f "${ENV_FILE}" ]; then
  echo "Sourcing environment variables from ${ENV_FILE}"
  . "${ENV_FILE}"
else
  echo "Error: Environment file ${ENV_FILE} not found."
  exit 1
fi

# Log the loaded variables to confirm they are set
echo "VITE_AWS_COGNITO_USER_POOL_ID is set to: ${VITE_AWS_COGNITO_USER_POOL_ID}"
echo "VITE_AWS_COGNITO_APP_CLIENT_ID is set to: ${VITE_AWS_COGNITO_APP_CLIENT_ID}"

# The frontend assets are static, so we can't inject environment variables at runtime
# in the same way as the backend. Instead, we'll replace placeholders in the built JS files.
# This is a common pattern for injecting runtime configuration into static frontend builds.

echo "Injecting runtime environment variables into static assets..."

# Find all JavaScript files in the assets directory
for file in /usr/share/nginx/html/assets/*.js; do
  echo "Processing $file..."
  # Replace placeholders with actual values. The placeholders are defined in the
  # frontend source code (e.g., `import.meta.env.VITE_AWS_COGNITO_USER_POOL_ID`).
  # The `sed` command performs the in-place replacement.
  sed -i "s|VITE_AWS_COGNITO_USER_POOL_ID_PLACEHOLDER|${VITE_AWS_COGNITO_USER_POOL_ID}|g" "$file"
  sed -i "s|VITE_AWS_COGNITO_APP_CLIENT_ID_PLACEHOLDER|${VITE_AWS_COGNITO_APP_CLIENT_ID}|g" "$file"
done

echo "Environment variables injected successfully."

# Now that the configuration is injected, execute the main container command
# (i.e., start the Nginx server).
# The original command is passed as arguments to this script ($@).
echo "Executing the main application command: $@"
exec "$@"
