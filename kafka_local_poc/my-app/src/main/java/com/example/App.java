package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-streams-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092");
        props.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        source.to("streams-pipe-output");
        final Topology topology = builder.build();

        System.out.println( topology.describe() );
    }
}
