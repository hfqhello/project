package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询删除的菜品是否在售卖
     * */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);
    /**
     * 删除菜品
     * */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);
    /**
    * 根据主键修改菜品信息
    *  @param dish
    * */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}