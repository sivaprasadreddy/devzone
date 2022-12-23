variable "aws_region" {
  description = "AWS Region"
}

variable "environment" {
  description = "The application environment"
}
variable "vpc_id" {
  description = "The vpc id"
}

variable "public_subnets" {
  description = "List of public subnets"
}

variable "app_name" {
  description = "The application name"
}

variable "container_image" {
  description = "Docker image"
}

variable "container_port" {
  description = "The port where the Docker is exposed"
}

variable "container_cpu" {
  description = "The number of cpu units used by the task"
}

variable "container_memory" {
  description = "The amount (in MiB) of memory used by the task"
}

variable "desired_task_count" {
  description = "Number of ECS tasks to run"
}

variable "health_check_path" {
  description = "Http path for task health check"
}

variable "db_endpoint" {
  description = "Database endpoint"
}

variable "db_name" {
  description = "Database name"
}

variable "db_username_ssm_path" {
  description = "Database username SSM parameter path"
}

variable "db_password_ssm_path" {
  description = "Database password  SSM parameter path"
}
