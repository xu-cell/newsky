package com.sky.controller.user;


import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xujj
 */
@RestController
@Api(tags = "小程序分类管理接口")
@RequestMapping("/user/category")
@Slf4j
public class UserCategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> getList(Integer type) {
        List<Category> lists = categoryService.userGetByType(type);
        return Result.success(lists);
    }

}
