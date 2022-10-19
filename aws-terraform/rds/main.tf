locals {
  env_app_name = "${var.app_name}-${var.environment}"
}

resource "aws_db_subnet_group" "postgresdb-subnet" {
  name        = "${local.env_app_name}-postgresdb-subnet"
  description = "RDS subnet group"
  subnet_ids  = var.private_subnets
}

resource "aws_db_parameter_group" "postgresdb-parameters" {
  name        = "${local.env_app_name}-postgresdb-parameters"
  family      = "postgres14"
  description = "PostgreSQL parameter group"
}

resource "aws_db_instance" "postgresdb" {
  allocated_storage       = var.db_allocated_storage
  engine                  = "postgres"
  engine_version          = var.db_engine_version
  instance_class          = var.db_instance_class
  identifier              = "${local.env_app_name}-postgresdb"
  db_name                 = "postgres"
  username                = var.db_username
  password                = var.db_password
  db_subnet_group_name    = aws_db_subnet_group.postgresdb-subnet.name
  parameter_group_name    = aws_db_parameter_group.postgresdb-parameters.name
  multi_az                = false
  vpc_security_group_ids  = [aws_security_group.allow-postgresdb.id]
  storage_type            = var.db_storage_type
  backup_retention_period = var.db_backup_retention_period
  availability_zone       = element(var.availability_zones, 0)
  skip_final_snapshot     = true
  tags = {
    Name = "${local.env_app_name}-postgresdb-instance"
  }
}

resource "aws_security_group" "allow-postgresdb" {
  vpc_id      = var.vpc_id
  name        = "${local.env_app_name}-allow-postgresdb"
  description = "allow-postgresdb"
  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    self        = true
  }
  tags = {
    Name = "${local.env_app_name}-allow-postgresdb"
  }
}
