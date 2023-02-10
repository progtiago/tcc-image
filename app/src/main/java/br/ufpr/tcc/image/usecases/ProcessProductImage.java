package br.ufpr.tcc.image.usecases;

import static br.ufpr.tcc.image.domains.ImageStatus.PROCESSING;

import br.ufpr.tcc.image.configurations.graphics.ImageConfigProperties;
import br.ufpr.tcc.image.domains.ProductImage;
import br.ufpr.tcc.image.domains.ProductImageStatus;
import br.ufpr.tcc.image.exceptions.DownloadImageException;
import br.ufpr.tcc.image.exceptions.ImageNotFoundException;
import br.ufpr.tcc.image.gateways.internals.rabbitmq.productimage.RetryDelayProvider;
import br.ufpr.tcc.image.gateways.outputs.ProductImageDataGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessProductImage {

  private final ProcessImage processImage;
  private final ImageConfigProperties imageConfigProperties;
  private final ProductImageDataGateway productImageDataGateway;
  private final RetryDelayProvider retryDelayProvider;

  public void execute(final String productId) {
    log.info("Starting images download for productId: {}", productId);
    var productImage = productImageDataGateway.findByProductId(productId).orElseThrow();
    productImage.defineProcessing();
    processImages(productImage);
    productImageDataGateway.save(productImage);
  }

  private void processImages(final ProductImage productImage) {
    try {
      productImage.getImagesByStatus(PROCESSING).forEach(
          image -> processImage.execute(image, productImage.getMetadata()));

      productImage.setStatus(ProductImageStatus.SUCCESS);

    } catch (ImageNotFoundException ie) {

      productImage.skipAllProcessingImages();
      productImage.setStatus(ProductImageStatus.SUCCESS);

    } catch (DownloadImageException de) {

      if(canRetry(de, productImage)) {
        retryDelayProvider.send(productImage.getProductId());
        productImage.setStatus(ProductImageStatus.ON_RETRY);
      } else {
        productImage.skipAllProcessingImages();
        productImage.setStatus(ProductImageStatus.ERROR);
      }

    } catch (Exception e) {

      productImage.skipAllProcessingImages();
      productImage.setStatus(ProductImageStatus.ERROR);
      productImage.setErrorMessage(e.getMessage());

    }
  }

  private boolean canRetry(final DownloadImageException de, final ProductImage productImage) {
    return de.isRetryable() &&
        productImage.getAttempt() < imageConfigProperties.getDownload().getLimitAttempts();
  }
}
