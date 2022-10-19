variable "aws_region" {
  description = "AWS Region"
  default     = "ap-south-1"
}

variable "aws_profile" {
  description = "AWS profile"
  default     = "default"
}

variable "environment" {
  description = "The application environment"
  default     = "prod"
}

variable "vpc_cidr" {
  description = "The CIDR block for the VPC."
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of availability zones"
  default     = ["ap-south-1a", "ap-south-1b"]
}

variable "public_subnets_cidr" {
  description = "List of public subnets cidr"
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_subnets_cidr" {
  description = "List of private subnets cidr"
  default     = ["10.0.3.0/24", "10.0.4.0/24"]
}

variable "app_name" {
  description = "The application name"
}

variable "container_image" {
  description = "Docker image"
}

variable "container_port" {
  description = "The port where the Docker is exposed"
  default     = 8080
}

variable "container_cpu" {
  description = "The number of cpu units used by the task"
  default     = 128
}

variable "container_memory" {
  description = "The amount (in MiB) of memory used by the task"
  default     = 256
}

variable "desired_task_count" {
  description = "Number of ECS tasks to run"
  default     = 1
}

variable "health_check_path" {
  description = "Http path for task health check"
  default     = "/"
}

variable "db_username" {
  description = "Database username"
}

variable "db_password" {
  description = "Database password"
}

variable "db_allocated_storage" {
  description = "Database allocated_storage"
}

variable "db_engine_version" {
  description = "Database engine_version"
}
variable "db_storage_type" {
  description = "Database storage_type"
}
variable "db_instance_class" {
  description = "Database instance_class"
}
variable "db_backup_retention_period" {
  description = "Database backup_retention_period"
}
