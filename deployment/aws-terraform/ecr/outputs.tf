output "ecr_repo_name" {
  value = aws_ecr_repository.main.name
}

output "ecr_repo_url" {
  value = aws_ecr_repository.main.repository_url
}