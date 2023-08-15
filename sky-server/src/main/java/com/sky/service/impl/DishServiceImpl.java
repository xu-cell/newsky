package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl  implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    SetmealMapper setmealMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */

    @AutoFill(OperationType.INSERT)
    @Transactional
    @Override
    public void insertDish(DishDTO dishDTO) {
        // 拷贝DTO属性 购进一个菜品
        Dish dish = BeanUtil.copyProperties(dishDTO,Dish.class);
        dish.setStatus(StatusConstant.DISABLE);
        // 插入到数据库中
        dishMapper.insert(dish);

        // 获取口味信息
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();

        // 向口味中添加 菜品信息
        dishFlavors.forEach(item-> item.setDishId(dish.getId()));


        // 批量添加口味信息

        dishFlavorMapper.insertBatch(dishFlavors);

    }

    /**
     * 菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult list(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        List<DishVO> dishList = dishMapper.list(dishPageQueryDTO);

        Page<DishVO> page = (Page<DishVO>)dishList;

        return new PageResult(page.getTotal(),page.getResult());
    }
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        // 3.3 【删除】  在售状态下，不可删除菜品;   被套餐关联的菜品不能删除
        // 根据 ids查询 获取 dish中 停售的菜品的数量 如果大于0 抛出异常
        Long count = dishMapper.selectById(ids);
        if(count  > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 根据 菜品id 在菜品套餐中间表中 查找套餐，如果查询出来的套餐id集合不为空 就抛出异常
        List<Long> countList = setmealDishMapper.selectById(ids);
        if(!CollectionUtils.isEmpty(countList)) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        // 删除菜品
        dishMapper.deleteDishById(ids);

        // 删除菜品的口味
        dishFlavorMapper.deleteDishFlavorById(ids);

    }
    @AutoFill(OperationType.UPDATE)
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        // 通过dishDTO获取Dish
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);

        // 拿到id 删除口味表信息
        dishFlavorMapper.deleteDishFlavorById(Collections.singletonList(dish.getId()));

        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dish.getId());
        }
        if(!CollectionUtils.isEmpty(flavors)) {
            // 添加新的口味表信息
            dishFlavorMapper.insertBatch(flavors);
        }

        // 更新Dish表信息
        dishMapper.updateDish(dish);

    }

    @Override
    public DishVO selectById(Long id) {
        // 通过id 查询 dish
        Dish dish = dishMapper.getInfoById(id);
        // 通过id 查询 flavor
        List<DishFlavor> dishFlavorList = dishFlavorMapper.getinfo(id);

        // 封装 DtoVO

        DishVO dishVO = BeanUtil.copyProperties(dish,DishVO.class);

        dishVO.setFlavors(dishFlavorList);
        return dishVO;
    }

    @Override
    public void updateStatus(DishDTO dishDTO) {
        // 通过dishDTO获取Dish
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);
        dishMapper.updateDish(dish);

        if(dish.getStatus().equals(StatusConstant.DISABLE)) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            // 根据 id 在中间表中找到套餐的id
            List<Long> longsId = Collections.singletonList(dish.getId());

            List<Long> setmealId = setmealDishMapper.selectById(longsId);

            if( setmealId != null && !CollectionUtils.isEmpty(setmealId)) {
                for (Long item : setmealId) {
                    Setmeal setmeal = Setmeal.builder().id(item).status(StatusConstant.DISABLE).build();
                    setmealMapper.update(setmeal);
                }
            }


        }

    }

    @Override
    public List<Dish> getListByCategoryId(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.listWithFlavor(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getinfo(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
