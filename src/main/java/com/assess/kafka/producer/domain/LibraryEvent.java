package com.assess.kafka.producer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LibraryEvent {
    private String eventId;
    private LibraryEventType eventType;
    private LibraryDto libraryDto;
    private String eventDetails;

    public LibraryEvent() {

    }
}

