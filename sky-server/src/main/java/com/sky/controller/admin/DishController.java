package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Api(tags = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    RedisTemplate redisTemplate;


    @ApiOperation("新增菜品")
    @PostMapping
    public Result  insertDish(@RequestBody DishDTO dishDTO) {
        dishService.insertDish(dishDTO);
        String result = "dish_" + dishDTO.getCategoryId();
        deleteCache(result);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {

        PageResult pageResult = dishService.list(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result deleteDish(@RequestParam List<Long> ids) {
        dishService.delete(ids);
        deleteCache("dish_*");
        return Result.success();
    }

    private void deleteCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        deleteCache("dish_*");
        return Result.success();
    }

    @ApiOperation("根据id查询数据用于回显")
    @GetMapping("/{id}")
    public Result<DishVO> list(@PathVariable Long id) {
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    @ApiOperation("菜品起售和禁售")
    @PostMapping("/status/{status}")
        public Result updateStatus(@PathVariable("status") Integer status, Long id) {
        DishDTO dishDTO= new DishDTO();
        dishDTO.setStatus(status);
        dishDTO.setId(id);
        dishService.updateStatus(dishDTO);
        deleteCache("dish_*");
        return Result.success();

    }
    @ApiOperation("根据分类id进行查询")
    @GetMapping("/list")
    public Result<List<Dish>> getListByCategoryId(Long categoryId) {

        List<Dish> list =  dishService.getListByCategoryId(categoryId);
        return Result.success(list);
    }
}
