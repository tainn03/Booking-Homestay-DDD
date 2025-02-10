package nnt.com.application.brokerMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ElasticSearchConsomer {
    HomestaySearchDomainService homestaySearchDomainService;

    @KafkaListener(topics = "database.sync", groupId = "my-group", concurrency = "5")
    public void consumer(ConsumerRecord<String, HomestayResponse> record) {
        homestaySearchDomainService.save(record.value());
    }
}
