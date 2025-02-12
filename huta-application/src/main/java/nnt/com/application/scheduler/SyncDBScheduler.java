package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.document.LocationDocument;
import nnt.com.domain.aggregates.model.dto.response.District;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.Province;
import nnt.com.domain.aggregates.model.dto.response.Ward;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import nnt.com.domain.aggregates.service.LocationSearchDomainService;
import nnt.com.domain.shared.model.vo.KafkaTopic;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SyncDBScheduler {
    final HomestayDomainService HomestayDomainService;
    final HomestaySearchDomainService homestaySearchDomainService;
    final KafkaProducer kafkaProducer;

    final RestTemplate restTemplate = new RestTemplate();
    final LocationSearchDomainService locationSearchDomainService;

    @Value("${application.ghn.token}")
    String token;

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
        List<Province> provinces = getProvinces();
        provinces.forEach(province -> {
            log.info("PROVINCE {}: {}", province.getProvinceID(), province.getProvinceName());
            List<District> districts = getDistricts(province.getProvinceID());
            districts.forEach(district -> {
                log.info("DISTRICT {}: {}", district.getDistrictID(), district.getDistrictName());
                List<Ward> wards = getWards(district.getDistrictID());
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
        locationSearchDomainService.save(LocationDocument.builder()
                .id(ward.getWardCode())
                .ward(ward.getWardName())
                .district(district.getDistrictName())
                .city(province.getProvinceName())
                .build());
    }

    private List<Province> getProvinces() {
        String provinceUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        List<Map<String, Object>> data = getData(provinceUrl);
        return data.stream()
                .map(item -> Province.builder()
                        .ProvinceID((Integer) item.get("ProvinceID"))
                        .ProvinceName((String) item.get("ProvinceName"))
                        .build())
                .collect(Collectors.toList());
    }

    private List<District> getDistricts(int provinceID) {
        String districtUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceID;
        List<Map<String, Object>> data = getData(districtUrl);
        return data.stream()
                .map(item -> District.builder()
                        .DistrictID((Integer) item.get("DistrictID"))
                        .DistrictName((String) item.get("DistrictName"))
                        .ProvinceID((Integer) item.get("ProvinceID"))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Ward> getWards(int districtID) {
        String wardUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtID;
        List<Map<String, Object>> data = getData(wardUrl);
        return data.stream()
                .map(item -> Ward.builder()
                        .WardCode((String) item.get("WardCode"))
                        .WardName((String) item.get("WardName"))
                        .DistrictID((Integer) item.get("DistrictID"))
                        .build())
                .collect(Collectors.toList());
    }

    private HttpEntity<String> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        return new HttpEntity<>(headers);
    }

    private List<Map<String, Object>> getData(String url) {
        HttpEntity<String> entity = getEntity();
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getBody().get("data") == null ? List.of() : (List<Map<String, Object>>) response.getBody().get("data");
        } catch (Exception e) {
            log.error("Error fetching data from URL: " + url, e);
            try {
                Thread.sleep(1000 * 60); // Wait for 1 minute
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return getData(url); // Retry
        }
    }

}
