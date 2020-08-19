# AWS lambda + Terraform

## Build artifacts
To create app artifact run from project root
``` 
./build-all.sh
```
AWS lambda function artifact is in folder
```
build/libs/jlambda.jar
```

## Deploy to AWS
Terraform is user to deploy app;ication to AWS.  
There are two possible envs:
 - test
 - prod
 
### Configure Terraform
Got to *terraform/lambda* folder and edit the next files:
 - variables.tf
 - env/test.tfvars
 - env/prod.tfvars
 
### Deploy to AWS
To deploy to AWS run from *terraform/lambda* folder
```
./tf_apply <env_name>
```
## Remove from AWS
To remove all lambda's infrastructure from AWS run from *terraform/lambda* folder
```
./tf_destroy <env_name>
```
or (no confirmation)
```
./tf_destroy <env_name> force
```

## Testing on AWS test env
You probably do not have Bemobi account so testing always is going to return "Bad request"  
Note that *ApiKey* should be HEX-digit (1056E0F39CD97BE9AE45A is just an example)
```
https://test.jlambda.visual-guitar.org/api
```
Example response
```json 
{
    "status": "OK",
    "value": "Lambda invoke result"
}
```
```json 
{
    "message": "Forbidden"
}
```
