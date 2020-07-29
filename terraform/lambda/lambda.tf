resource "aws_lambda_function" "lambda_function" {
  function_name     = local.lambda_function_name
  runtime           = var.lambda_runtime
  filename          = var.lambda_payload_filename
//  source_code_hash  = base64sha256(file(var.lambda_payload_filename))

  handler           = var.lambda_function_handler
  timeout           = 60
  memory_size       = 256
  role              = aws_iam_role.lambda_iam_role.arn
  depends_on        = [aws_iam_role_policy_attachment.aws_iam_role_policy_attachment]
//  kms_key_arn       = data.aws_kms_key.aws_lambda_kms_key.arn
  description       = "${var.service_instance_name}: test lambda function"

  environment {
    variables = {
      foo = "bar"
    }
  }

  tags = local.common_tags
}

resource "aws_lambda_permission" "lambda_permission" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda_function.function_name
  principal     = "apigateway.amazonaws.com"
  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.path_resource_deploy.execution_arn}/*/*"
}
