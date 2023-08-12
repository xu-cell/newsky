package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @author xujj
 */
public interface SetMealService {
    PageResult list(SetmealPageQueryDTO setmealPageQueryDTO);

    void insertSetMeal(SetmealDTO setmealDTO);

    void deleteSetMeal(List<Long> ids);

    void putSetMeal(SetmealDTO setmealDTO);

    void update(SetmealDTO setmealDto);

    SetmealVO selectById(Long id);
}
