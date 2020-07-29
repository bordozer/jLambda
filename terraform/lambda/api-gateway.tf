resource "aws_api_gateway_rest_api" "gateway" {
  name        = "tf-${var.service_instance_name}-api-gateway"
  description = "${var.service_instance_name}: lambda API gateway"

  endpoint_configuration {
    types = ["REGIONAL"]
  }

  tags = local.common_tags
}

resource "aws_api_gateway_resource" "path_resource" {
  rest_api_id = aws_api_gateway_rest_api.gateway.id
  parent_id   = aws_api_gateway_rest_api.gateway.root_resource_id
  path_part   = var.api_path
}

resource "aws_api_gateway_method" "path_resource_method" {
  rest_api_id   = aws_api_gateway_rest_api.gateway.id
  resource_id   = aws_api_gateway_resource.path_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "path_resource_integration" {
  rest_api_id             = aws_api_gateway_rest_api.gateway.id
  resource_id             = aws_api_gateway_resource.path_resource.id
  http_method             = aws_api_gateway_method.path_resource_method.http_method

  integration_http_method = "GET"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.lambda_function.invoke_arn
}

resource "aws_api_gateway_deployment" "path_resource_deploy" {
  depends_on  = [aws_api_gateway_integration.path_resource_integration]
  rest_api_id = aws_api_gateway_rest_api.gateway.id
  stage_name  = local.lambda_stage
}

# Unfortunately the proxy resource cannot match an empty path at the root of the API.
# To handle that, a similar configuration must be applied to the root resource that is built in to the REST API object:
resource "aws_api_gateway_method" "lambda_method_root" {
  rest_api_id   = aws_api_gateway_rest_api.gateway.id
  resource_id   = aws_api_gateway_rest_api.gateway.root_resource_id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_integration_root" {
  rest_api_id = aws_api_gateway_rest_api.gateway.id
  resource_id = aws_api_gateway_method.lambda_method_root.resource_id
  http_method = aws_api_gateway_method.lambda_method_root.http_method

  integration_http_method = "GET"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.lambda_function.invoke_arn
}




/*resource "aws_api_gateway_domain_name" "example" {
  certificate_arn = var.certificate_arn
  domain_name     = var.domain_name
}

# Example DNS record using Route53.
# Route53 is not specifically required; any DNS host can be used.
resource "aws_route53_record" "example" {
  name    = aws_api_gateway_domain_name.example.domain_name
  type    = "A"
  zone_id = var.route53_zone_id

  alias {
    evaluate_target_health = true
    name                   = aws_api_gateway_domain_name.example.cloudfront_domain_name
    zone_id                = aws_api_gateway_domain_name.example.cloudfront_zone_id
  }
}*/
