package com.sky.controller.user;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujj
 */
@Slf4j
@RestController
@Api(tags = "套餐管理接口")
@RequestMapping("/user/setmeal")
public class UserSetMealController {

    @Autowired
    private SetMealService setMealService;

    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    @ApiOperation("套餐查询")
    @GetMapping("/list")
    public Result<List<Setmeal>> page(Long categoryId) {

        List<Setmeal> list = setMealService.userSelectById(categoryId);

        return Result.success(list);
    }




    @GetMapping("/dish/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<List<DishItemVO>> getById(@PathVariable Long id) {
        List<DishItemVO> dishItemVO = setMealService.selectBySetMealId(id);
        return Result.success(dishItemVO);
    }
}
