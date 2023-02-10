package br.ufpr.tcc.image.gateways.outputs;

import br.ufpr.tcc.image.domains.ProductImage;
import java.util.Optional;

public interface ProductImageDataGateway {

  ProductImage save(ProductImage productImage);

  Optional<ProductImage> findByProductId(String productId);

}
