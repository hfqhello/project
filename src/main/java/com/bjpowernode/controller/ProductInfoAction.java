package com.bjpowernode.controller;


import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    private  static int PAGE_SIZE=5;

    @Autowired
    ProductInfoService productInfoService;
    @RequestMapping("/getAll")
    public  String getAll(HttpServletRequest request){
        List<ProductInfo> list= productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    //分页开发
    @RequestMapping("/splite")
    public  String  splite(HttpServletRequest request){
        //得到第一页数据
        PageInfo pageInfo=productInfoService.SpliPage(1,PAGE_SIZE);
        request.setAttribute("info",pageInfo);
        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody
    @RequestMapping("/ajaxsplit")
    public  void  ajaxsplit(int page,HttpSession session){
        System.out.println(".....................ajaxsplit");
        PageInfo pageInfo= productInfoService.SpliPage(page,PAGE_SIZE);
        session.setAttribute("info",pageInfo);
    }



    //异步ajax文件上传
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public  Object  ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //提取生成文件uuid+上传图片的后缀名
        String saveFileName= FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目图片存储路径
        String path=request.getServletContext().getRealPath("/image_big");

        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将图片已json数据返回给页面 jackson
        JSONObject object = new JSONObject();
        object.put("imgurl",saveFileName);
        return object.toString();
    }





}
