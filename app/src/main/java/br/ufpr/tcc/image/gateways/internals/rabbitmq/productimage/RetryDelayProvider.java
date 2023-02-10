package br.ufpr.tcc.image.gateways.internals.rabbitmq.productimage;

import br.ufpr.tcc.image.configurations.rabbit.productimage.QueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryDelayProvider {

  private final RabbitTemplate rabbitTemplate;
  private final QueueProperties queueProperties;

  public void send(final String productId) {
    final String routingKey = queueProperties.getRetryDelayQueueProperties().getRoutingKey();
    log.info("Sending image download event to delay queue. productId: {}", productId);
    rabbitTemplate.convertAndSend(queueProperties.getExchange(), routingKey, productId);
  }
}
