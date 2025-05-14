package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.service.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management",
        description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Get all categories",
            description = "Get list of all categories")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<CategoryDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @Operation(summary = "Get category",
            description = "Get specific category by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Create a new category",
            description = "Create a new category")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createCategory(
            @Valid @RequestBody CreateCategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @Operation(summary = "Update category",
            description = "Update specific category by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequestDto categoryRequestDto
    ) {
        return categoryService.update(id, categoryRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
