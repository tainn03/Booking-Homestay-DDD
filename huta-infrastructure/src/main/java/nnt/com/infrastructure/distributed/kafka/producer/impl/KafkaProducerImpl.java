package nnt.com.infrastructure.distributed.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class KafkaProducerImpl implements KafkaProducer {
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendFireAndForgot(String topic, String key, Object message) {
        long startTime = System.currentTimeMillis();
        send(topic, key, message);
        log.info("GỬI YÊU CẦU {}:{}-{} BẰNG FIRE AND FORGET VỚI TOTAL TIME = {} ms", topic, key, message, System.currentTimeMillis() - startTime);
    }

    @Override
    public void sendSync(String topic, String key, Object message) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<SendResult<String, Object>> future = send(topic, key, message);
        try {
            SendResult<String, Object> result = future.get();
            log.info("GỬI YÊU CẦU {}:{}-{} BẰNG SYNC VỚI TOTAL TIME = {} ms VÀ KẾT QUẢ LÀ {}", topic, key, message, System.currentTimeMillis() - startTime, result.getRecordMetadata());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendAsync(String topic, String key, Object message) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<SendResult<String, Object>> future = send(topic, key, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("2: PHẢN HỒI YÊU CẦU {}:{}-{} BẰNG ASYNC VỚI TOTAL TIME = {} ms VÀ KẾT QUẢ LÀ {}", topic, key, message, System.currentTimeMillis() - startTime, result.getRecordMetadata());
            } else {
                log.error(ex.getMessage(), ex);
            }
        });
        log.info("1: GỬI YÊU CẦU {}:{}-{} BẰNG ASYNC VỚI TOTAL TIME = {} ms", topic, key, message, System.currentTimeMillis() - startTime);
    }

    private CompletableFuture<SendResult<String, Object>> send(String topic, String key, Object message) {
        if (key == null) {
            return kafkaTemplate.send(topic, message);
        }
        return kafkaTemplate.send(topic, key, message);
    }
}
