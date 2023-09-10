package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.saveWithDish(setmealDTO);
        return  Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public  Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){

       PageResult pageResult=setmealService.pageQuery(setmealPageQueryDTO);

       return  Result.success(pageResult);

    }

    /**
     * 批量删除套餐
     * */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public  Result delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return  Result.success();
    }

    /**
     * 根据id查询套餐
     * */
    @GetMapping("/{id}")
    public  Result <SetmealVO>  getById(@PathVariable Long id){
        SetmealVO setmealVO= setmealService.getByIdWithDish(id);
        return  Result.success(setmealVO);
    }




}
