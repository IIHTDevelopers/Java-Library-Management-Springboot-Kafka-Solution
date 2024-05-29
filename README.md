### Library Application

- Provides library operation like alloted/cancellation using Apache kafka.

### Library Application Producer Module

- ** Create Library using api endpoint /api/library/ and publish library event message to kafka using topic create-library.

### Library Application Consumer Module

- ** LibraryEventsConsumerManualOffset Listening library event and store details in database.
- Incase of any failure happen in LibraryEventsConsumerManualOffset then New message has been published to
  retry-create-library topic and store failure record in database

### Library Application Retry Module

- ** LibraryEventsRetryConsumer is Responsible to listen failed consumed message.
- A scheduler RetryScheduler is running wit 5 sec internal to fetch all failure record from database and publish again
  on kafka

---
