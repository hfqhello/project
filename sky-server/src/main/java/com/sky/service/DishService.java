package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * */
    void  saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    /**
     * 菜品批量删除
     * */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品和关联的口味数据
     * */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品数据
     * */
    void updateWithFlavor(DishVO dishVO);

    /**
     * 根据分类id查询菜品
     * */
    List<Dish> list(Long categoryId);


    /**
     * 微信小程序 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
    /**菜品启售的状态
     * */
    void startOrStop(Integer status, Long id);
}
