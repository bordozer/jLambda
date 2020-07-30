variable "service_name" {
  default = "jlambda"
}
variable "service_instance_name" {}
variable "environment_name" {}

variable "region" {
  default = "eu-west-3"
}

/*variable "domain_name" {}
variable "route53_zone_id" {
  default = "ZYQ37WWIE7SAZ"
}

// The certificate should be in 'us-east-1'
variable "certificate_arn" {
  default = "arn:aws:acm:us-east-1:899415655760:certificate/77fe5ab9-3abb-4210-8919-2dea592dc857"
}*/

variable "lambda_payload_filename" {
  default = "../../build/libs/jlambda.jar"
}

variable "lambda_function_handler" {
  default = "com.bordozer.jlambda.LambdaHandler"
}

variable "lambda_runtime" {
  default = "java11"
}

variable "api_path" {
  default = "api"
}

locals {
  common_tags = {
    Name = var.service_instance_name
    ServiceName = var.service_name
    Environment = var.environment_name
  }
  lambda_function_name = "tf-${var.service_instance_name}-function"
  lambda_stage = "${var.environment_name}-stage"
}
