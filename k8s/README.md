# Running application on Kubernetes

## Install minikube
https://minikube.sigs.k8s.io/docs/start/

`brew install minikube` or

```shell
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube
```

* Start minikube

```shell
minikube start  //use docker driver
minikube start --driver=virtualbox
eval $(minikube docker-env)
```

* Build docker image: `./gradlew bootBuildImage`

* Deploy application

    * `kubectl apply -f k8s/config.yaml`
    * `kubectl apply -f k8s/postgresdb.yaml`
    * `kubectl apply -f k8s/devzone.yaml`
    * `kubectl port-forward service/devzone-svc 9090:8080`
    * `kubectl scale --replicas=2 deployment devzone-deployment`

### Some useful commands

```shell

kubectl create deployment devzone --image sivaprasadreddy/devzone:latest -o yaml --dry-run=client > k8s/deployment.yaml

kubectl create service clusterip devzone --tcp 80:8080 -o yaml --dry-run=client > k8s/service.yaml

kubectl port-forward service/devzone-svc 9090:8080

kubectl create configmap devzone --from-file=./k8s/application.properties

kubectl create configmap devzone-configmap --from-file=./application.properties

kubectl get configmap devzone -o yaml

kubectl delete service/devzone-svc deployment.apps/devzone-deployment

kubectl delete service/devzone-postgres-svc deployment.apps/devzone-postgres-deployment
```
