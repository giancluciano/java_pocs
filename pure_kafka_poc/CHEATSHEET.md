# Kafka Topic Cheatsheet

Quick reference for topic and partition operations against a local broker at `localhost:9092`.
All commands run from `kafka_2.13-4.2.0/`.

> Set once per shell to shorten commands:
> ```bash
> export BS=localhost:9092
> ```

## Topics

### Create a topic

```bash
bin/kafka-topics.sh --create \
  --topic demo-topic \
  --bootstrap-server $BS \
  --partitions 1
```

With custom config (e.g. retention):

```bash
bin/kafka-topics.sh --create \
  --topic demo-topic \
  --bootstrap-server $BS \
  --partitions 1 \
  --replication-factor 1 \
  --config retention.ms=86400000 \
  --config cleanup.policy=delete
```

`--if-not-exists` makes the create idempotent.

### List topics

```bash
# All topics
bin/kafka-topics.sh --list --bootstrap-server $BS

# Exclude internal topics (e.g. __consumer_offsets)
bin/kafka-topics.sh --list --bootstrap-server $BS --exclude-internal
```

### Describe a topic

```bash
bin/kafka-topics.sh --describe --topic demo-topic --bootstrap-server $BS
```

Shows partition count, replication factor, leader/replica/ISR per partition, and overridden configs.

### Delete a topic

```bash
bin/kafka-topics.sh --delete --topic demo-topic --bootstrap-server $BS
```

Requires `delete.topic.enable=true` on the broker (default in modern Kafka). Deletion is async — verify with `--list`.

## Partitions

Partitions belong to a topic. You can only **increase** partition count; decreasing is not supported.

### Create / add partitions

Increase partition count for an existing topic:

```bash
bin/kafka-topics.sh --alter \
  --topic demo-topic \
  --bootstrap-server $BS \
  --partitions 6
```

The new total must be greater than the current count. Adding partitions changes key→partition routing for new messages, which can break consumers that rely on key ordering.

### List / inspect partitions

```bash
# Per-partition leader, replicas, ISR for a single topic
bin/kafka-topics.sh --describe --topic demo-topic --bootstrap-server $BS

# All topics, all partitions
bin/kafka-topics.sh --describe --bootstrap-server $BS

# Only under-replicated partitions
bin/kafka-topics.sh --describe --bootstrap-server $BS --under-replicated-partitions

# Only partitions without an active leader
bin/kafka-topics.sh --describe --bootstrap-server $BS --unavailable-partitions

# Earliest / latest offsets per partition
bin/kafka-get-offsets.sh --bootstrap-server $BS --topic demo-topic --time earliest
bin/kafka-get-offsets.sh --bootstrap-server $BS --topic demo-topic --time latest
```

### "Delete" partition data

Kafka does not support removing a partition from a topic. To clear data within partitions you have a few options:

```bash
# 1. Delete records up to a given offset (per partition) using a JSON spec
cat > /tmp/delete-records.json <<'EOF'
{
  "partitions": [
    { "topic": "demo-topic", "partition": 0, "offset": -1 },
    { "topic": "demo-topic", "partition": 1, "offset": -1 }
  ],
  "version": 1
}
EOF

bin/kafka-delete-records.sh \
  --bootstrap-server $BS \
  --offset-json-file /tmp/delete-records.json
```

`offset: -1` means "delete up to the high-water mark" (truncate the partition).

```bash
# 2. Drop and recreate the topic (removes all partitions)
bin/kafka-topics.sh --delete --topic demo-topic --bootstrap-server $BS
bin/kafka-topics.sh --create --topic demo-topic --bootstrap-server $BS \
  --partitions 3 --replication-factor 1
```

## Topic configs

```bash
# View configs (including dynamic overrides)
bin/kafka-configs.sh --bootstrap-server $BS \
  --entity-type topics --entity-name demo-topic --describe

# Set / update a config
bin/kafka-configs.sh --bootstrap-server $BS \
  --entity-type topics --entity-name demo-topic \
  --alter --add-config retention.ms=3600000

# Remove an override (revert to broker default)
bin/kafka-configs.sh --bootstrap-server $BS \
  --entity-type topics --entity-name demo-topic \
  --alter --delete-config retention.ms
```
