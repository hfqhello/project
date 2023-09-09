package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl  implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     * */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品插入一条数据
        dishMapper.insert(dish);

        //获取菜品的主键
        Long dishId=dish.getId();

        //插入味道表
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if(flavors!=null&& flavors.size()>0){
            //向口味表dish_flavor插入n条数据
            flavors.forEach(flavor->{
                flavor.setDishId(dishId);
            });
        }
        //批量插入
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * 菜品分页查询
     * */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }



    /**
     * 菜品批量删除
     * */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
            //查询是否在售卖
        ids.forEach(id->{
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
            //查询是否在在套餐内
        List<Long> setmealIds=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&& setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
            //删除数据
        ids.forEach(id->{
            dishMapper.deleteById(id);
            //删除口味表数据
            dishFlavorMapper.deleteByDishId(id);
        });

    }
    /**
     * 根据id查询菜品和关联的口味数据
     * */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish dish=dishMapper.getById(id);
        //根据id查找菜品口味数据
       List<DishFlavor> dishFlavors= dishFlavorMapper.getByDishId(id);
       //封装DishVO
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品数据
     * */
    @Override
    @Transactional
    public void updateWithFlavor(DishVO dishVO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishVO,dish);
        //修改dish 菜品表
        dishMapper.update(dish);
        //删除菜品口味表
        dishFlavorMapper.deleteByDishId(dishVO.getId());
        //插入菜品修改的口味数据
        List<DishFlavor> flavors = dishVO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishVO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品
     * */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish=Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);

    }


}
