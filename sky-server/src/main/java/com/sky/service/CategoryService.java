package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    public PageResult list(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(Category category);

    void insert(CategoryDTO categoryDTO);

    void delete(Long id);

    List<Category> getByType(Integer type);

    List<Category> userGetByType(Integer type);
}
