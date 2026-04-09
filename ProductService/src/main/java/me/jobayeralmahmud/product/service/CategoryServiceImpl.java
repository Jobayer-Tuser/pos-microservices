package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.repository.CategoryRepository;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.response.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final  CategoryRepository repository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return repository.retrieveAllCategories();
    }

    @Override
    public Category getCategoryById(UUID id) {
        return findCategoryById(id);
    }

    @Override
    public Category createCategory(CreateCategoryRequest request) {
        var category = Category.builder()
                .name(request.name())
                .description(request.description())
                .parentId(request.parentId())
                .build();
        return repository.save(category);
    }

    @Override
    public Category updateCategory(UUID id, UpdateCategoryRequest request) {
        var category = findCategoryById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        return repository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Category getCategoryReference(UUID id) {
        return repository.getReferenceById(id);
    }

    private Category findCategoryById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Category not found with id: " + id));
    }
}