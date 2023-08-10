package com.bjpowernode.controller;


import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    private  static int PAGE_SIZE=5;
    private  String  saveFileName="";

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
         saveFileName= FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
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


    //将新增的数据插入数据库
    @RequestMapping("/save")
    public  String save(ProductInfo info,HttpServletRequest request){
        info.setpDate(new Date());
        info.setpImage(saveFileName);
        int num= -1;
        try{
            num=  productInfoService.save(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            request.setAttribute("msg","数据增加成功");
        }else {
            request.setAttribute("msg","数据增加失败");
        }
        //清空saveFileName变量中的内容，为了下次增加或修改的异步ajax的上传处理
        saveFileName="";
        return  "forward:/prod/splite.action";
    }
    @RequestMapping("/one")
    public String one(int pid, Model model){
        ProductInfo info=productInfoService.getByID(pid);
        model.addAttribute("prod",info);
        return "update";
    }
    @RequestMapping("/update")
    public  String update(ProductInfo info,HttpSession session){

        //因为ajax的异步图片上传，如果有上传过 则saveFileName里有上传上来的图片的名称，
        // 如果没有使用异步ajax上传过图片则saveFileNme=""
        if(!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }
        int num=-1;
        try {
            num= productInfoService.update(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            session.setAttribute("msg","数据更新成功");
        }else {
            session.setAttribute("msg","数据更新失败");
        }
        //处理完更新后，saveFileName里有可能有数据
        //而下一次更新时要使用这个变量做为判断的依据，就会出错，所以必须清空saveFileName.
        saveFileName="";
        return  "redirect:/prod/splite.action";
    }

    @RequestMapping("/delete")
    public  String delete(int pid,HttpServletRequest request ){
        int num=-1;
        try {
            num=productInfoService.delete(pid);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(num>0){
            request.setAttribute("msg","数据删除成功");
        }else {
            request.setAttribute("msg","数据删除失败");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public  Object deleteAjaxSplit(HttpServletRequest request){
        PageInfo info= productInfoService.SpliPage(1,PAGE_SIZE);
        request.getSession().setAttribute("info",info);
        return  request.getAttribute("msg");
    }

    @RequestMapping("/deleteBatch")
     public  String deleteBatch(String pids,HttpServletRequest request){
        //将上传的字符串截开 形成商品id的字符串
         String [] ps=pids.split(",");
         int num=-1;

         try {
             num  =productInfoService.deleteBatch(ps);
             if(num>0){
                 request.setAttribute("msg","批量删除成功");
             }else {
                 request.setAttribute("msg","批量删除失败");
             }
         }catch (Exception e){
             e.printStackTrace();
             request.setAttribute("msg","商品不可删除");
         }

        return  "forward:/prod/deleteAjaxSplit.action";
     }


}
