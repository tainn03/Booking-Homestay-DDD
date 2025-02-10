package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import nnt.com.domain.shared.model.vo.KafkaTopic;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SyncDBScheduler {
    HomestayDomainService HomestayDomainService;
    HomestaySearchDomainService homestaySearchDomainService;
    KafkaProducer kafkaProducer;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void syncData() {
        List<HomestayDocument> homestayDocuments = homestaySearchDomainService.findAll();
        List<HomestayResponse> homestays = HomestayDomainService.getAllHomestay();
        int DBrows = homestays.size();
        int ESrows = homestayDocuments.size();
        if (DBrows != ESrows) {
            log.info("SYNC DATA FROM DB TO ELASTICSEARCH FROM {} DB ROWS TO {} ES ROWS", DBrows, ESrows);
            syncDataToElasticSearch(homestays, homestayDocuments);
        }
    }

    private void syncDataToElasticSearch(List<HomestayResponse> homestays, List<HomestayDocument> homestayDocuments) {
        homestays.forEach(homestay -> {
            if (homestayDocuments.stream().noneMatch(homestayDocument -> homestayDocument.getId() == homestay.getId())) {
                kafkaProducer.sendFireAndForgot(KafkaTopic.SYNC_TOPIC.getTopic(), String.valueOf(homestay.getId()), homestay);
            }
        });
    }
}
