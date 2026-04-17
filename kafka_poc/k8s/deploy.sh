#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "==> Creating namespace..."
kubectl apply -f "$SCRIPT_DIR/namespace.yaml"

echo "==> Deploying Kafka cluster (KRaft mode, 3 brokers)..."
kubectl apply -f "$SCRIPT_DIR/kafka-headless-service.yaml"
kubectl apply -f "$SCRIPT_DIR/kafka-service.yaml"
kubectl apply -f "$SCRIPT_DIR/kafka-statefulset.yaml"

echo "==> Deploying Kafka UI..."
kubectl apply -f "$SCRIPT_DIR/kafka-ui.yaml"

echo ""
echo "==> Waiting for Kafka pods to be ready..."
kubectl -n kafka rollout status statefulset/kafka --timeout=300s

echo ""
echo "Done! Access points:"
echo "  Kafka bootstrap : minikube service kafka-bootstrap -n kafka --url"
echo "  Kafka UI        : minikube service kafka-ui -n kafka --url"
