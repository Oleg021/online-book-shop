package mate.academy.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.CategoryMapper;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.category.CategoryRepository;
import mate.academy.bookshop.service.impl.CategoryServiceImpl;
import mate.academy.bookshop.util.CategoryUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllCategories_WithPageable_ShouldReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(new Category(), new Category());
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryDto> categoryDtos = List.of(new CategoryDto(), new CategoryDto());
        when(categoryMapper.toDto(Mockito.any(Category.class)))
                .thenAnswer(invocation -> {
                    Category category = invocation.getArgument(0);
                    int index = categories.indexOf(category);
                    return categoryDtos.get(index);
                });

        Page<CategoryDto> result = categoryService.getAll(pageable);

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper, Mockito.times(categories.size()))
                .toDto(Mockito.any(Category.class));

        assertEquals(categoryDtos, result.getContent());
    }

    @Test
    void getById_WithValidId_ShouldReturnValidCategory() {
        Long categoryId = 1L;

        Category category = CategoryUtil.buildSampleCategory();
        CategoryDto categoryDto = CategoryUtil.buildSampleCategoryDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(categoryId);

        assertNotNull(actual);
        assertEquals(categoryDto, actual); // порівняння об'єктів, якщо equals реалізований
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getById_WithInvalidCategoryId_ShouldReturnException() {
        Long categoryId = 10L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        String expected = "Category not found by id " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void save_ShouldSaveCategory() {
        CreateCategoryRequestDto requestDto = CategoryUtil.buildCreateCategoryRequestDto();

        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        Category savedCategory = CategoryUtil.buildSampleCategory();
        CategoryDto savedCategoryDto = CategoryUtil.buildSampleCategoryDto();

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto actual = categoryService.save(requestDto);

        assertNotNull(actual);
        assertEquals(savedCategoryDto, actual);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(savedCategory);
    }

    @Test
    void update_ShouldUpdateCategory() {
        Long categoryId = 1L;

        CreateCategoryRequestDto requestDto = CategoryUtil.buildUpdatedCategoryRequestDto();
        Category existingCategory = CategoryUtil.buildExistingCategory(categoryId);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(existingCategory));

        categoryService.update(categoryId, requestDto);

        verify(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(existingCategory);
    }

    @Test
    void deleteById_ShouldDeleteCategoryWithSameId() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
}
