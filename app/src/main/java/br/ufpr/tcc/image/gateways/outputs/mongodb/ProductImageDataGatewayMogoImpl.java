package br.ufpr.tcc.image.gateways.outputs.mongodb;

import br.ufpr.tcc.image.domains.ProductImage;
import br.ufpr.tcc.image.gateways.outputs.ProductImageDataGateway;
import br.ufpr.tcc.image.gateways.outputs.mongodb.documents.ProductImageDocument;
import br.ufpr.tcc.image.gateways.outputs.mongodb.repositories.ProductImageRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageDataGatewayMogoImpl implements ProductImageDataGateway {

  private final ProductImageRepository productImageRepositoroy;

  @Override
  public ProductImage save(final ProductImage productImage) {
    return productImageRepositoroy.save(new ProductImageDocument(productImage)).toDomain();
  }

  @Override
  public Optional<ProductImage> findByProductId(final String productId) {
    return productImageRepositoroy.findById(productId).map(ProductImageDocument::toDomain);
  }
}
