package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.ProductRequest;
import byurens.dto.ProductResponse;
import byurens.entities.AddOnGroup;
import byurens.entities.Category;
import byurens.entities.Product;
import byurens.entities.ProductVariant;
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
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Product not found"));

        Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ByurensCafeException("Category not found"));

        existingProduct.setName(request.name());
        existingProduct.setProductType(request.productType());
        existingProduct.setCategory(category);
        existingProduct.setAvailable(request.isAvailable());

        existingProduct.getVariants().clear();
        if (request.variants() != null) {
            request.variants().forEach(variant -> {
                ProductVariant newVariant = ProductVariant.builder()
                    .product(existingProduct)
                    .size(variant.size())
                    .price(variant.price())
                    .onSale(variant.onSale())
                    .onSalePercent(variant.onSalePercent())
                    .capital(variant.capital())
                    .isPromo(variant.isPromo())
                    .build();
                existingProduct.getVariants().add(newVariant);
            });
        }

        existingProduct.getAddOnGroups().clear();
        if (request.addOnGroups() != null) {
            request.addOnGroups().forEach(group -> {
                AddOnGroup newGroup = AddOnGroup.builder()
                    .name(group.name())
                    .minSelection(group.minSelection())
                    .maxSelection(group.maxSelection())
                    .build(); 
                existingProduct.getAddOnGroups().add(newGroup);
            });
        }

        Product savedProduct = productRepository.save(existingProduct);
        return mapToResponse(savedProduct);
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
            .map(this::mapToResponse).toList();
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Product not found"));
        return mapToResponse(product);
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
