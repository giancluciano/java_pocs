# Minikube manifests — producer/consumer + host Kafka

Kafka runs on the host (outside the cluster) on `localhost:9092`. Pods reach
it via `host.minikube.internal:9092` (a DNS name minikube auto-injects).

## One-time Kafka config

For pods to actually connect, the broker must **advertise** an address
reachable from inside the cluster. In `kafka_2.13-4.2.0/config/server.properties`
(or whichever properties file you use), set:

```
listeners=PLAINTEXT://0.0.0.0:9092
advertised.listeners=PLAINTEXT://host.minikube.internal:9092
```

Then restart Kafka. From the host, you can still reach Kafka at
`host.minikube.internal:9092` (it resolves to your local IP) if you add it to
`/etc/hosts`, or just keep using `localhost:9092` from the host by configuring
two listeners.

## Build images into minikube

```bash
eval "$(minikube docker-env)"
docker build -t producer-app:latest ./producer-app
docker build -t consumer-app:latest ./consumer_app/consumer-app
```

## Install KEDA (one-time)

```bash
helm repo add kedacore https://kedacore.github.io/charts
helm repo update kedacore
helm upgrade --install keda kedacore/keda \
  --namespace keda --create-namespace --wait
```

Verify: `kubectl get pods -n keda` — should show `keda-operator`,
`keda-operator-metrics-apiserver`, and `keda-admission-webhooks` Running.

## Apply

```bash
kubectl apply -f k8s/kafka-config.yaml
kubectl apply -f k8s/producer-deployment.yaml
kubectl apply -f k8s/consumer-deployment.yaml
kubectl apply -f k8s/consumer-scaledobject.yaml
```

`consumer-scaledobject.yaml` is a KEDA `ScaledObject` that scales the consumer
deployment from 0 to 10 replicas based on Kafka consumer-group lag on
`demo-topic` (lag threshold = 5). It overrides the static `replicas` in
`consumer-deployment.yaml`.

## Verify

```bash
kubectl get pods -l app=producer-app
kubectl get pods -l app=consumer-app
kubectl logs -l app=producer-app --tail=20 -f
kubectl logs -l app=consumer-app --tail=20 -f

# KEDA scaling state
kubectl get scaledobject
kubectl get hpa
kubectl describe scaledobject consumer-app
```

## Tear down

```bash
kubectl delete -f k8s/
```
