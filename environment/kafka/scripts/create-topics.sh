# Wait for Kafka to be ready
until /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092; do
  echo "Waiting for Kafka to be ready..."
  sleep 2
done

# Create topics
/opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic user.mail
/opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic booking.mail
/opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic payment.mail
/opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic homestay.recommendation

echo "Topics created."
