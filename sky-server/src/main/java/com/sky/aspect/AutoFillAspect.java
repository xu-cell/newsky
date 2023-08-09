package com.sky.aspect;
import cn.hutool.core.util.ObjectUtil;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * AOP切面类：实现特定方法的传入参数的公共属性的自动填充
 * @author xujj
 */
@Slf4j
@Component
@Aspect


public class AutoFillAspect {

    @Before("execution(* com.sky.mapper.*.*(..)) && @annotation(autoFill)")
    public void autoFillProperties(JoinPoint joinpoint, AutoFill autoFill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取方法传入的实参
        Object[] objs = joinpoint.getArgs();

        if(ObjectUtil.isEmpty(objs)) {
            return;
        }
        Object obj = objs[0];
        // 获取方法

        Method setCreateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setCreateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);


        // 为传入参数赋值
        OperationType operationType = autoFill.value();
        if(operationType == OperationType.INSERT) {
            setCreateTime.invoke(obj,LocalDateTime.now());
            setCreateUser.invoke(obj,BaseContext.getCurrentId());
        }
        setUpdateTime.invoke(obj,LocalDateTime.now());
        setUpdateUser.invoke(obj,BaseContext.getCurrentId());





    }

}
