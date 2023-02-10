package br.ufpr.tcc.image.gateways.inputs.http.requests;

import br.ufpr.tcc.image.domains.Image;
import br.ufpr.tcc.image.domains.ImageStatus;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImageRequest implements Serializable {

  private static final long serialVersionUID = 5405306607608310934L;

  @NotNull
  private Integer position;
  @NotEmpty
  private String originalUri;

  public Image toDomain() {
    return Image.builder()
        .position(this.position)
        .originalUri(this.originalUri)
        .imageStatus(ImageStatus.PENDING)
        .build();
  }
}
