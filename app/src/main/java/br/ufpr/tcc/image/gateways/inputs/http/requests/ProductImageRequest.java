package br.ufpr.tcc.image.gateways.inputs.http.requests;

import static br.ufpr.tcc.image.domains.ProductImageStatus.PENDING;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import br.ufpr.tcc.image.domains.Metadata;
import br.ufpr.tcc.image.domains.ProductImage;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductImageRequest implements Serializable {

  private static final long serialVersionUID = -8255912752645640058L;

  @NotNull
  private String productId;
  @NotEmpty
  private List<ImageRequest> images;

  public ProductImage toDomain(final String imageDir, final Boolean stopDownloadAfter404) {
    return ProductImage.builder()
        .productId(this.productId)
        .images(emptyIfNull(this.images)
            .stream().map(ImageRequest::toDomain).collect(toList()))
        .metadata(new Metadata(imageDir, stopDownloadAfter404))
        .attempt(0)
        .status(PENDING)
        .build();
  }
}
