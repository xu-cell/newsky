package com.sky.mapper;

import com.sky.dto.SetmealDTO;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> selectById(List<Long> ids);

    void update(SetmealDTO setmealDTO);

    void insert(List<SetmealDish> setmealDishes);

    void delete(List<Long> ids);

    List<Long> selectDishId(Long setmealId);

    List<SetmealDish> selectDetailById(Long id);
}
