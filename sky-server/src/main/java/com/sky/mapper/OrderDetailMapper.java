package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 向明细表插入数据
     * */
    void insertBatch(List<OrderDetail> orderDetails);
}