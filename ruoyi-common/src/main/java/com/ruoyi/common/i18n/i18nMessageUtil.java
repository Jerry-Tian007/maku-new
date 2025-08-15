package com.ruoyi.common.i18n;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class i18nMessageUtil {
    /**
     * 根据key信息获取对应语言的内容
     *
     * @param key      消息key值
     * @param language 语言_国家
     * @return
     */
    public static String get(String key, String language) {
        if (!StringUtils.isEmpty(language)) {
            String[] arrs = language.split("_");
            if (arrs.length == 2) {
                return get(key, new Locale(arrs[0], arrs[1]));
            }
        }
        //使用默认的国际化语言
        return get(key, Locale.getDefault());
    }

    /**
     * 根据key信息获取对应语言的内容
     *
     * @param key      消息key值
     * @param params   需要替换到占位符中的参数 占位符下标重0开始  格式如: {0} {1}
     * @param language 语言_国家
     * @return
     */
    public static String get(String key, Object[] params, String language) {
        if (!StringUtils.isEmpty(language)) {
            String arrs[] = language.split("_");
            if (arrs.length == 2) {
                return get(key, params, new Locale(arrs[0], arrs[1]));
            }
        }
        return get(key, params, Locale.getDefault());
    }

    private static String get(String key, Locale language) {
        return get(key, new String[0], language);
    }

    private static String get(String key, Object[] params, Locale language) {
        return getInstance().getMessage(key, params, language);
    }

    private static MessageSource getInstance() {
        return Lazy.messageSource;
    }

    /**
     * 使用懒加载方式实例化messageSource国际化工具
     */
    private static class Lazy {
        private static final MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
    }

}
