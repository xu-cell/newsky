package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author xujj
 */
public interface ShoppingCarService {
    void insert(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void delete();

    void sub(ShoppingCartDTO shoppingCartDTO);

}

