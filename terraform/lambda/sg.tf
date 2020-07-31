resource "aws_security_group" "alb_sg" {
  name = "tf-${var.service_instance_name}-alb-sg"
  description = "${var.service_instance_name} ALB SG"

  vpc_id = var.vpc

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    description = "TLS from VPC"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  # Access from LB to everywhere
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  tags = local.common_tags
}
