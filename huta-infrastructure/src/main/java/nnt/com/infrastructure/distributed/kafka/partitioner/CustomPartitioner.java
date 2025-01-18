package nnt.com.infrastructure.distributed.kafka.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.utils.Utils;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Slf4j
public class CustomPartitioner implements Partitioner {

    // Viết custom partitioner để tránh sticky partition: sticky partition là cơ chế mà một partition nhận nhiều message hơn các partition khác
    // Mục đích của custom partitioner là để phân bố message đến các partition một cách đồng đều
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionsForTopic(topic).size();
        if (keyBytes == null) {
            return (int) (Math.random() * numPartitions);
        }
        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
