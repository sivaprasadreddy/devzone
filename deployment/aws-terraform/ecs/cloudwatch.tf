
resource "aws_cloudwatch_log_group" "main" {
  name = "/ecs/${local.env_app_name}-task"
  tags = {
    Name        = "${local.env_app_name}-task"
    Environment = var.environment
  }
}
