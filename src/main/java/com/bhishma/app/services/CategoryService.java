package com.bhishma.app.services;

import com.bhishma.app.payloads.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);
    void deleteCategory(Integer categoryId);

     CategoryDto getCategory(Integer categoryId);

    List<CategoryDto> getCategories();
}
