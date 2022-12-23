variable "environment" {
  description = "The application environment"
}

variable "vpc_id" {
  description = "The VPC id"
}

variable "vpc_cidr" {
  description = "The CIDR block for the VPC."
}

variable "availability_zones" {
  description = "List of availability zones"
}

variable "public_subnets" {
  description = "List of public subnets"
}

variable "private_subnets" {
  description = "List of private subnets"
}
variable "app_name" {
  description = "The application name"
}

variable "db_allocated_storage" {
  description = "Database allocated_storage"
}

variable "db_username" {
  description = "Database username"
}

variable "db_password" {
  description = "Database password"
}
variable "db_engine_version" {
  description = "Database engine_version"
}

variable "db_instance_class" {
  description = "Database instance_class"
}
variable "db_storage_type" {
  description = "Database storage_type"
}
variable "db_backup_retention_period" {
  description = "Database backup_retention_period"
}




