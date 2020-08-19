data "aws_api_gateway_rest_api" "gateway" {
  name = local.api_gateway_name
}
