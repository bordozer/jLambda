data "aws_kms_key" "aws_lambda_kms_key" {
  key_id = var.aws_lambda_kms_key_id
}
