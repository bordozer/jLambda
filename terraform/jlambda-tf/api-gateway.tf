resource "aws_api_gateway_resource" "lambda_api_gateway_resource" {
  rest_api_id = data.aws_api_gateway_rest_api.gateway.id
  parent_id   = data.aws_api_gateway_rest_api.gateway.root_resource_id
  path_part   = var.api_gateway_path
}

resource "aws_api_gateway_method" "lambda_api_gateway_method" {
  rest_api_id   = data.aws_api_gateway_rest_api.gateway.id
  resource_id   = aws_api_gateway_resource.lambda_api_gateway_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_api_gateway_integration" {
  rest_api_id             = data.aws_api_gateway_rest_api.gateway.id
  resource_id             = aws_api_gateway_resource.lambda_api_gateway_resource.id
  http_method             = aws_api_gateway_method.lambda_api_gateway_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.lambda_function.invoke_arn
}

resource "aws_api_gateway_deployment" "lambda_deploy" {
  depends_on  = [aws_api_gateway_integration.lambda_api_gateway_integration]
  rest_api_id = data.aws_api_gateway_rest_api.gateway.id
  stage_name  = local.lambda_stage
}

resource "aws_api_gateway_domain_name" "lambda_api_gateway_domain" {
  certificate_arn = var.certificate_arn
  domain_name     = var.domain_name
}

resource "aws_api_gateway_base_path_mapping" "api_gateway_stage_mapping" {
  api_id      = data.aws_api_gateway_rest_api.gateway.id
  stage_name  = aws_api_gateway_deployment.lambda_deploy.stage_name
  domain_name = aws_api_gateway_domain_name.lambda_api_gateway_domain.domain_name
}
