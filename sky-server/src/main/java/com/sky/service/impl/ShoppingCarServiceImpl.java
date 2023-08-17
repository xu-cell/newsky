package com.sky.service.impl;



import cn.hutool.core.bean.BeanUtil;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCarMapper;
import com.sky.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @author xujj
 */
@Service
@Slf4j
public class ShoppingCarServiceImpl implements ShoppingCarService {
    @Autowired
    ShoppingCarMapper shoppingCarMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    SetmealMapper setmealMapper;
    /**
     * 添加套餐获取菜品到购物车
     * @param shoppingCartDTO
     */
    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        // 判断 菜品、套餐是否被添加：根据传参，在购物车表中查询是否存在

        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCarMapper.list(shoppingCart);

        if(list != null && list.size() > 0) {
            log.info("添加已经存在的菜品");
            shoppingCart = list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCarMapper.update(shoppingCart);

        } else {
            log.info("添加一个新菜品或则套餐");
            if(shoppingCart.getDishId() != null) {
                // 添加一个菜品
                Dish dish = dishMapper.getInfoById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                // 添加一个套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());

                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());

            }

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCarMapper.insert(shoppingCart);
        }


    }

    @Override
    public List<ShoppingCart> list() {
       return shoppingCarMapper.list(new ShoppingCart());
    }

    @Override
    public void delete() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCarMapper.delete(shoppingCart);
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCarMapper.list(shoppingCart);

        if(list != null && list.size() > 0) {
            shoppingCart = list.get(0);
            int size =  shoppingCart.getNumber();
            size--;
            if(size == 0) {
                shoppingCarMapper.deleteById(shoppingCart.getId());
            } else {
                shoppingCart.setNumber(size);
                shoppingCarMapper.update(shoppingCart);
            }
        } else {
            log.warn("删除的菜品/套餐存在");
        }
    }
}
