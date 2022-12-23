output "db_username_ssm_path" {
  value = aws_ssm_parameter.db_username.name
}

output "db_password_ssm_path" {
  value = aws_ssm_parameter.db_password.name
}
