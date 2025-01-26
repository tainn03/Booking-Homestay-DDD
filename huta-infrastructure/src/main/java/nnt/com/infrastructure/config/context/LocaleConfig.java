package nnt.com.infrastructure.config.context;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.TimeZone;

@Configuration
@Slf4j
public class LocaleConfig {

    @PostConstruct
    public void init() {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        log.info("Date in Asia/Ho_Chi_Minh: {}", new Date());
    }
}