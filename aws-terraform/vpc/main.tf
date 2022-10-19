locals {
  env_app_name = "${var.app_name}-${var.environment}"
}

# Internet VPC
resource "aws_vpc" "main" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = {
    Name = local.env_app_name
  }
}

# Internet GW
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = local.env_app_name
  }
}

# Subnets
resource "aws_subnet" "private" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = element(var.private_subnets_cidr, count.index)
  availability_zone = element(var.availability_zones, count.index)
  count             = length(var.private_subnets_cidr)
  tags = {
    Name = "${local.env_app_name}-private-${count.index + 1 }"
  }
}

resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = element(var.public_subnets_cidr, count.index)
  availability_zone       = element(var.availability_zones, count.index)
  count                   = length(var.public_subnets_cidr)
  map_public_ip_on_launch = true
  tags = {
    Name = "${local.env_app_name}-public-${count.index + 1 }"
  }
}

# route tables
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
  tags = {
    Name = "${local.env_app_name}-public"
  }
}

# route associations public
resource "aws_route_table_association" "public" {
  count          = length(var.public_subnets_cidr)
  subnet_id      = element(aws_subnet.public.*.id, count.index)
  route_table_id = aws_route_table.public.id
}
