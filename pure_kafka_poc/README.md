# Pure Kafka POC

Local Kafka 4.2.0 (KRaft mode) broker plus a Java 25 consumer that reads from `demo-topic`.

## Layout

- `kafka_2.13-4.2.0/` — Kafka distribution (Scala 2.13 / Kafka 4.2.0)
- `consumer_app/consumer-app/` — Maven project with the Java 25 consumer (`com.example.App`)

## Prerequisites

- Java 25 (`java --version`)
- Maven 3.9+ (`mvn --version`)

## 1. Start Kafka (KRaft, single node)

From `kafka_2.13-4.2.0/`:

```bash
cd kafka_2.13-4.2.0

# Format storage (only needed the first time, or after wiping logs)
KAFKA_CLUSTER_ID=$(bin/kafka-storage.sh random-uuid)
bin/kafka-storage.sh format -t "$KAFKA_CLUSTER_ID" -c config/server.properties

# Start the broker (foreground)
bin/kafka-server-start.sh config/server.properties
```

Broker listens on `localhost:9092`, controller quorum on `localhost:9093`.

To stop: `Ctrl+C`, or from another shell `bin/kafka-server-stop.sh`.

## 2. Create the topic

In another shell, from `kafka_2.13-4.2.0/`:

```bash
bin/kafka-topics.sh --create \
  --topic demo-topic \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1

# Verify
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

## 3. Run the Java consumer

From `consumer_app/consumer-app/`:

```bash
cd consumer_app/consumer-app

# Build
mvn clean package

# Run
mvn exec:java
```

The consumer subscribes to `demo-topic` (group `consumer-app-group`) and prints each record's offset, key, value, and partition.

## 4. Produce test messages

In another shell, from `kafka_2.13-4.2.0/`:

```bash
bin/kafka-console-producer.sh \
  --topic demo-topic \
  --bootstrap-server localhost:9092
```

Type messages and press Enter. Each one should appear in the consumer's output.

## Useful commands

```bash
# Describe topic
bin/kafka-topics.sh --describe --topic demo-topic --bootstrap-server localhost:9092

# Check consumer group lag
bin/kafka-consumer-groups.sh --describe --group consumer-app-group --bootstrap-server localhost:9092

# Reset Kafka state (stop broker first, then)
rm -rf /tmp/kraft-combined-logs
```
