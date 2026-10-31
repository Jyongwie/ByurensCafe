package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.CategoryRequest;
import byurens.dto.CategoryResponse;
import byurens.entities.Category;
import byurens.exception.ByurensCafeException;
import byurens.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional 
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
            .label(request.label())
            .isActive(request.isActive())
            .taskTarget(request.taskTarget())
            .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Transactional 
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Category not found"));

        existingCategory.setLabel(request.label());
        existingCategory.setIsActive(request.isActive());
        existingCategory.setTaskTarget(request.taskTarget());

        Category savedCategory = categoryRepository.save(existingCategory);
        return mapToResponse(savedCategory);
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
            .map(this::mapToResponse).toList();
    }

    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Category not found"));
        return mapToResponse(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getLabel(),
            category.getIsActive(),
            category.getTaskTarget()
        );
    }
}
