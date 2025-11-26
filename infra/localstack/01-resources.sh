#!/bin/bash

echo "Configuring LocalStack resources..."

# Define a shared directory for communication between containers
SHARED_DIR="/tmp/localstack"
ENV_FILE="${SHARED_DIR}/.env.localstack"
HEALTH_FILE="${SHARED_DIR}/initialized"

# Clean up previous run's files to ensure a fresh start
rm -f "${ENV_FILE}" "${HEALTH_FILE}"
mkdir -p "${SHARED_DIR}"

# Set default AWS region
AWS_REGION="us-east-1"

# --- Cognito ---
# Create the user pool and capture the dynamically generated ID
USER_POOL_NAME="tucasdesk-local"
USER_POOL_ID=$(awslocal cognito-idp create-user-pool \
  --pool-name "${USER_POOL_NAME}" \
  --query 'UserPool.Id' \
  --output text \
  --region "${AWS_REGION}")
echo "Cognito User Pool created with ID: ${USER_POOL_ID}"

# Create the user pool client and capture its dynamic ID
CLIENT_NAME="tucasdesk-client-local"
USER_POOL_CLIENT_ID=$(awslocal cognito-idp create-user-pool-client \
  --user-pool-id "${USER_POOL_ID}" \
  --client-name "${CLIENT_NAME}" \
  --no-generate-secret \
  --query 'UserPoolClient.ClientId' \
  --output text \
  --region "${AWS_REGION}")
echo "Cognito User Pool Client created with ID: ${USER_POOL_CLIENT_ID}"

# Write the dynamic Cognito variables to the shared .env file
# This file will be sourced by the backend containers
echo "Writing dynamic Cognito configuration to ${ENV_FILE}"
{
  echo "AWS_COGNITO_USER_POOL_ID=${USER_POOL_ID}"
  echo "AWS_COGNITO_APP_CLIENT_ID=${USER_POOL_CLIENT_ID}"
  # For client-side access from the browser
  echo "VITE_AWS_COGNITO_USER_POOL_ID=${USER_POOL_ID}"
  echo "VITE_AWS_COGNITO_APP_CLIENT_ID=${USER_POOL_CLIENT_ID}"
} > "${ENV_FILE}"

# --- SQS ---
QUEUE_NAME="tucasdesk-local-queue"
QUEUE_URL=$(awslocal sqs create-queue --queue-name "${QUEUE_NAME}" --query 'QueueUrl' --output text --region "${AWS_REGION}")
QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url "${QUEUE_URL}" --attribute-names QueueArn --query 'Attributes.QueueArn' --output text --region "${AWS_REGION}")
echo "SQS Queue created with URL: ${QUEUE_URL} and ARN: ${QUEUE_ARN}"

# --- SNS ---
TOPIC_TICKET_CREATED_NAME="tucasdesk-local-ticket-created"
TOPIC_TICKET_CREATED_ARN=$(awslocal sns create-topic --name "${TOPIC_TICKET_CREATED_NAME}" --query 'TopicArn' --output text --region "${AWS_REGION}")
echo "SNS Topic for ticket creation created with ARN: ${TOPIC_TICKET_CREATED_ARN}"

TOPIC_TICKET_CLOSED_NAME="tucasdesk-local-ticket-closed"
TOPIC_TICKET_CLOSED_ARN=$(awslocal sns create-topic --name "${TOPIC_TICKET_CLOSED_NAME}" --query 'TopicArn' --output text --region "${AWS_REGION}")
echo "SNS Topic for ticket closure created with ARN: ${TOPIC_TICKET_CLOSED_ARN}"

TOPIC_TICKET_INTERACTED_NAME="tucasdesk-local-ticket-interacted"
TOPIC_TICKET_INTERACTED_ARN=$(awslocal sns create-topic --name "${TOPIC_TICKET_INTERACTED_NAME}" --query 'TopicArn' --output text --region "${AWS_REGION}")
echo "SNS Topic for ticket interaction created with ARN: ${TOPIC_TICKET_INTERACTED_ARN}"

# --- Subscriptions ---
awslocal sns subscribe --topic-arn "${TOPIC_TICKET_CREATED_ARN}" --protocol sqs --notification-endpoint "${QUEUE_ARN}" --region "${AWS_REGION}"
echo "Subscribed SQS queue to ticket creation topic"

awslocal sns subscribe --topic-arn "${TOPIC_TICKET_CLOSED_ARN}" --protocol sqs --notification-endpoint "${QUEUE_ARN}" --region "${AWS_REGION}"
echo "Subscribed SQS queue to ticket closure topic"

awslocal sns subscribe --topic-arn "${TOPIC_TICKET_INTERACTED_ARN}" --protocol sqs --notification-endpoint "${QUEUE_ARN}" --region "${AWS_REGION}"
echo "Subscribed SQS queue to ticket interaction topic"

# --- SES ---
EMAIL_ADDRESS="noreply@tucasdesk.local"
awslocal ses verify-email-identity --email-address "${EMAIL_ADDRESS}" --region "${AWS_REGION}"
echo "SES email identity verified for: ${EMAIL_ADDRESS}"

# --- Signal Completion ---
# Create a "done" flag file to indicate that the initialization is complete.
# The backend entrypoint script will wait for this file.
touch "${HEALTH_FILE}"
echo "Initialization complete. Health flag created at ${HEALTH_FILE}"
