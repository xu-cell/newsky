package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    public PageResult list(CategoryPageQueryDTO categoryPageQueryDTO);
}
