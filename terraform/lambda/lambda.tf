resource "aws_lambda_function" "java_lambda_function" {
  runtime           = var.lambda_runtime
  filename          = var.lambda_payload_filename
//  source_code_hash  = base64sha256(file(var.lambda_payload_filename))
  function_name     = "java_lambda_function"

  handler           = var.lambda_function_handler
  timeout           = 60
  memory_size       = 256
  role              = aws_iam_role.lambda_iam_role.arn
  depends_on        = ["aws_cloudwatch_log_group.log_group"]

  environment {
    variables = {
      foo = "bar"
    }
  }
}

resource "aws_lambda_permission" "java_lambda_function" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.java_lambda_function.function_name
  principal     = "apigateway.amazonaws.com"
  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.java_lambda_deploy.execution_arn}/*/*"
}
