package br.ufpr.tcc.image.gateways.outputs.mongodb.documents;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import br.ufpr.tcc.image.domains.ProductImage;
import br.ufpr.tcc.image.domains.ProductImageStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document("productImages")
public class ProductImageDocument {

  @Id
  private String productId;
  private List<ImageDocument> images;
  private Integer attempt;
  private MetadataDocument metadata;
  @Indexed
  private ProductImageStatus status;
  private String errorMessage;
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

  public ProductImageDocument(final ProductImage productImage) {
    this.productId = productImage.getProductId();
    this.images = emptyIfNull(productImage.getImages())
        .stream().map(ImageDocument::new).collect(toList());
    this.attempt = productImage.getAttempt();
    this.status = productImage.getStatus();
    this.errorMessage = productImage.getErrorMessage();
    this.metadata = ofNullable(productImage.getMetadata())
        .map(MetadataDocument::new).orElse(null);
  }

  public ProductImage toDomain() {
    return ProductImage.builder()
        .productId(this.productId)
        .images(emptyIfNull(this.images)
            .stream().map(ImageDocument::toDomain).collect(toList()))
        .attempt(this.attempt)
        .status(this.status)
        .errorMessage(this.errorMessage)
        .metadata(ofNullable(this.metadata)
            .map(MetadataDocument::toDomain).orElse(null))
        .build();
  }
}
