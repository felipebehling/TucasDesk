#!/bin/sh

# This script is the entrypoint for the backend Docker container.
# Its purpose is to wait for LocalStack to be fully initialized,
# then load the dynamic AWS resource configuration, and finally
# start the Spring Boot application.

set -e

# Define the shared directory and the files we need to check
SHARED_DIR="/tmp/localstack"
ENV_FILE="${SHARED_DIR}/.env.localstack"
HEALTH_FILE="${SHARED_DIR}/initialized"

echo "Backend entrypoint started."
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
# This will export the dynamic AWS_COGNITO_USER_POOL_ID and
# AWS_COGNITO_APP_CLIENT_ID into this container's environment.
if [ -f "${ENV_FILE}" ]; then
  echo "Sourcing environment variables from ${ENV_FILE}"
  . "${ENV_FILE}"
else
  echo "Error: Environment file ${ENV_FILE} not found."
  exit 1
fi

# Log the loaded variables to confirm they are set
echo "AWS_COGNITO_USER_POOL_ID is set to: ${AWS_COGNITO_USER_POOL_ID}"
echo "AWS_COGNITO_APP_CLIENT_ID is set to: ${AWS_COGNITO_APP_CLIENT_ID}"

# Now that the environment is configured, execute the main container command
# (i.e., start the Spring Boot application).
# The original command is passed as arguments to this script ($@).
echo "Executing the main application command: $@"
exec "$@"
