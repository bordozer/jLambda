#!/bin/bash

YELLOW='\e[93m'
DEFAULT='\e[39m'

SERVICE_NAME="jlambda"

# env: `test` or `prod`
ENV=$1
if [ -z "$ENV" ]
then
      echo -e "ENV parameter is empty. Provide '${YELLOW}test${DEFAULT}' or '${YELLOW}prod${DEFAULT}'"
      exit 1;
fi

echo -e "Environment '${YELLOW}${ENV}${DEFAULT}' is going to be deployed to AWS"

terraform -version

terraform init \
  -backend-config="key=${SERVICE_NAME}.${ENV}.tfstate"

#terraform plan

terraform apply -var-file="env/${ENV}.tfvars" -auto-approve -var="environment_name=${ENV}"
