data "aws_caller_identity" "current" {}

locals {
  env_app_name = "${var.app_name}-${var.environment}"
  ssm_db_username = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter${var.db_username_ssm_path}"
  ssm_db_password = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter${var.db_password_ssm_path}"
}

resource "aws_ecs_task_definition" "main" {
  family                   = "${local.env_app_name}-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn
  container_definitions = jsonencode([{
    name        = "${local.env_app_name}-container"
    image       = var.container_image
    essential   = true
    environment = [
      { name = "SPRING_DATASOURCE_DRIVER_CLASS_NAME", value = "org.postgresql.Driver" },
      { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${var.db_endpoint}/${var.db_name}" }
    ]
    secrets = [
      {
        "name": "SPRING_DATASOURCE_USERNAME",
        "valueFrom": local.ssm_db_username
      },
      {
        "name": "SPRING_DATASOURCE_PASSWORD",
        "valueFrom": local.ssm_db_password
      }
    ]
    portMappings = [{
      protocol      = "tcp"
      containerPort = var.container_port
      hostPort      = var.container_port
    }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = aws_cloudwatch_log_group.main.name
        awslogs-stream-prefix = "ecs"
        awslogs-region        = var.aws_region
      }
    }
  }])

  tags = {
    Name        = "${local.env_app_name}-task-def"
    Environment = var.environment
  }
}

resource "aws_ecs_cluster" "main" {
  name = "${local.env_app_name}-cluster"
  tags = {
    Name        = "${local.env_app_name}-cluster"
    Environment = var.environment
  }
}

resource "aws_ecs_service" "main" {
  name                               = "${local.env_app_name}-service"
  cluster                            = aws_ecs_cluster.main.id
  //task_definition                    = "${aws_ecs_task_definition.main.family}:${max("${aws_ecs_task_definition.main.revision}", "${aws_ecs_task_definition.main.revision}")}"
  task_definition                    = aws_ecs_task_definition.main.arn
  desired_count                      = var.desired_task_count
  launch_type                        = "FARGATE"
  scheduling_strategy                = "REPLICA"

  network_configuration {
    security_groups  = [aws_security_group.ecs_tasks.id]
    subnets          = var.public_subnets
    assign_public_ip = true
  }
}

output "ecs_cluster_id" {
  value = aws_ecs_cluster.main.id
}

output "ecs_task_id" {
  value = aws_ecs_task_definition.main.id
}
