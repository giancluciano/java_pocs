KAFKA_CLUSTER_ID="$(kafka_2.13-4.2.0/bin/kafka-storage.sh random-uuid)"

kafka_2.13-4.2.0/bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c kafka_2.13-4.2.0/config/server.properties

kafka_2.13-4.2.0/bin/kafka-server-start.sh kafka_2.13-4.2.0/config/server.properties
