package org.ltk.kafconsumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class BitcoinMarketInfoConsumer {
    private static final String TOPIC = "BTCUSDT";

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(TOPIC)
                .partitions(12)
                .replicas(3)
                .build();
    }

    @KafkaListener(id = "BitcoinMarketInfoConsumerId", topics = TOPIC)
    public void listen(String in) {
        System.out.println(in);
    }
}
