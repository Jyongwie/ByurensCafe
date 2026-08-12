package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import byurens.entities.Product;
import byurens.exception.ProductException;
import byurens.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(UUID id, Product product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setProductType(product.getProductType());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setAvailable(product.isAvailable());
        existingProduct.setVariants(product.getVariants());
        existingProduct.setAddOnGroups(product.getAddOnGroups());

        return productRepository.save(existingProduct);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductException("Product not found"));
    }
}   
