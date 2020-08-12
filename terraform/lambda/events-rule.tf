resource "aws_cloudwatch_event_rule" "lambda_ping" {
  name                = "${local.aws_service_name}-ping-rule"
  description         = "Ping ${aws_lambda_function.lambda_function.function_name} each 5 minutes to keep it warm"
  is_enabled          = var.lambda_keep_warm_enabled
  schedule_expression = "rate(5 minutes)" // cron(0/5 * * * ? *) // each 5 minutes
}

resource "aws_cloudwatch_event_target" "lambda_ping_target" {
  rule          = aws_cloudwatch_event_rule.lambda_ping.name
  target_id     = "${local.aws_service_name}-ping"
  arn           = aws_lambda_function.lambda_function.arn
  input         = "{\"source\":\"lambda-auto-ping-event\"}"
}
