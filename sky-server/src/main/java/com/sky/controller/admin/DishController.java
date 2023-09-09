package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result <String> save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return  Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public  Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        log.info("菜品数据",pageResult);
        return Result.success(pageResult);
    }

    @ApiOperation("菜品批量删除")
    @DeleteMapping
    public  Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除");
        dishService.deleteBatch(ids);
        return  Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和关联的口味数据")
    public  Result<DishVO> getById(@PathVariable Long id){

        return  Result.success(dishService.getByIdWithFlavor(id));
    }


}
