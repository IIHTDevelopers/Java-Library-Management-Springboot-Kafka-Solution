package com.assess.kafka.consumer.service;


import com.assess.kafka.consumer.entity.Library;
import com.assess.kafka.consumer.jpa.LibraryRepository;
import com.assess.kafka.producer.domain.LibraryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaLibraryConsumerService {
    @Autowired
    private LibraryRepository libraryRepository;

    public Library listenCreateLibrary(LibraryDto libraryDto) {
        Library dbLibrary = Library.builder()
                .publication("ABC")
                .author("Bala guru")
                .title("Java")
                .studentId(1l)
                .build();
        return libraryRepository.save(dbLibrary);
    }

}
