package com.kkaka.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2019/1/22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Account {
    String accountName();

    String password();

    String alias();

    boolean isDefault() default false;
}
