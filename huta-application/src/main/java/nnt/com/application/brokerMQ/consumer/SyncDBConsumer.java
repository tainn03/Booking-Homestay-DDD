package nnt.com.application.brokerMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.document.LocationDocument;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import nnt.com.domain.aggregates.service.LocationSearchDomainService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SyncDBConsumer {
    HomestaySearchDomainService homestaySearchDomainService;
    LocationSearchDomainService locationSearchDomainService;

    @KafkaListener(topics = "database.sync", groupId = "my-group", concurrency = "5")
    public void consumerHomestays(ConsumerRecord<String, HomestayResponse> record) {
        homestaySearchDomainService.save(record.value());
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
    }

    @KafkaListener(topics = "database.sync", groupId = "my-group", concurrency = "5")
    public void consumerLocation(ConsumerRecord<String, LocationDocument> record) {
        locationSearchDomainService.save(record.value());
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
    }
}
