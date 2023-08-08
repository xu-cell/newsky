package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xujj
 */
@Mapper
public interface CategoryMapper {

    List<Category> getCategories(String name, Integer type);
}
