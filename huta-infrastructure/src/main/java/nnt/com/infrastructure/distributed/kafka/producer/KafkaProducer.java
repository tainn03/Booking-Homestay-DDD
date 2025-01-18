package nnt.com.infrastructure.distributed.kafka.producer;

public interface KafkaProducer {

    void sendFireAndForgot(String topic, String key, Object message);

    void sendSync(String topic, String key, Object message);

    void sendAsync(String topic, String key, Object message);
}
