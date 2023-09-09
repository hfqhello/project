package com.sky.mapper;

import com.sky.annation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    /**
     * 插入新建套餐数据
     * */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     *  保存套餐和菜品的关联关系
    * */
    void insertBatch(List<SetmealDish> setmealDishes);
}
