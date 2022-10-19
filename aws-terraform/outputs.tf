
output "vpc_id" {
  value = module.vpc.vpc_id
}

output "public_subnets" {
  value = module.vpc.public_subnets
}

output "private_subnets" {
  value = module.vpc.private_subnets
}

output "ecr_repo_name" {
  value = module.ecr.ecr_repo_name
}

output "ecr_repo_url" {
  value = module.ecr.ecr_repo_url
}

output "db_endpoint" {
  value = module.rds.db_endpoint
}

output "db_address" {
  value = module.rds.db_address
}

output "db_port" {
  value = module.rds.db_port
}

output "db_name" {
  value = module.rds.db_name
}

output "db_username_ssm_path" {
  value = module.ssm.db_username_ssm_path
}

output "db_password_ssm_path" {
  value = module.ssm.db_password_ssm_path
}