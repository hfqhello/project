package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl  implements AdminService {

    @Autowired
    AdminMapper  adminMapper;


    @Override
    public Admin login(String name, String password) {
        AdminExample adminExample=new AdminExample();
        //如添加用户名a_name条件
        adminExample.createCriteria().andANameEqualTo(name);
        //默认用户名不会重名
        System.out.println("sssssssssssssssssssssssssss  list");
        List<Admin> list= adminMapper.selectByExample(adminExample);
        System.out.println("sssssssssssssssssssssssssss  555555555");
        if(list!=null&&list.size()>0){
            Admin admin=list.get(0);
            //查询到的用户进行密码匹配，密码为MD5加密

            /**
             *  分析：
             *  admin.getApass==>c984aed14aec7623a54f591da7a85fd4b762d
             *  pwd===>000000
             *  在进行密码对比时，要将传入的pwd进行md5加密，再与数据库中查到的对象的密码进行对！
             * */
            String pwd= MD5Util.getMD5(password);

            if(pwd.equals(admin.getaPass())){

                return  admin;
            }

        }

        return null;
    }

}
