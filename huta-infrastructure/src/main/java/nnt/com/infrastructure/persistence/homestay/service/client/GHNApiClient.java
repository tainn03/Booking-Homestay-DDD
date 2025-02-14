package nnt.com.infrastructure.persistence.homestay.service.client;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class GHNApiClient {
    final RestTemplate restTemplate = new RestTemplate();
    @Value("${application.ghn.token}")
    String token;

    public List<Map<String, Object>> callApiGetProvinces() {
        String provinceUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        return getData(provinceUrl);
    }

    public List<Map<String, Object>> callApiGetDistricts(int provinceID) {
        String districtUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceID;
        return getData(districtUrl);
    }

    public List<Map<String, Object>> callApiGetWards(int districtID) {
        String wardUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtID;
        return getData(wardUrl);
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

    private HttpEntity<String> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        return new HttpEntity<>(headers);
    }
}
