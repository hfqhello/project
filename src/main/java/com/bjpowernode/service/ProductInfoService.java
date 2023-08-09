package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {

    List<ProductInfo>  getAll();

    PageInfo SpliPage(int pageNum, int pageSize);

}
