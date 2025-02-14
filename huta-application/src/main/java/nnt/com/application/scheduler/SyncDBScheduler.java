package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.document.LocationDocument;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.service.GHNDomainService;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import nnt.com.domain.aggregates.service.LocationSearchDomainService;
import nnt.com.domain.shared.model.dto.District;
import nnt.com.domain.shared.model.dto.Province;
import nnt.com.domain.shared.model.dto.Ward;
import nnt.com.domain.shared.model.vo.KafkaTopic;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SyncDBScheduler {
    HomestayDomainService HomestayDomainService;
    HomestaySearchDomainService homestaySearchDomainService;
    KafkaProducer kafkaProducer;

    GHNDomainService ghnDomainService;
    LocationSearchDomainService locationSearchDomainService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void syncHomestayData() {
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

    @Scheduled(cron = "0 0 0 1 * ?") // Run at 00:00:00 on the first day of every month
    public void syncLocationData() {
        try {
            List<LocationDocument> locations = locationSearchDomainService.getAll();
            syncDataToElasticSearch(locations);
        } catch (UncategorizedElasticsearchException e) {
            log.error("ELASTICSEARCH ERROR DUE TO: " + e.getMessage());
            // Handle the exception as needed
        }
    }

    private void syncDataToElasticSearch(List<LocationDocument> locations) {
        if (locations.size() >= 10000) { // all = 11596
            log.info("SKIP SYNC LOCATION DATA: " + locations.size() + " LOCATIONS");
            return;
        }
        log.info("START SYNC LOCATION DATA");
        AtomicInteger count = new AtomicInteger();
        List<Province> provinces = ghnDomainService.getProvinces();
        provinces.forEach(province -> {
            log.info("PROVINCE {}: {}", province.getProvinceID(), province.getProvinceName());
            List<District> districts = ghnDomainService.getDistricts(province.getProvinceID());
            districts.forEach(district -> {
                log.info("DISTRICT {}: {}", district.getDistrictID(), district.getDistrictName());
                List<Ward> wards = ghnDomainService.getWards(district.getDistrictID());
                wards.forEach(ward -> {
                    log.info("WARD {}: {}", ward.getWardCode(), ward.getWardName());
                    if (locations.stream().noneMatch(location -> location.getId().equals(ward.getWardCode()))) {
                        saveLocation(province, district, ward);
                        count.getAndIncrement();
                    }
                });
            });
        });
        log.info("SYNC LOCATION DATA SUCCESSFULLY: " + count.get() + " LOCATIONS");
    }

    private void saveLocation(Province province, District district, Ward ward) {
        LocationDocument locationDocument = LocationDocument.builder()
                .id(ward.getWardCode())
                .ward(ward.getWardName())
                .district(district.getDistrictName())
                .city(province.getProvinceName())
                .build();
        kafkaProducer.sendFireAndForgot(KafkaTopic.SYNC_TOPIC.getTopic(), ward.getWardCode(), locationDocument);
    }
}
