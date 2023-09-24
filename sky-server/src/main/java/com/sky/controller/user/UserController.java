package com.sky.controller.user;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "C端-用户接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录，授权码为：{}", userLoginDTO.getCode());
        User user=userService.wxLogin(userLoginDTO);

        Map claims=new HashMap();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String toke= JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO userLoginDTO1=UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(toke)
                .build();
        return  Result.success(userLoginDTO1);
    }



}



