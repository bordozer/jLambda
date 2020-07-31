resource "aws_lb_target_group" "lb_tg" {
  name = "tf-${var.service_instance_name}-tg"
  target_type = "lambda"
  // if false then all http parameters are in 'queryStringParameters' (see com.bordozer.jlambda.handler.LambdaHandler)
  // if true - in 'multiValueQueryStringParameters'
  lambda_multi_value_headers_enabled = false

  // Be aware: https://aws.amazon.com/lambda/pricing/
  health_check {
    enabled = var.lambda_health_enabled
    port = 80
    path = var.lambda_health_check_uri
    healthy_threshold = 3
    unhealthy_threshold = 3
    timeout = 30
    interval = 80 // should be greater then aws_lambda_function#timeout
  }

  tags = local.common_tags
}

resource "aws_lb_target_group_attachment" "tg_attach" {
  target_group_arn = aws_lb_target_group.lb_tg.arn
  target_id = aws_lambda_function.lambda_function.arn
  depends_on = [aws_lambda_permission.with_alb]
}
