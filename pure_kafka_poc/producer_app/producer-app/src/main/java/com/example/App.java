package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "demo-topic";
    private static final long SEND_INTERVAL_MS = 1000L;

    public static void main(String[] args) throws InterruptedException {
        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        var running = new AtomicBoolean(true);
        var producer = new KafkaProducer<String, String>(props);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> running.set(false)));

        System.out.printf("Producing to '%s' on %s every %d ms%n", TOPIC, BOOTSTRAP_SERVERS, SEND_INTERVAL_MS);

        try {
            long counter = 0;
            while (running.get()) {
                var key = "key-" + counter;
                var value = "message-" + counter + "-" + Instant.now();
                var record = new ProducerRecord<>(TOPIC, key, value);

                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        System.err.printf("Send failed: %s%n", exception.getMessage());
                    } else {
                        System.out.printf("Sent key=%s value=%s -> partition=%d offset=%d%n",
                                key, value, metadata.partition(), metadata.offset());
                    }
                });

                counter++;
                Thread.sleep(SEND_INTERVAL_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted.");
        } finally {
            producer.flush();
            producer.close();
            System.out.println("Producer closed.");
        }
    }
}
