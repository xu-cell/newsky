package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xujj
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult list(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        // 在套餐表中查询套餐信息 封装vo
        List<SetmealVO> list = setmealMapper.getList(setmealPageQueryDTO);

        // 构建PageResult,封装查询结果
        Page<SetmealVO> resultPage = (Page<SetmealVO>)list;

        return new PageResult(resultPage.getTotal(), resultPage.getResult());
    }

    @Transactional
    @Override
    public void insertSetMeal(SetmealDTO setmealDTO) {
        // 创建一个套餐，从传入参数获取套餐信息.mapper接口需要注解AUTOFILL 插入到套餐表中

        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);

        Long setMealId = setmealMapper.insert(setmeal);

        // 获取传入套餐菜品信息 并拿到套餐id进行设置
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();


        // 拿到分类id 获取菜品
        for (SetmealDish item : setmealDishes) {
            item.setSetmealId(setMealId);
        }

        // 插入套餐菜品信息到中间表
        setmealDishMapper.insert(setmealDishes);



    }

    @Transactional
    @Override
    public void deleteSetMeal(List<Long> ids) {
        // 查找状态为起售的套餐的数量
        Long count = setmealMapper.selectById(ids);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }

        // 删除套餐表
        setmealMapper.delete(ids);
        setmealDishMapper.delete(ids);
        // 根据套餐id【删除套餐菜品中间表】
    }

    @Transactional
    @Override
    public void putSetMeal(SetmealDTO setmealDTO) {
        // 创建一个套餐，从传入参数获取套餐信息.mapper接口需要注解AUTOFILL 插入到套餐表中
        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);

        // 设置更新参数,注解自动更新，更新套餐表信息

        setmealMapper.update(setmeal);

        // 拿到套餐id 在中间表中，删除套餐对应的相关菜品信息

        setmealDishMapper.delete(Collections.singletonList(setmeal.getId()));


        // 添加信息套餐菜品关联信息

        // 获取传入套餐菜品信息 并拿到套餐id进行设置
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();


        // 拿到分类id 获取菜品
        for (SetmealDish item : setmealDishes) {
            item.setSetmealId(setmeal.getId());
        }

        // 插入套餐菜品信息到中间表

        setmealDishMapper.insert(setmealDishes);

    }

    @Override
    public void update(SetmealDTO setmealDto) {
        // 起售套餐时，如果套餐内包含停售的菜品，则不能起售

        Setmeal setmeal = BeanUtil.copyProperties(setmealDto,Setmeal.class);

        if(setmeal.getStatus().equals(StatusConstant.ENABLE)) {
            // 起售套餐时，如果套餐内包含停售的菜品，则不能起售
            Long setmealId = setmeal.getId();
            // 套餐内停售的菜品的数量
            List<Long> count = setmealDishMapper.selectDishId(setmealId);

            Long disableCount = dishMapper.selectDisabledDishId(count);

            if(disableCount > 0) {
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }

        setmealMapper.update(setmeal);
    }

    @Override
    public SetmealVO selectById(Long id) {


        // 根据id查询套餐信息
        Setmeal setmeal = setmealMapper.getById(id);

        List<SetmealDish> list = setmealDishMapper.selectDetailById(id);

        SetmealVO setmealVO = BeanUtil.copyProperties(setmeal,SetmealVO.class);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    @Override
    public List<Setmeal> userSelectById(Long categoryId) {

        // 根据id查询套餐信息
        List<Setmeal> setmeal = setmealMapper.userGetById(categoryId);
        return setmeal;
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> selectBySetMealId(Long id) {

        // DishItemVO = 中间表信息 + dish表信息


        List<Long> dishListIds = setmealDishMapper.selectDishId(id);
        List<Dish> dishList = new ArrayList<>();
        for (Long dishId : dishListIds) {
            dishList.add(dishMapper.getInfoById(dishId));
        }

        List<DishItemVO> dishItemVOS = new ArrayList<DishItemVO>();
        List<SetmealDish> setmealDishs = setmealDishMapper.selectDetailById(id);

        for (Dish item : dishList) {

            DishItemVO dishItemVO = BeanUtil.copyProperties(item,DishItemVO.class);
            dishItemVOS.add(dishItemVO);
        }

        for (SetmealDish item : setmealDishs) {
            String name = item.getName();
            for (DishItemVO vo : dishItemVOS) {
                if(vo.getName().equals(name)) {
                    vo.setCopies(item.getCopies());
                }
            }
        }


        return dishItemVOS;
    }
}
