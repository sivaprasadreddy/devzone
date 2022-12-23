provider "aws" {
  profile = var.aws_profile
  region  = var.aws_region
}

module "vpc" {
  source               = "./vpc"
  vpc_cidr             = var.vpc_cidr
  environment          = var.environment
  app_name             = var.app_name
  availability_zones   = var.availability_zones
  public_subnets_cidr  = var.public_subnets_cidr
  private_subnets_cidr = var.private_subnets_cidr
}

module "ecr" {
  source    = "./ecr"
  repo_name = "${var.app_name}-${var.environment}"
}

module "ssm" {
  source      = "./ssm"
  app_name    = var.app_name
  environment = var.environment
  db_username = var.db_username
  db_password = var.db_password
}

module "rds" {
  source                     = "./rds"
  vpc_id                     = module.vpc.vpc_id
  vpc_cidr                   = var.vpc_cidr
  environment                = var.environment
  private_subnets            = module.vpc.private_subnets
  public_subnets             = module.vpc.public_subnets
  availability_zones         = var.availability_zones
  app_name                   = var.app_name
  db_username                = var.db_username
  db_password                = var.db_password
  db_allocated_storage       = var.db_allocated_storage
  db_storage_type            = var.db_storage_type
  db_backup_retention_period = var.db_backup_retention_period
  db_engine_version          = var.db_engine_version
  db_instance_class          = var.db_instance_class
}

module "ecs" {
  source               = "./ecs"
  aws_region           = var.aws_region
  environment          = var.environment
  app_name             = var.app_name
  vpc_id               = module.vpc.vpc_id
  public_subnets       = module.vpc.public_subnets
  container_cpu        = var.container_cpu
  container_image      = var.container_image
  container_memory     = var.container_memory
  container_port       = var.container_port
  desired_task_count   = var.desired_task_count
  health_check_path    = var.health_check_path
  db_endpoint          = module.rds.db_endpoint
  db_name              = module.rds.db_name
  db_username_ssm_path = module.ssm.db_username_ssm_path
  db_password_ssm_path = module.ssm.db_password_ssm_path
}