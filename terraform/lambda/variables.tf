variable "service_name" {
  default = "lambda"
}
variable "service_instance_name" {}
variable "environment_name" {}

variable "region" {
  default = "eu-west-3"
}

variable "lambda_payload_filename" {
  default = "../../build/libs/lambda.jar"
}

variable "lambda_function_handler" {
  default = "com.bordozer.lambda.LambdaHandler"
}

variable "lambda_runtime" {
  default = "java11"
}

variable "api_path" {
  default = "{proxy+}"
}
