package com.assess.kafka.consumer.consumer;

import com.assess.kafka.consumer.entity.Library;
import com.assess.kafka.consumer.service.KafkaLibraryConsumerService;
import com.assess.kafka.consumer.service.LibraryEventsService;
import com.assess.kafka.producer.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LibraryEventsConsumerManualOffset implements AcknowledgingMessageListener<String, LibraryEvent> {
    @Autowired
    private KafkaLibraryConsumerService libraryConsumerService;
    @Autowired
    private LibraryEventsService libraryEventsService;

    @Override
    @KafkaListener(topics = "${spring.kafka.library.topic.create-library}", groupId = "${spring.kafka.library.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, LibraryEvent> consumerRecord, Acknowledgment acknowledgment) {
        log.info("LibraryEventsConsumerManualOffset Received message from Kafka: " + consumerRecord.value());
            if (consumerRecord.offset() % 2 == 0) {
                throw new RuntimeException("This is really odd.");
            }
            processMessage(consumerRecord.value());
            acknowledgment.acknowledge();
    }

    private void processMessage(LibraryEvent libraryEvent) {
        Library dbLibrary = libraryConsumerService.listenCreateLibrary(libraryEvent.getLibraryDto());
        libraryEventsService.listenCreateLibraryEvent(libraryEvent, dbLibrary);
    }

}