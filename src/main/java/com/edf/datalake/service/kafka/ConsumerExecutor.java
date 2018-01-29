package com.edf.datalake.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumerExecutor {

    private ConcurrentLinkedQueue<KafkaConsumer> consumers;

    public ConsumerExecutor() {
        consumers = new ConcurrentLinkedQueue<>();
    }

    public void addConsumer(KafkaConsumer consumer) {
        consumers.add(consumer);
    }

    public ConsumerRecords<String, String> consume(Long pollTime) {
        Iterator<KafkaConsumer> iterator = consumers.iterator();

        while (iterator.hasNext()) {
            try {
                ConsumerRecords<String, String> records = iterator.next().poll(Long.valueOf(pollTime));
                return records;
            } catch (ConcurrentModificationException e) {
                // Consumer is busy, Try to get the next one !
            }
        }

        // No consumer was returned ! fuck you and your heavy load !
        return null;
    }

    public ConcurrentLinkedQueue<KafkaConsumer> getConsumers() {
        return consumers;
    }
}
