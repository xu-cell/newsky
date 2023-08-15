package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void insertDish(DishDTO dishDTO);

    PageResult list(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    void update(DishDTO dishDTO);

    DishVO selectById(Long id);

    void updateStatus(DishDTO dishDTO);

    List<Dish> getListByCategoryId(Long categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
