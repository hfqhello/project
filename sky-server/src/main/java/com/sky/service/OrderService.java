package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户下单
     * */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);
    /**
     * 历史订单查询
     * */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 用户取消订单
     *
     * @return
     */
    void userCancelById(Long id) throws Exception;
    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    void repetition(Long id);

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    OrderStatisticsVO statistics();
    /**
     * 接单
     *
     * @return
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);
    /**
     * 拒单
     *
     * @return
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);
    /**
     * 取消订单
     *
     * @return
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);
    /**
     * 派送订单
     *
     * @return
     */
    void delivery(Long id);
    /**
     * 完成订单
     *
     * @return
     */
    void complete(Long id);
}
