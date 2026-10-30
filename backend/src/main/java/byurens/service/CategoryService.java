package byurens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.CategoryRequest;
import byurens.dto.CategoryResponse;
import byurens.entities.Category;
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

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getLabel(),
            category.getIsActive(),
            category.getTaskTarget()
        );
    }
}
