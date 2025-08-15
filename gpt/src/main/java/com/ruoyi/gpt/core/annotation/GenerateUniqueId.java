package com.ruoyi.gpt.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GenerateUniqueId {

    /**
     * 目标对象
     * @return String
     */
    String target() default "";

    /**
     * 目标对象的id字段
     * @return String
     */
    String filed() default "id";

    /**
     * 目标对象的参数索引
     * @return
     */
    int index() default 0;

}
