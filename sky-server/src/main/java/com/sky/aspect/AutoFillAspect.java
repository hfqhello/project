package com.sky.aspect;


import com.sky.annation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面同一公共字段
 * */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {
    /**
     * 切入点
     * */
    @Pointcut("execution(* com.sky.mapper.*.*(..))&& @annotation(com.sky.annation.AutoFill)")
    public  void  autoFillPointCut(){
    }
    /**
     * 通知
     * */
    @Before("autoFillPointCut()")
    public  void  autoFill(JoinPoint joinPoint){
        log.info("公共字段自动填充");
        //获得方法签名
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        //获得方法上的注解
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);
        //获得注解中的操作方法
        OperationType operationType=autoFill.value();
        //获取当前目标方法参数
        Object [] args=joinPoint.getArgs();
        if(args==null|| args.length==0){
            return;
        }
        //实体对象
        Object entity=args[0];
        //准备赋值的数据
        LocalDateTime time=LocalDateTime.now();
        Long empId= BaseContext.getCurrentId();
        if(operationType==OperationType.INSERT){
            //当前执行insert方法
            try {
                Method setCreateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method SET_CREATE_USER =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method SET_UPDATE_USER =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //通过反射调用目标方法
                setCreateTime.invoke(entity,time);
                setUpdateTime.invoke(entity,time);
                SET_CREATE_USER.invoke(entity,empId);
                SET_UPDATE_USER.invoke(entity,empId);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("自动填充失败");
            }

        }else{
            //当前执行更新方法
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method SET_UPDATE_USER =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                setUpdateTime.invoke(entity,time);
                SET_UPDATE_USER.invoke(entity,empId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
