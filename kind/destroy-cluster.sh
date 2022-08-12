#!/bin/sh

echo "🏴‍☠️ Destroying Kubernetes cluster..."

kind delete cluster --name simple-k8s
