variable "service_name" {
  default = "jlambda"
}
variable "service_instance_name" {}
variable "environment_name" {}

variable "region" {
  default = "eu-west-3"
}

variable "aws_lambda_kms_key_id" {
  default = "4b7362d4-1df7-4ffa-9933-f1e0a06858e6"
}

variable "lambda_payload_filename" {
  default = "../../build/libs/jlambda.jar"
}

variable "lambda_function_handler" {
  default = "com.bordozer.jlambda.LambdaHandler"
}

variable "lambda_runtime" {
  default = "java8"
}

variable "api_path" {
  default = "{proxy+}"
}

locals {
  common_tags = {
    Name = var.service_instance_name
    ServiceName = var.service_name
    Environment = var.environment_name
  }
}
