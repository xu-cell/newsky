package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
