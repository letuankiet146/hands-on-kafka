# hands-on-kafka

Hands-on project that demonstrates connecting to crypto exchanges, consuming market data and producing it to Kafka topics. The workspace contains an exchange connector library and simple producer/consumer demos.

## Repository layout
- [crypto-ex-connector](crypto-ex-connector) — connector modules and common library
  - [connector module](crypto-ex-connector/connector) — exchange clients, requesters, websockets and services  
  - [common_lib module](crypto-ex-connector/common_lib) — shared models and helpers
- [producer-consumer-apps](producer-consumer-apps) — example Kafka apps
  - [kafproducer](producer-consumer-apps/kafproducer) — Kafka producer app
  - [kafconsumer](producer-consumer-apps/kafconsumer) — Kafka consumer app
- [kafka-cluster/docker-compose.yml](kafka-cluster/docker-compose.yml) — local Kafka (Kraft ver) setup for testing

## Quick start

Prerequisites: Java 21, Docker (for local Kafka), or a running Kafka cluster.

1. Start local Kafka (optional)
   - docker compose up -d from [kafka-cluster/docker-compose.yml](kafka-cluster/docker-compose.yml)

2. Build modules
   - Build connector and common lib:
     - mvn -pl crypto-ex-connector clean install
   - Or use the maven wrapper in producer apps:
     - cd producer-consumer-apps/kafproducer && ./mvnw package
     - cd producer-consumer-apps/kafconsumer && ./mvnw package

3. Configure environment variables used by the connector (see [`ApplicationConfig`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/config/ApplicationConfig.java) for beans created from env):
   - API_KEY, SECRET_KEY, EMAIL, CHAT_ID
   - Optional trading envs used by [`TradingConstant`](crypto-ex-connector/common_lib/src/main/java/org/ltk/model/TradingConstant.java): IS_TRADE, SYMBOL, LEVERAGE, QUANTITY, TP_TARGET_PERCENT, SL_TARGET_PERCENT

4. Run the producer example
   - Run the Spring Boot app in [producer-consumer-apps/kafproducer](producer-consumer-apps/kafproducer) (IDE run or):
     - ./mvnw spring-boot:run
   - The producer uses [`ExchangeService`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/service/ExchangeService.java) to subscribe to mark price and publishes to Kafka via [`org.ltk.BitcoinPriceRunner`](producer-consumer-apps/kafproducer/src/main/java/org/ltk/BitcoinPriceRunner.java).

## Tests
- Connector module tests exercise utilities and requesters:
  - Example tests: [crypto-ex-connector/connector/src/test/java/org/ltk/connector/utils/HashAlgorithmTest.java](crypto-ex-connector/connector/src/test/java/org/ltk/connector/utils/HashAlgorithmTest.java), [crypto-ex-connector/connector/src/test/java/org/ltk/connector/requestor/ws/BingXFutureWebSocketTest.java](crypto-ex-connector/connector/src/test/java/org/ltk/connector/requestor/ws/BingXFutureWebSocketTest.java)
- Run tests with Maven:
  - mvn -pl crypto-ex-connector/connector test

## Important files / configuration
- Connector application config: [crypto-ex-connector/connector/src/main/resources/application.yml](crypto-ex-connector/connector/src/main/resources/application.yml)
- Connector beans and env wiring: [crypto-ex-connector/connector/src/main/java/org/ltk/connector/config/ApplicationConfig.java](crypto-ex-connector/connector/src/main/java/org/ltk/connector/config/ApplicationConfig.java)
- Exchange REST endpoints and WSS URIs: [`RESTApiUrl`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/client/RESTApiUrl.java)
- Example client implementations:
  - [`BingXExFutureClientImpl`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/client/impl/BingXExFutureClientImpl.java)
  - [`BinanceExFutureClientImpl`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/client/impl/BinanceExFutureClientImpl.java)
  - [`OKXExFutureClientImpl`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/client/impl/OKXExFutureClientImpl.java)

## Notes & tips
- URL and signing helpers are in [`UrlBuilder`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/utils/UrlBuilder.java) and [`SecurityHelper`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/utils/SecurityHelper.java).
- Default recv window used for exchange signing: [`ConnectorConst.DEFAULT_RECV_WINDOW`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/utils/ConnectorConst.java).
- The connector provides default (unsupported) implementations in [`ExFutureClient`](crypto-ex-connector/connector/src/main/java/org/ltk/connector/client/ExFutureClient.java) — exchange-specific clients override those methods.

If you want, I can:
- add a short run script for starting Kafka + producer,
- or generate a minimal docker-compose for the producer/consumer demo.
