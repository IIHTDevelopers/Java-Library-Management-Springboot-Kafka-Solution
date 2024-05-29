package com.assess.kafka.consumer.service;


import com.assess.kafka.consumer.entity.Library;
import com.assess.kafka.consumer.entity.LibraryEvent;
import com.assess.kafka.consumer.entity.LibraryEventType;
import com.assess.kafka.consumer.jpa.LibraryEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.assess.kafka.consumer.entity.LibraryEventType.*;

@Service
public class LibraryEventsService {

    private final LibraryEventRepository libraryEventRepository;

    @Autowired
    public LibraryEventsService(LibraryEventRepository libraryEventRepository) {
        this.libraryEventRepository = libraryEventRepository;
    }


    public void listenCreateLibraryEvent(com.assess.kafka.producer.domain.LibraryEvent libraryEvent, Library dbLibrary) {
        LibraryEvent event = LibraryEvent.builder()
                .eventType(mapEventType(libraryEvent.getEventType()))
                .eventDetails(libraryEvent.getEventDetails())
                .libraryId(dbLibrary.getBookId())
                .build();
        libraryEventRepository.save(event);
    }

    private LibraryEventType mapEventType(com.assess.kafka.producer.domain.LibraryEventType eventType) {
        return switch (eventType) {
            case BOOK_ALLOTED -> BOOK_ALLOTED;
            case BOOK_RELEASED -> BOOK_RELEASED;
        };
    }
}
