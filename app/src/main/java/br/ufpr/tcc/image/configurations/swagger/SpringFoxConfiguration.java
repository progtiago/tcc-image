package br.ufpr.tcc.image.configurations.swagger;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Profile("!test")
public class SpringFoxConfiguration {

  private static final String BASE_PACKAGE = "br.ufpr.tcc.image";

  @Bean
  public Docket docket() throws IOException {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo())
        .useDefaultResponseMessages(false);
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .title("TCC Image")
        .description("App to download product images")
        .version("1")
        .build();
  }
}
