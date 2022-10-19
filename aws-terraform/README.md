# DevZone Deployment on AWS using Terraform

This terraform module contains code for the deployment of devzone app on ECS cluster.

```shell
$ brew install terraform
$ terraform init
$ terraform plan
$ terraform apply
$ terraform destroy
```

As there is no ALB provisioned you need to get the Public IP from ECS Task details and access http://IP_ADDR:8080/

