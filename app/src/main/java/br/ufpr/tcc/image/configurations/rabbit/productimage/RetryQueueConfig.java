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
public class RetryQueueConfig {

  private final QueueProperties queueProperties;

  @Bean
  public SimpleRabbitListenerContainerFactory retryQueueContainerFactory(
      final ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory listenerFactory =
        new SimpleRabbitListenerContainerFactory();
    listenerFactory.setConnectionFactory(connectionFactory);
    listenerFactory.setPrefetchCount(queueProperties.getRetryQueueProperties().getPrefetchCount());
    listenerFactory.setConcurrentConsumers(queueProperties.getRetryQueueProperties().getConcurrentConsumers());
    listenerFactory.setMaxConcurrentConsumers(queueProperties.getRetryQueueProperties().getMaxConcurrentConsumers());
    return listenerFactory;
  }

  @Bean
  public Queue retryQueue() {
    return new Queue(queueProperties.getRetryQueueProperties().getName(), true);
  }

  @Bean
  public Binding retryBinding(
          final DirectExchange imageExchange, final Queue retryQueue) {
    return BindingBuilder.bind(retryQueue)
        .to(imageExchange)
        .with(queueProperties.getRetryQueueProperties().getRoutingKey());
  }
}
