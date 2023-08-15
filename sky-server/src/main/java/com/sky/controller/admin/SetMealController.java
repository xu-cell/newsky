package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujj
 */
@Slf4j
@RestController
@Api(tags = "套餐管理接口")
@RequestMapping("/admin/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;


    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageResult pageResult = setMealService.list(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    @ApiOperation("新增套餐")
    @PostMapping
    public Result addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.insertSetMeal(setmealDTO);
        return Result.success();
    }


    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("删除套餐")
    @DeleteMapping
    public Result deleteSetMeal(@RequestParam List<Long> ids) {
        setMealService.deleteSetMeal(ids);
        return Result.success();
    }

    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("修改套餐")
    @PutMapping
    public Result putSetMeal(@RequestBody SetmealDTO setmealDto) {
        setMealService.putSetMeal(setmealDto);
        return Result.success();
    }
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("套餐起售和禁售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status, Long id) {

        SetmealDTO setmealDto = new SetmealDTO();
        setmealDto.setId(id);
        setmealDto.setStatus(status);

        setMealService.update(setmealDto);

        return Result.success();

    }


    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setMealService.selectById(id);
        return Result.success(setmealVO);
    }
}
