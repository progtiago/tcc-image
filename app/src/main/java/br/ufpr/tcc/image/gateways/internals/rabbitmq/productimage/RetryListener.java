package br.ufpr.tcc.image.gateways.internals.rabbitmq.productimage;

import br.ufpr.tcc.image.configurations.rabbit.productimage.QueueProperties;
import br.ufpr.tcc.image.usecases.ProcessProductImage;
import br.ufpr.tcc.image.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryListener {

  private final ProcessProductImage processProductImage;
  private final QueueProperties queueProperties;
  private final JsonUtils jsonUtils;

  @RabbitListener(
      queues = "${spring.rabbitmq.queues.product-images.retry.name}",
      containerFactory = "retryQueueContainerFactory")
  public void execute(final Message message) {
    try {
      var productId = jsonUtils.toObject(message.getBody(), String.class);
      processProductImage.execute(productId);
    } catch (Exception ex) {
      log.error("Rabbit Listener {} has failed for message {}.",
          queueProperties.getRetryQueueProperties().getName(),
          message,
          ex);
    }
  }

}
