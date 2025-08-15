package com.ruoyi.common.i18n;

public class LanguageContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getUserLanguage() {
        return contextHolder.get();
    }

    public static void setUserLanguage(String language) {
        contextHolder.set(language);
    }

    public static void clearUserLanguage() {
        contextHolder.remove();
    }
}
