package com.assess.kafka.consumer.jpa;

import com.assess.kafka.consumer.entity.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Long> {
    // Define custom query methods if needed
}