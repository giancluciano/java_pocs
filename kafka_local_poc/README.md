# Local kafka

https://kafka.apache.org/42/getting-started/quickstart/

1. download kafka latest version and extract to this folder

start with:
´´´
. start.sh
´´´

run java with:
´´´
cd my-app
mvn -q compile exec:java -Dexec.mainClass=com.example.App
´´´

## ZooKeeper vs KRaft

Kafka has historically needed a separate **ZooKeeper** ensemble to manage cluster metadata. **KRaft** (Kafka Raft) replaces ZooKeeper with a metadata quorum *built into Kafka itself*. As of Kafka 4.0, ZooKeeper support was removed entirely — KRaft is the only mode.

### What metadata is being managed?

Both systems do the same job: track the cluster's "source of truth":
- Which brokers are alive
- Topic and partition definitions
- Partition leadership and ISR (in-sync replicas)
- Configs, ACLs, client quotas
- Controller election

### ZooKeeper mode (legacy)

- Two separate distributed systems: a **ZooKeeper ensemble** (3 or 5 nodes) and the **Kafka brokers**.
- One broker is elected **controller** and reads/writes metadata to ZooKeeper.
- Operators run, monitor, secure, and upgrade two clusters with different protocols, configs, and tools.
- On controller failover, the new controller has to **reload the full metadata** from ZooKeeper — slow on large clusters (tens of seconds to minutes).
- Metadata changes propagate to brokers via RPC, so brokers can drift out of sync with the controller.

### KRaft mode (current)

- A subset of nodes act as **controllers** running an internal **Raft consensus protocol**. They form their own quorum — no external system.
- Cluster metadata lives in an internal Kafka topic, **`__cluster_metadata`**, which is itself a Raft-replicated log.
- Brokers **tail that log** like any other consumer, so they all see the same ordered stream of metadata events. Replaying the log = catching up.
- Nodes can be `process.roles=controller`, `broker`, or `broker,controller` (combined — what `server.properties` does for local dev).
- Failover is fast: the new active controller already has the log in memory; no reload from an external store.

### Why the change?

| Concern | ZooKeeper | KRaft |
|---|---|---|
| Operational surface | Two systems to deploy/secure/upgrade | One |
| Metadata scale | Limited (~200K partitions practical) | Millions of partitions |
| Controller failover | Seconds to minutes | Sub-second to a few seconds |
| Consistency model | Snapshot reload + RPC fan-out | Single replicated log, event-sourced |
| Config | Two sets (`zookeeper.connect`, etc.) | One (`controller.quorum.*`, `process.roles`, `node.id`) |
| Storage bootstrap | Implicit | Explicit `kafka-storage.sh format` with a cluster UUID |

### How it shows up in this project

Look at `kafka_2.13-4.2.0/config/server.properties`:

- `process.roles=broker,controller` — this single node plays both roles (only sensible for dev).
- `node.id=1` — KRaft uses `node.id`, not the old `broker.id`.
- `controller.quorum.bootstrap.servers=localhost:9093` — peers in the Raft quorum.
- `controller.listener.names=CONTROLLER` — required in KRaft.
- No `zookeeper.connect=...` — that property doesn't exist anymore.

And `start.sh` does the KRaft-specific bootstrap:
1. `kafka-storage.sh random-uuid` — generates a cluster ID.
2. `kafka-storage.sh format --standalone -t $UUID -c server.properties` — writes the initial metadata log + a `meta.properties` marker into `log.dirs` so the node knows it belongs to that cluster. ZooKeeper-mode clusters didn't need this step.

In short: **KRaft folds the "who's in charge and what does the cluster look like" problem back into Kafka itself**, making it operationally simpler, faster to recover, and able to scale to far more partitions.
