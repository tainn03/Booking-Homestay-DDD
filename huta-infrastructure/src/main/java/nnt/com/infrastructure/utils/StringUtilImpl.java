package nnt.com.infrastructure.utils;

import nnt.com.domain.shared.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class StringUtilImpl implements StringUtil {
    @Override
    public String generateRandomPassword() {
        int length = 8;
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return password.toString();
    }
}
