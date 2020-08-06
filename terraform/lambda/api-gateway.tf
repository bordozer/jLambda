resource "aws_api_gateway_rest_api" "lambda_api_gateway" {
  name        = "tf-${var.service_instance_name}-api-gateway"
  description = "${var.service_instance_name}: lambda API gateway"

  tags = local.common_tags
}

resource "aws_api_gateway_resource" "lambda_api_gateway_resource" {
  rest_api_id = aws_api_gateway_rest_api.lambda_api_gateway.id
  parent_id   = aws_api_gateway_rest_api.lambda_api_gateway.root_resource_id
  path_part   = var.api_gateway_path
}

resource "aws_api_gateway_method" "lambda_api_gateway_method" {
  rest_api_id   = aws_api_gateway_rest_api.lambda_api_gateway.id
  resource_id   = aws_api_gateway_resource.lambda_api_gateway_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_api_gateway_integration" {
  rest_api_id             = aws_api_gateway_rest_api.lambda_api_gateway.id
  resource_id             = aws_api_gateway_resource.lambda_api_gateway_resource.id
  http_method             = aws_api_gateway_method.lambda_api_gateway_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.lambda_function.invoke_arn
}

resource "aws_api_gateway_deployment" "lambda_deploy" {
  depends_on  = [aws_api_gateway_integration.lambda_api_gateway_integration]
  rest_api_id = aws_api_gateway_rest_api.lambda_api_gateway.id
  stage_name  = local.lambda_stage
}

resource "aws_api_gateway_domain_name" "lambda_api_gateway_domain" {
  certificate_arn = var.certificate_arn
  domain_name     = var.domain_name
}

# Example DNS record using Route53.
# Route53 is not specifically required; any DNS host can be used.
resource "aws_route53_record" "lambda_api_gateway_route53_record" {
  name    = aws_api_gateway_domain_name.lambda_api_gateway_domain.domain_name
  type    = "A"
  zone_id = var.route53_zone_id

  alias {
    evaluate_target_health = false
    name                   = aws_api_gateway_domain_name.lambda_api_gateway_domain.cloudfront_domain_name
    zone_id                = aws_api_gateway_domain_name.lambda_api_gateway_domain.cloudfront_zone_id
  }
}

resource "aws_api_gateway_base_path_mapping" "api_gateway_stage_mapping" {
  api_id      = aws_api_gateway_rest_api.lambda_api_gateway.id
  stage_name  = aws_api_gateway_deployment.lambda_deploy.stage_name
  domain_name = aws_api_gateway_domain_name.lambda_api_gateway_domain.domain_name
//  base_path   = var.api_gateway_path
}

resource "aws_api_gateway_authorizer" "lambda" {
  name                              = "${local.lambda_function_name}-api-gateway-authorizer"
  type                              = "COGNITO_USER_POOLS"
  rest_api_id                       = aws_api_gateway_rest_api.lambda_api_gateway.id
  authorizer_uri                    = aws_lambda_function.lambda_function.invoke_arn
  authorizer_credentials            = aws_iam_role.lambda_iam_role.arn
  authorizer_result_ttl_in_seconds  = 120
  identity_source                   = "method.request.header.Authorization"
  provider_arns                     = ["arn:aws:cognito-idp:${var.cognito_region}:${var.aws_account_id}:userpool/${var.cognito_user_pool_id}"]
}
