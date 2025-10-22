package org.ltk;

import org.apache.kafka.clients.admin.NewTopic;
import org.ltk.connector.client.ExchangeName;
import org.ltk.connector.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BitcoinPriceRunner implements CommandLineRunner {

    public static final String TOPIC = "BTCUSDT";
    @Autowired
    private ExchangeService exchangeService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BitcoinPriceRunner(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(TOPIC)
                .partitions(12)
                .replicas(3)
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("BitcoinPriceRunner is running...");
//        exchangeService.subscribeForceOrder(ExchangeName.BINANCE, BTCUSDT, this::processResponse);
        exchangeService.subscribeMarkPrice(ExchangeName.BINANCE, "BTCUSDT", "1s", this::processResponse);
    }

    private void processResponse(String jsonResponse) {
        System.out.println(jsonResponse);
        kafkaTemplate.send(TOPIC, jsonResponse);
    }
}
