package br.ufpr.tcc.image.gateways.outputs.mongodb.repositories;

import br.ufpr.tcc.image.gateways.outputs.mongodb.documents.ProductImageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository extends MongoRepository<ProductImageDocument, String> {
}
