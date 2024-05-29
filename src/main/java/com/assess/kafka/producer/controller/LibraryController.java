package com.assess.kafka.producer.controller;

import com.assess.kafka.producer.domain.LibraryDto;
import com.assess.kafka.producer.domain.LibraryEvent;
import com.assess.kafka.producer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
    @Autowired
    private LibraryEventProducer libraryEventProducer;

    @PostMapping("/")
    public ResponseEntity<?> createLibrary(@RequestBody LibraryDto requestedLibraryDto) {
        try {
            LibraryEvent libraryEvent = libraryEventProducer.sendCreateLibraryEvent(requestedLibraryDto, "Library Created");
            return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending Library event");
        }
    }
}
