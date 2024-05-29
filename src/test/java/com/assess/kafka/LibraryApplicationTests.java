package com.assess.kafka;

import com.assess.kafka.consumer.consumer.LibraryEventsConsumerManualOffset;
import com.assess.kafka.producer.controller.LibraryController;
import com.assess.kafka.producer.domain.LibraryDto;
import com.assess.kafka.producer.domain.LibraryEvent;
import com.assess.kafka.producer.domain.LibraryEventType;
import com.assess.kafka.producer.producer.LibraryEventProducer;
import com.assess.kafka.testutils.MasterData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static com.assess.kafka.testutils.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


@WebMvcTest(LibraryController.class)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@EnableKafka
public class LibraryApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private KafkaTemplate<String, LibraryEvent> kafkaTemplate;


    @MockBean
    private LibraryEventProducer libraryEventProducer;

    @MockBean
    private LibraryEventsConsumerManualOffset libraryEventsConsumerManualOffset;

    @Test
    public void test_BookControllerSendBook() throws Exception {
        final int[] count = new int[1];
        LibraryDto libraryDto = LibraryDto.builder()
                .publication("Bala")
                .bookId(1L)
                .author("CREATED")
                .title("Java")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.
                builder()
                .eventDetails("Create Library")
                .eventType(LibraryEventType.BOOK_ALLOTED)
                .libraryDto(libraryDto)
                .build();

        when(libraryEventProducer.sendCreateLibraryEvent(libraryDto, "Library Created")).then(new Answer<LibraryEvent>() {

            @Override
            public LibraryEvent answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub
                count[0]++;
                return libraryEvent;
            }
        });

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/library/")
                .content(MasterData.asJsonString(libraryDto)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        yakshaAssert(currentTest(), count[0] == 1, businessTestFile);

    }

    @Test
    public void testSendBook() throws Exception {
        LibraryDto libraryDto = LibraryDto.builder()
                .publication("Bala")
                .bookId(1L)
                .author("CREATED")
                .title("Java")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.
                builder()
                .eventDetails("Create Library")
                .eventType(LibraryEventType.BOOK_ALLOTED)
                .libraryDto(libraryDto)
                .build();
        try {
            CompletableFuture<SendResult<String, LibraryEvent>> mockFuture = mock(CompletableFuture.class);
            when(kafkaTemplate.send("create-library", libraryEvent.getEventType().toString(), libraryEvent)).thenReturn(mockFuture);
            this.libraryEventProducer.sendCreateLibraryEvent(libraryDto, "Library Created");
            yakshaAssert(currentTest(), true, businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }

    }

    @Test
    @Disabled
    public void testConsumeBook() {
        LibraryDto libraryDto = LibraryDto.builder()
                .publication("Bala")
                .bookId(1L)
                .author("CREATED")
                .title("Java")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.
                builder()
                .eventDetails("Create Library")
                .eventType(LibraryEventType.BOOK_ALLOTED)
                .libraryDto(libraryDto)
                .build();

        kafkaTemplate.send("create-library", libraryEvent.getEventType().toString(), libraryEvent);


        await().atMost(5, SECONDS).untilAsserted(() -> {
            ConsumerRecord<String, LibraryEvent> mockRecord = mock(ConsumerRecord.class);

            Acknowledgment mockAcknowledgment = mock(Acknowledgment.class);
            libraryEventsConsumerManualOffset.onMessage(mockRecord, mockAcknowledgment);
            yakshaAssert(currentTest(), true, businessTestFile);

        });
    }


}
