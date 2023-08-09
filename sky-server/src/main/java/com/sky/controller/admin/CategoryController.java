package com.sky.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujj
 */
@RestController
@Api(tags = "分类管理接口")
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询接口
     */

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageResult pageResult = categoryService.list(categoryPageQueryDTO);

        return Result.success(pageResult);
    }

    @ApiOperation("启用/禁用分类")
    @PostMapping("/status/{status}")
    public Result enableAndDisable(@PathVariable("status") Integer status,  Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);

        categoryService.update(category);

        return Result.success();

    }
    @ApiOperation("新增分类")
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {

        categoryService.insert(categoryDTO);
        return Result.success(categoryDTO);
    }

    @ApiOperation("删除分类")
    @DeleteMapping
    public Result removeCategory(Long id) {
        categoryService.delete(id);
        return Result.success();
    }


    @ApiOperation("修改分类")
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
         // 使用huTool工具 BeanUtil
        categoryService.update(BeanUtil.copyProperties(categoryDTO, Category.class));
        return Result.success();
    }


    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> getList(Integer type) {
        List<Category> lists = categoryService.getByType(type);

        return Result.success(lists);
    }

}
