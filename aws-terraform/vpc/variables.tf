variable "environment" {
  description = "The application environment"
}

variable "app_name" {
  description = "The application name"
}
variable "vpc_cidr" {
  description = "The CIDR block for the VPC."
}

variable "availability_zones" {
  description = "List of availability zones"
}

variable "public_subnets_cidr" {
  description = "List of public subnets cidr"
}

variable "private_subnets_cidr" {
  description = "List of private subnets cidr"
}
