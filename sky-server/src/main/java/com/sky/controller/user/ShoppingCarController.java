package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujj
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCarController {
    @Autowired
    ShoppingCarService shoppingCarService;


    @PostMapping("/add")
    public Result addToCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        shoppingCarService.insert(shoppingCartDTO);

        return Result.success();
    }


    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {

        List<ShoppingCart>list = shoppingCarService.list();

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result delete() {

        shoppingCarService.delete();
        return Result.success();
    }
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCarService.sub(shoppingCartDTO);
        return Result.success();
    }
}
