# Deploying on Kubernetes using Helm

* Install helm https://helm.sh/
* Deploy helm chart

```shell
devzone/k8s-heml> helm install devzone-v1 ./devzone
devzone/k8s-heml> kubectl get pods 
devzone/k8s-heml> kubectl logs devzone-v1-b78b76887-snfbm
devzone/k8s-heml> helm uninstall devzone-v1
```

