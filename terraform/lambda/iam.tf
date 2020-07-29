resource "aws_iam_role" "lambda_iam_role" {
  name = "tf-${var.service_instance_name}-invoke-role"
  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": ["apigateway.amazonaws.com","lambda.amazonaws.com"]
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
POLICY

  tags = local.common_tags
}

resource "aws_iam_role_policy" "logs_iam_policy" {
  name = "tf-${var.service_instance_name}-logs-iam-role-policy"
  role = aws_iam_role.lambda_iam_role.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "lambda_iam_policy" {
  name = "tf-${var.service_instance_name}-lambda-iam-role-policy"
  role = aws_iam_role.lambda_iam_role.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "lambda:InvokeFunction"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "cloudwatch_iam__policy" {
  name = "tf-${var.service_instance_name}-cloudwatch-iam-role-policy"
  role = aws_iam_role.lambda_iam_role.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "cloudwatch:Describe*",
        "cloudwatch:Get*",
        "cloudwatch:List*"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "kms_iam_policy" {
  name = "tf-${var.service_instance_name}-kms-iam-role-policy"
  role = aws_iam_role.lambda_iam_role.id
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
          "kms:Decrypt"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}
