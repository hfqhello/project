package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
        @Autowired
       ProductInfoMapper  productInfoMapper;

        //获取所有数据
    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());

    }
        //获取分页的数据  利用插件完成
    public PageInfo  SpliPage(int pageNum,int pageSize){
         //分页插件完成分页设置
        PageHelper.startPage(pageNum,pageSize);
        //进行PageInfo的数据封装
        //进行有条件的查询操作，必须创建 ProductInfoExample 对象
        ProductInfoExample example=new ProductInfoExample();
        //查询出来的数据倒序排列
        example.setOrderByClause("p_id desc");
        //设置排序完后 取集合数据
        List<ProductInfo> list= productInfoMapper.selectByExample(example);
        //将查询到的数据封装到PageInfo 中
        PageInfo <ProductInfo> pageInfo =new PageInfo<>(list);
        return pageInfo ;
    }
    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }
    @Override
    public ProductInfo getByID(int pid) {
        return  productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {

        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String [] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

}