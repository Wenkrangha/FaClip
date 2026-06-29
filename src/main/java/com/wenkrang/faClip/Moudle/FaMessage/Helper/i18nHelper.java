package com.wenkrang.faClip.Moudle.FaMessage.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.wenkrang.faClip.Moudle.FaMessage.Fm.waring;

/**
 * 国际化帮助类，用于处理多语言文本的获取和日志记录
 * 提供静态和实例方法来获取本地化字符串，并支持格式化功能
 */
public class i18nHelper {
    //设置语言
    private final Locale locale;

    private final ResourceBundle resourceBundle;

    /**
     * 默认构造函数，使用系统默认的语言环境
     * 初始化ResourceBundle以加载language资源文件
     */
    public i18nHelper() {
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("language", locale, this.getClass().getClassLoader());
    }

    /**
     * 带参数的构造函数，允许指定特定的语言环境和资源包
     * @param locale 指定的语言环境
     * @param resourceBundle 指定的资源包
     */
    @SuppressWarnings("unused")
    public i18nHelper(Locale locale, ResourceBundle resourceBundle) {
        this.locale = locale;
        this.resourceBundle = resourceBundle;
    }

    /**
     * 获取国际化文本（私有实例方法）
     * @param Message 资源键名
     * @return 对应的本地化字符串
     */
    //获取i18n文本
    //pt = private text
    public @NotNull String pt(@NotNull String Message) {
        return resourceBundle.getString(Message);
    }

    /**
     * 获取并格式化国际化文本（私有实例方法）
     * @param Message 资源键名
     * @param args 格式化参数
     * @return 格式化后的本地化字符串
     */
    public @NotNull String pft(@NotNull String Message, Object... args) {
        return String.format(t(Message), args);
    }

    /**
     * 获取并格式化国际化文本（公共静态方法）
     * @param Message 资源键名
     * @param args 格式化参数
     * @return 格式化后的本地化字符串
     */
    public static String ft(@NotNull String Message, Object... args) {
        return new i18nHelper().pft(Message, args);
    }

    /**
     * 获取国际化文本（公共静态方法）
     * @param Message 资源键名
     * @return 对应的本地化字符串
     */
    //获取i18n文本
    public static String t(@NotNull String Message) {
        return new i18nHelper().pt(Message);
    }
    
    /**
     * 记录警告日志（私有实例方法）
     * @param Message 资源键名
     */
    public void pw(@NotNull String Message) {waring(pt(Message));}
    
    /**
     * 记录警告日志（公共静态方法）
     * @param Message 资源键名
     */
    public static void w(@NotNull String Message) {waring(new i18nHelper().pt(Message));}

    /**
     * 记录格式化警告日志（私有实例方法）
     * @param Message 资源键名
     * @param args 格式化参数
     */
    public void pfw(@NotNull String Message, Object... args) {waring(pft(Message, args));}
    
    /**
     * 记录格式化警告日志（公共静态方法）
     * @param Message 资源键名
     * @param args 格式化参数
     */
    public static void fw(@NotNull String Message, Object... args) {waring(new i18nHelper().pft(Message, args));}
}
