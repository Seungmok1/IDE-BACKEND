package everyide.webide.websocket.kafka;

import java.util.UUID;

public final class KafkaProperties {
    public static String name = UUID.randomUUID().toString();
    public static final String KAFKA_TOPIC = "kafka";
    public static final String GROUP_ID = "group1";
    public static final String BROKER = "localhost:9092";
}
