resource "aws_ssm_parameter" "db_username" {
  name        = "/${var.environment}/${var.app_name}/dbUsername"
  description = "Database Username"
  type        = "SecureString"
  value       = var.db_username
  overwrite = true
}

resource "aws_ssm_parameter" "db_password" {
  name        = "/${var.environment}/${var.app_name}/dbPassword"
  description = "Database password"
  type        = "SecureString"
  value       = var.db_password
  overwrite = true
}
