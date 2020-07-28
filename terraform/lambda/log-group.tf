resource "aws_cloudwatch_log_group" "log_group" {
  name = "/aws/lambda/java_lambda_function"
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
