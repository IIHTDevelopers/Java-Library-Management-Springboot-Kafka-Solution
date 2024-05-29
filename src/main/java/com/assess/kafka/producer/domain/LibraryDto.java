package com.assess.kafka.producer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LibraryDto {
    private Long bookId;
    private String title;
    private String author;
    private String publication;
    private Long studentId;
    private Long librarianId;

    public LibraryDto() {
    }
}
