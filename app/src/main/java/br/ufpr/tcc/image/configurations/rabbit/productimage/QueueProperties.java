package br.ufpr.tcc.image.configurations.rabbit.productimage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("spring.rabbitmq")
public class QueueProperties {

  private String exchange;

  @Autowired
  private DownloadQueueProperties downloadQueueProperties;

  @Autowired
  private RetryQueueProperties retryQueueProperties;

  @Autowired
  private RetryDelayQueueProperties retryDelayQueueProperties;

  @Data
  @Configuration
  @ConfigurationProperties("spring.rabbitmq.queues.product-images.download")
  public class DownloadQueueProperties {
    private String name;
    private String routingKey;
    private Integer prefetchCount;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
  }
  
  @Data
  @Configuration
  @ConfigurationProperties("spring.rabbitmq.queues.product-images.retry")
  public class RetryQueueProperties {
    private String name;
    private String routingKey;
    private Integer prefetchCount;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
  }

  @Data
  @Configuration
  @ConfigurationProperties("spring.rabbitmq.queues.product-images.retry-delay")
  public class RetryDelayQueueProperties {
    private String name;
    private String routingKey;
    private Integer prefetchCount;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
    private Integer delayInMs;
  }
}
