package com.sky.mapper;

import com.sky.dto.SetmealDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> selectById(List<Long> ids);

    void update(SetmealDTO setmealDTO);

}
