package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xujj
 */
@Mapper
public interface CategoryMapper {

    List<Category> getCategories(String name, Integer type);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    void delete(Long id);
}
