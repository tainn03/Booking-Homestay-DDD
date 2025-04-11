package nnt.com.infrastructure.config.context;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class LongToTimestampConverter implements Converter<Long, Timestamp> {
    @Override
    public Timestamp convert(Long source) {
        return new Timestamp(source);
    }
}
