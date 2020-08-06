/*
resource "aws_cloudwatch_log_group" "example" {
  name              = "API-Gateway-Execution-Logs_${aws_api_gateway_rest_api.lambda_api_gateway.id}/${local.lambda_stage}"
  retention_in_days = 1
}
*/
