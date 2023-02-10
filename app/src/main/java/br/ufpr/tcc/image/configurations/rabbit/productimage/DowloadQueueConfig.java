package br.ufpr.tcc.image.configurations.rabbit.productimage;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DowloadQueueConfig {

  private final QueueProperties queueProperties;

  @Bean
  public SimpleRabbitListenerContainerFactory downloadQueueContainerFactory(
      final ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory listenerFactory =
        new SimpleRabbitListenerContainerFactory();
    listenerFactory.setConnectionFactory(connectionFactory);
    listenerFactory.setPrefetchCount(queueProperties.getDownloadQueueProperties().getPrefetchCount());
    listenerFactory.setConcurrentConsumers(queueProperties.getDownloadQueueProperties().getConcurrentConsumers());
    listenerFactory.setMaxConcurrentConsumers(queueProperties.getDownloadQueueProperties().getMaxConcurrentConsumers());
    return listenerFactory;
  }

  @Bean
  public Queue downloadQueue() {
    return new Queue(queueProperties.getDownloadQueueProperties().getName(), true);
  }

  @Bean
  public Binding downloadBinding(
          final DirectExchange imageExchange, final Queue downloadQueue) {
    return BindingBuilder.bind(downloadQueue)
        .to(imageExchange)
        .with(queueProperties.getDownloadQueueProperties().getRoutingKey());
  }
}
