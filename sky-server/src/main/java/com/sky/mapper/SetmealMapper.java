package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    List<SetmealVO> getList(SetmealPageQueryDTO setmealPageQueryDTO);


    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long insert(Setmeal setmeal);

    void delete(List<Long> ids);

    Long selectById(List<Long> ids);


    Setmeal getById(Long id);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    List<Setmeal> userGetById(Long categoryId);
}
