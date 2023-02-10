package br.ufpr.tcc.image.configurations.resttemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import static java.time.Duration.ofMillis;

@Configuration
public class RestTemplateConfiguration {

  @Value("${spring.rest-template.connect-timeout:5000}")
  private Integer connectTimeout;

  @Value("${spring.rest-template.read-timeout:10000}")
  private Integer readTimeout;

  @Bean
  @Primary
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setConnectTimeout(ofMillis(connectTimeout))
        .setReadTimeout(ofMillis(readTimeout))
        .build();
  }
}
