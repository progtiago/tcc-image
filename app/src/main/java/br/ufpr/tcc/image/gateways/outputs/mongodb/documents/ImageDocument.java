package br.ufpr.tcc.image.gateways.outputs.mongodb.documents;

import br.ufpr.tcc.image.domains.Image;
import br.ufpr.tcc.image.domains.ImageStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDocument {

  private Integer position;
  private String originalUri;
  private String localUri;
  private ImageStatus imageStatus;
  private String errorMessage;

  public ImageDocument(final Image image) {
    this.originalUri = image.getOriginalUri();
    this.localUri = image.getLocalUri();
    this.position = image.getPosition();
    this.imageStatus = image.getImageStatus();
    this.errorMessage = image.getErrorMessage();
  }

  public Image toDomain() {
    return Image.builder()
        .position(this.position)
        .originalUri(this.originalUri)
        .localUri(this.localUri)
        .imageStatus(this.imageStatus)
        .errorMessage(this.errorMessage)
        .build();
  }
}