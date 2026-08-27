package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.ProductRequest;
import byurens.dto.ProductResponse;
import byurens.entities.Category;
import byurens.entities.Product;
import byurens.exception.ByurensCafeException;
import byurens.repository.CategoryRepository;
import byurens.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ByurensCafeException("Category not found"));

        Product product = Product.builder()
            .name(request.name())
            .productType(request.productType())
            .category(category)
            .isAvailable(request.isAvailable())
            .build();

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
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

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getProductType(),
            product.getCategory().getLabel(),
            product.isAvailable()
        );
    }
}   
