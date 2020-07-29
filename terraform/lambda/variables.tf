variable "service_name" {
  default = "jlambda"
}
variable "service_instance_name" {}
variable "environment_name" {}

variable "region" {
  default = "eu-west-3"
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
