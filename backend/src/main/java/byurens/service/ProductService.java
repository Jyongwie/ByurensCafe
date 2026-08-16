package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.entities.Product;
import byurens.exception.ByurensCafeException;
import byurens.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(UUID id, Product product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setProductType(product.getProductType());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setAvailable(product.isAvailable());

        existingProduct.getVariants().clear();
        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> {
                variant.setProduct(existingProduct);
                existingProduct.getVariants().add(variant);
            });
        }

        existingProduct.getAddOnGroups().clear();
        if (product.getAddOnGroups() != null) {
            existingProduct.getAddOnGroups().addAll(product.getAddOnGroups());
        }

        return productRepository.save(existingProduct);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Product not found"));
    }
}   
