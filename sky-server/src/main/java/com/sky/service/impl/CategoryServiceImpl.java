package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujj
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据条件进行查询
     */
    @Override
    public PageResult list(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        List<Category> categories = categoryMapper.getCategories(categoryPageQueryDTO.getName(), categoryPageQueryDTO.getType());

        Page<Category> pages = (Page<Category>)categories;

        return new PageResult(pages.getTotal(),pages.getResult());

    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void insert(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                        .name(categoryDTO.getName())
                        .sort(categoryDTO.getSort())
                        .type(categoryDTO.getType())
                        .status(0).build();
        categoryMapper.insert(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.delete(id);
    }

    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.getCategories(null,type);
    }
}
