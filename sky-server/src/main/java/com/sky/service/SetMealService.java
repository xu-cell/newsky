package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
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

    List<Setmeal> userSelectById(Long categoryId);

    List<DishItemVO> selectBySetMealId(Long id);
}
