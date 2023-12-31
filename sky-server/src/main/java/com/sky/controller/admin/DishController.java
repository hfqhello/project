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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result <String> save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);

        //数据库数据修改了  要清除redis数据
        Long  categoryId=dishDTO.getCategoryId();
        String  key="dish_"+categoryId;
        cleanCache(key);
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

        cleanCache("dish_*");
        return  Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和关联的口味数据")
    public  Result<DishVO> getById(@PathVariable Long id){
        return  Result.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * 修改菜品
     * */
    @PutMapping
    @ApiOperation("修改菜品")
    public  Result   update(@RequestBody DishVO dishVO){
        log.info("修改菜品：{}", dishVO);
        dishService.updateWithFlavor(dishVO);
        cleanCache("dish_*");
        return  Result.success();
    }

    /**
     * 修改菜品启售状态
     * */
    /**
     * 套餐停起售
     * */
    @PostMapping("/status/{status}")
    @ApiOperation("餐停起售")
    public  Result startOrStop(@PathVariable Integer status ,Long id){
        dishService.startOrStop(status,id);
        cleanCache("dish_*");
        return  Result.success();
    }


    /**
     * 根据分类id查询菜品
     * */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public  Result<List<Dish>> list(Long categoryId){
        List<Dish> list=dishService.list(categoryId);
        return  Result.success(list);
    }


    /**
     * 清理缓存数据
     * */
   private  void  cleanCache(String pattern){
       Set keys=redisTemplate.keys(pattern);
       redisTemplate.delete(keys);
   }


}
