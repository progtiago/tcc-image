package br.ufpr.tcc.image.configurations.rabbit.productimage;

import java.util.HashMap;
import java.util.Map;
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
public class RetryDelayQueueConfig {

  private final QueueProperties queueProperties;

  @Bean
  public SimpleRabbitListenerContainerFactory retryDelayContainerFactory(
      final ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory listenerFactory =
        new SimpleRabbitListenerContainerFactory();
    listenerFactory.setConnectionFactory(connectionFactory);
    listenerFactory.setPrefetchCount(queueProperties.getRetryDelayQueueProperties().getPrefetchCount());
    listenerFactory.setConcurrentConsumers(queueProperties.getRetryDelayQueueProperties().getConcurrentConsumers());
    listenerFactory.setMaxConcurrentConsumers(queueProperties.getRetryDelayQueueProperties().getConcurrentConsumers());
    return listenerFactory;
  }

  @Bean
  public Binding retryDelayRouterKeyBinding(
          final DirectExchange imageExchange, final Queue retryDelayQueue) {
    return BindingBuilder.bind(retryDelayQueue).to(imageExchange)
        .with(queueProperties.getRetryDelayQueueProperties().getRoutingKey());
  }

  @Bean
  public Queue retryDelayQueue() {
    Map<String, Object> queueArgs = new HashMap<>();
    queueArgs.put("x-delayed-type", "direct");
    queueArgs.put("x-dead-letter-exchange", queueProperties.getExchange());
    queueArgs.put("x-dead-letter-routing-key", queueProperties.getRetryQueueProperties().getRoutingKey());
    queueArgs.put("x-message-ttl", queueProperties.getRetryDelayQueueProperties().getDelayInMs());
    return new Queue(queueProperties.getRetryDelayQueueProperties().getName(), true, false, false, queueArgs);
  }

  @Bean
  public Binding retryDelayQueueBinding(
          final Queue retryDelayQueue, final DirectExchange imageExchange) {
    return BindingBuilder.bind(retryDelayQueue).to(imageExchange)
            .with(queueProperties.getRetryDelayQueueProperties().getRoutingKey());
  }
}
