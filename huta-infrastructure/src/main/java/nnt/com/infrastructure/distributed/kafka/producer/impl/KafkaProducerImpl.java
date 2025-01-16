package nnt.com.infrastructure.distributed.kafka.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        kafkaTemplate.send(topic, key, message);
        log.info("GỬI {}: KEY {}, VALUE {} BẰNG FIRE AND FORGET VỚI TOTAL TIME = {} ms", topic, key, message, System.currentTimeMillis() - startTime);
    }

    @Override
    public void sendSync(String topic, String key, Object message) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        SendResult<String, Object> result = future.get();
        log.info("GỬI {}: KEY {}, VALUE {} BẰNG SYNC VỚI TOTAL TIME = {} ms VÀ KẾT QUẢ LÀ {}", topic, key, message, System.currentTimeMillis() - startTime, result.getRecordMetadata());
    }

    @Override
    public void sendAsync(String topic, String key, Object message) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("2: GỬI {}: KEY {}, VALUE {} BẰNG ASYNC VỚI TOTAL TIME = {} ms VÀ KẾT QUẢ LÀ {}", topic, key, message, System.currentTimeMillis() - startTime, result.getRecordMetadata());
            } else {
                log.error("2: GỬI {}: KEY {}, VALUE {}BẰNG ASYNC VỚI TOTAL TIME = {} ms VÀ CÓ LỖI {}", topic, key, message, System.currentTimeMillis() - startTime, ex);
            }
        });
        log.info("1: GỬI {}: KEY {}, VALUE {} BẰNG ASYNC VỚI TOTAL TIME = {} ms TRONG KHI CHƯA CÓ KẾT QUẢ", topic, key, message, System.currentTimeMillis() - startTime);
    }
}
