package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.library.utils.Slugify;
import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.repository.CategoryRepository;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final  CategoryRepository repository;

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return findCategoryById(id);
    }

    @Override
    public Category createCategory(CreateCategoryRequest request) {
        var category = Category.builder()
                .name(request.name())
                .description(request.description())
                .slug(Slugify.toSlug(request.name()))
                .parentId(request.parentId())
                .build();
        return repository.save(category);
    }

    @Override
    public Category updateCategory(Long id, UpdateCategoryRequest request) {
        var category = findCategoryById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        category.setSlug(Slugify.toSlug(request.name()));
        return repository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

    private Category findCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Category not found with id: " + id));
    }
}