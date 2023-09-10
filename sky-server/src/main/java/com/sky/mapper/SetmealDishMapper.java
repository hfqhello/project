package com.sky.mapper;

import com.sky.annation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    /**
     * 删除套餐菜品关系表中的数据
     * */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询相关联的菜品
     * */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long id);
}
