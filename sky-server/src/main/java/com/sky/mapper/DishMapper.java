package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    List<DishVO> list(DishPageQueryDTO dishPageQueryDTO);

    Long selectById(List<Long> ids);

    void deleteDishById(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    Dish getInfoById(Long id);

    List<Dish> selectByCategoryId(Long categoryId);

    Long selectDisabledDishId(List<Long> count);

    List<Dish> listWithFlavor(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}


