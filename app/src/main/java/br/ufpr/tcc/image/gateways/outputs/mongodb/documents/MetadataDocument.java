package br.ufpr.tcc.image.gateways.outputs.mongodb.documents;

import br.ufpr.tcc.image.domains.Metadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDocument {

  private String imageDir;
  private Boolean stopDownloadAfter404;

  public MetadataDocument(final Metadata metadata) {
    this.imageDir = metadata.getImageDir();
    this.stopDownloadAfter404 = metadata.getStopDownloadAfter404();
  }

  public Metadata toDomain() {
    return Metadata.builder()
        .imageDir(this.imageDir)
        .stopDownloadAfter404(this.stopDownloadAfter404)
        .build();
  }
}
