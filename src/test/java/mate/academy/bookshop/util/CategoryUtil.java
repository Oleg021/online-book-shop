package mate.academy.bookshop.util;

import java.util.List;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.model.Category;

public class CategoryUtil {
    public static CreateCategoryRequestDto buildCreateCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Test Category");
        dto.setDescription("Test description");
        return dto;
    }

    public static CategoryDto buildCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Test Category");
        dto.setDescription("Test description");
        return dto;
    }

    public static CategoryDto buildSampleCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Category 1");
        dto.setDescription("Description 1");
        return dto;
    }

    public static List<CategoryDto> buildSampleCategoryDtoList() {
        CategoryDto cat1 = new CategoryDto();
        cat1.setId(1L);
        cat1.setName("Category 1");
        cat1.setDescription("Description 1");

        CategoryDto cat2 = new CategoryDto();
        cat2.setId(2L);
        cat2.setName("Category 2");
        cat2.setDescription("Description 2");

        CategoryDto cat3 = new CategoryDto();
        cat3.setId(3L);
        cat3.setName("Category 3");
        cat3.setDescription("Description 3");

        return List.of(cat1, cat2, cat3);
    }

    public static CategoryDto buildCategoryDtoWithId(Long id) {
        CategoryDto dto = buildCategoryDto();
        dto.setId(id);
        return dto;
    }

    public static CreateCategoryRequestDto buildUpdateCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Updated Category");
        dto.setDescription("Updated description");
        return dto;
    }

    public static CategoryDto buildUpdatedCategoryDto(Long id) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName("Updated Category");
        dto.setDescription("Updated description");
        return dto;
    }

    public static Category buildSampleCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 1");
        category.setDeleted(false);
        return category;
    }

    public static CreateCategoryRequestDto buildUpdatedCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Updated category name");
        dto.setDescription("Updated category description");
        return dto;
    }

    public static Category buildExistingCategory(Long id) {
        Category category = new Category();
        category.setId(id);
        category.setName("New category name");
        category.setDescription("New category description");
        category.setDeleted(false);
        return category;
    }
}
