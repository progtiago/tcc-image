package br.ufpr.tcc.image.domains;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {

  private Integer position;
  private String originalUri;
  private String localUri;
  private ImageStatus imageStatus;
  private String errorMessage;

}
