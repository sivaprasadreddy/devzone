output "db_endpoint" {
  value = aws_db_instance.postgresdb.endpoint
}

output "db_address" {
  value = aws_db_instance.postgresdb.address
}

output "db_port" {
  value = aws_db_instance.postgresdb.port
}

output "db_name" {
  value = aws_db_instance.postgresdb.db_name
}

output "db_username" {
  value = aws_db_instance.postgresdb.username
}
