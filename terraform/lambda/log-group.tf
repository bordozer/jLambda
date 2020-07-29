resource "aws_cloudwatch_log_group" "log_group" {
  name_prefix = "/aws/lambda/tf-${var.service_instance_name}/"
  tags = local.common_tags
}

data "aws_iam_policy_document" "cloudwatch_log_group_access_document" {
  statement {
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]

    resources = [
      "arn:aws:logs:::*",
    ]
  }
}
