package br.ufpr.tcc.image.configurations.graphics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tcc-image")
public class ImageConfigProperties {
  private final Download download = new Download();
  private final Resize resize = new Resize();

  @Getter
  @Setter
  public static class Download {
    private String rootDir;
    private int connectionTimeoutMillis;
    private int readTimeoutMillis;
    private String userAgent;
    private int maxRedirects;
    private int limitAttempts;
  }

  @Getter
  @Setter
  public static class Resize {
    private int height;
    private int width;
  }
}
