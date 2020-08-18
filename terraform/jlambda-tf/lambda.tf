resource "aws_lambda_function" "lambda_function" {
  function_name     = "${local.aws_service_name}-function"
  runtime           = var.lambda_runtime
  filename          = var.lambda_payload_filename
  source_code_hash  = filebase64sha256(var.lambda_payload_filename)

  handler           = var.lambda_function_handler
  timeout           = 60
  memory_size       = 256
  role              = aws_iam_role.lambda_iam_role.arn
  depends_on        = [
    aws_iam_role.lambda_iam_role,
    aws_iam_role_policy.cloudwatch_iam_policy,
    aws_iam_role_policy.kms_iam_policy,
    aws_iam_role_policy.lambda_iam_policy,
    aws_iam_role_policy.logs_iam_policy
  ]
  description       = "${local.service_instance_name}: lambda function"

  tags = local.common_tags
}

resource "aws_lambda_permission" "with_api_gateway" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda_function.function_name
  principal     = "apigateway.amazonaws.com"
  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_rest_api.lambda_api_gateway.execution_arn}/${local.lambda_stage}/GET/${var.api_gateway_path}"
}