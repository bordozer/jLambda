variable "service_name" {
  default = "jlambda"
}
variable "service_instance_name" {}
variable "environment_name" {}
variable "route53_record" {}

variable "region" {
  default = "eu-west-3"
}

variable "vpc" {
  default = "vpc-74c2c81d"
}

variable "subnets" {
  default = [
    "subnet-08d6e761",
    "subnet-f2d79f89",
    "subnet-096bf644"
  ]
}
variable "certificate_arn" {
  default = "arn:aws:acm:eu-west-3:899415655760:certificate/443e4bee-2470-4b79-aed0-895aaedbb2ed"
}

variable "route53_zone_id" {
  default = "ZYQ37WWIE7SAZ"
}

/*
// The certificate should be in 'us-east-1'
variable "certificate_arn" {
  default = "arn:aws:acm:us-east-1:899415655760:certificate/77fe5ab9-3abb-4210-8919-2dea592dc857"
}*/

variable "lambda_payload_filename" {
  default = "../../build/libs/jlambda.jar"
}

variable "lambda_function_handler" {
  default = "com.bordozer.jlambda.handler.LambdaHandler"
}

variable "lambda_runtime" {
  default = "java11"
}

variable "api_gateway_path" {
  default = "api"
}

variable "lambda_health_enabled" {
  default = false
}
variable "lambda_health_check_uri" {
  default = "/?health-check=yes"
}
variable "alb_logs_enabled" {
  default = true
}
variable "alb_logs_s3_bucket" {
  default = "bordozer-alb-logs"
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
