package com.assess.kafka.producer.producer;

import com.assess.kafka.producer.domain.LibraryDto;
import com.assess.kafka.producer.domain.LibraryEvent;
import com.assess.kafka.producer.domain.LibraryEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class LibraryEventProducer {

    private final KafkaTemplate<String, LibraryEvent> kafkaTemplate;

    @Value("${spring.kafka.library.topic.create-library}")
    private String topic;

    @Autowired
    public LibraryEventProducer(KafkaTemplate<String, LibraryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public LibraryEvent sendCreateLibraryEvent(LibraryDto libraryDto, String eventDetails) {
        LibraryEvent libraryEvent = LibraryEvent.
                builder()
                .libraryDto(libraryDto)
                .eventType(LibraryEventType.BOOK_ALLOTED)
                .eventDetails(eventDetails + libraryDto.getLibrarianId())
                .build();
        try {
            CompletableFuture<SendResult<String, LibraryEvent>> sendResultCompletableFuture = kafkaTemplate.send(topic, libraryEvent.getEventType().toString(), libraryEvent);
           return sendResultCompletableFuture.get().getProducerRecord().value();
        } catch (Exception e) {
            log.debug("Error occurred while publishing message due to " + e.getMessage());
        }
        return libraryEvent;
    }
}
