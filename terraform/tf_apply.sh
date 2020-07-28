#!/bin/bash

SERVICE_NAME="lambda"

# env: `staging` or `prod`
ENV=$1
if [ -z "$ENV" ]
then
      echo "ENV is empty. Provide 'test' or 'prod'"
      exit 1;
fi

terraform -version

terraform init \
  -backend-config="key=${SERVICE_NAME}.${ENV}.tfstate"

terraform plan

terraform apply -var-file="env/${ENV}.tfvars" -auto-approve
