package nnt.com.domain.common.utils;

import java.util.Map;

public interface VNPayUtil {
    String hashAllFields(Map fields);

    String hmacSHA512(final String data);

    String getRandomNumber(int len);

    Map<String, String> createVnPayParams(int total, String orderInfor, String urlReturn);

    String createQueryUrl(String queryUrl, String vnp_SecureHash);
}
