package mate.academy.bookshop.category;

import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.CategoryMapper;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.category.CategoryRepository;
import mate.academy.bookshop.service.impl.CategoryServiceImpl;
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
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryDto> categoryDtos = List.of(new CategoryDto(), new CategoryDto());
        Mockito.when(categoryMapper.toDto(Mockito.any(Category.class)))
                .thenAnswer(invocation -> {
                    Category category = invocation.getArgument(0);
                    int index = categories.indexOf(category);
                    return categoryDtos.get(index);
                });

        Page<CategoryDto> result = categoryService.getAll(pageable);

        Mockito.verify(categoryRepository).findAll(pageable);
        Mockito.verify(categoryMapper, Mockito.times(categories.size()))
                .toDto(Mockito.any(Category.class));

        Assertions.assertEquals(categoryDtos, result.getContent());
    }

    @Test
    void getById_WithValidId_ShouldReturnValidCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Detective");
        category.setDescription("Implies the presence of a mystery that will be solved");
        category.setDeleted(false);

        CategoryDto categoryDto = new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(categoryId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(categoryDto.getName(), actual.getName());
        Mockito.verify(categoryRepository).findById(categoryId);
        Mockito.verify(categoryMapper).toDto(category);
    }

    @Test
    void getById_WithInvalidCategoryId_ShouldReturnException() {
        Long categoryId = 10L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        String expected = "Category not found by id " + categoryId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ShouldSaveCategory() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Detective");
        requestDto.setDescription("Implies the presence of a mystery that will be solved");

        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(category.getName());
        savedCategory.setDescription(category.getDescription());

        CategoryDto savedCategoryDto = new CategoryDto();
        savedCategoryDto.setId(savedCategoryDto.getId());
        savedCategoryDto.setName(savedCategory.getName());
        savedCategoryDto.setDescription(savedCategory.getDescription());

        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto actual = categoryService.save(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedCategoryDto, actual);
        Mockito.verify(categoryMapper).toEntity(requestDto);
        Mockito.verify(categoryRepository).save(category);
        Mockito.verify(categoryMapper).toDto(savedCategory);
    }

    @Test
    void update_ShouldUpdateCategory() {
        Long categoryId = 1L;

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Updated category name");
        requestDto.setDescription("Updated category description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("New category name");
        existingCategory.setDescription("New category description");

        Mockito.when(categoryRepository
                .findById(categoryId)).thenReturn(Optional.of(existingCategory));

        categoryService.update(categoryId, requestDto);

        Mockito.verify(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
        Mockito.verify(categoryRepository).save(existingCategory);
        Mockito.verify(categoryMapper).toDto(existingCategory);
    }

    @Test
    void deleteById_ShouldDeleteCategoryWithSameId() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        Mockito.verify(categoryRepository).deleteById(categoryId);
    }
}
