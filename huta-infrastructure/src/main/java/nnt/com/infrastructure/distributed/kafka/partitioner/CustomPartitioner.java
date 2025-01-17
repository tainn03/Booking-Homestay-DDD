package nnt.com.infrastructure.distributed.kafka.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Slf4j
public class CustomPartitioner implements Partitioner {
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionsForTopic(topic).size();
        int partition = counter.getAndIncrement() % numPartitions;
        log.info("PARTITION: " + partition);
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
