package br.ufpr.tcc.image.domains;

import static br.ufpr.tcc.image.domains.ImageStatus.ERROR;
import static br.ufpr.tcc.image.domains.ImageStatus.PENDING;
import static br.ufpr.tcc.image.domains.ImageStatus.PROCESSING;
import static br.ufpr.tcc.image.domains.ImageStatus.SKIPPED;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImage {

  private String productId;
  private List<Image> images;
  private Integer attempt;
  private Metadata metadata;
  private ProductImageStatus status;
  private String errorMessage;

  public void incrementAttempt() {
    this.attempt++;
  }

  public List<Image> getImagesByStatus(final ImageStatus...imageStatus) {
    return emptyIfNull(this.images).stream()
        .filter(image -> asList(imageStatus).contains(image.getImageStatus())).collect(toList());
  }

  public void defineProcessing() {
    this.getImagesByStatus(PENDING, ERROR)
        .forEach(image -> image.setImageStatus(PROCESSING));
    this.incrementAttempt();
    this.setStatus(ProductImageStatus.PROCESSING);
  }

  public void skipAllProcessingImages() {
    emptyIfNull(this.getImages()).stream()
        .filter(image -> PROCESSING.equals(image.getImageStatus()))
        .forEach(image -> image.setImageStatus(SKIPPED));
  }

}
