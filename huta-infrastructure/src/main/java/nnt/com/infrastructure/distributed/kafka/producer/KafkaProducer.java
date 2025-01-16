package nnt.com.infrastructure.distributed.kafka.producer;

import java.util.concurrent.ExecutionException;

public interface KafkaProducer {

    void sendFireAndForgot(String topic, String key, Object message);

    void sendSync(String topic, String key, Object message) throws ExecutionException, InterruptedException;

    void sendAsync(String topic, String key, Object message);
}
