#!/usr/bin/env bash

SERVICE_NAME="jlambda"
# env: `test` or `prod`
ENV=$1
FORCE=$2
if [ -z "$ENV" ]
then
      echo "ENV is empty. Provide 'test' or 'prod'"
      exit 1;
fi

echo "=================================================="
echo "Environment '${ENV}' is going to be destroyed ${FORCE}!"
echo "=================================================="

if [ "force" = "${FORCE}" ]; then
   terraform destroy "-var-file=env/${ENV}.tfvars" -auto-approve
   echo "Environment '${ENV}' has been destroyed. R.I.P."
   exit 0
fi

CONFIRM_STR="Destroy ${SERVICE_NAME}-${ENV}"
read -r -p "Type '${CONFIRM_STR}' to proceed: " confirm
if [ "${confirm}" = "${CONFIRM_STR}" ]; then
   terraform destroy "-var-file=env/${ENV}.tfvars" -auto-approve
   echo "Environment '${ENV}' has been destroyed. R.I.P."
   exit 0
fi

echo ""
echo "Wrong confirmation"
echo ""
