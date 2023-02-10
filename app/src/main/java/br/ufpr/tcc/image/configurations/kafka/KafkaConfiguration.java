package br.ufpr.tcc.image.configurations.kafka;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

  private final KafkaProperties kafkaProperties;

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
      kafkaListenerContainerFactory() {
    final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
    factory.setConsumerFactory(
        new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties()));
    factory.getContainerProperties().setAckMode(RECORD);
    factory.setStatefulRetry(true);
    return factory;
  }
}
