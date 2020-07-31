data "aws_s3_bucket" "alb_logs_s3_bucket" {
  bucket = var.alb_logs_s3_bucket
}
