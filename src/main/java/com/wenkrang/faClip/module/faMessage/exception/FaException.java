package com.wenkrang.faClip.module.faMessage.exception;

import com.wenkrang.faClip.module.faMessage.helper.I18nHelper;

/**
 * FaClip 异常体系基类
 * <p>所有业务异常都必须继承此类，错误消息统一通过 i18n 资源键获取，
 * 禁止硬编码字符串。键命名规范：{模块名}.Error.{场景}.{具体错误}</p>
 */
public class FaException extends RuntimeException{
    public FaException(String message) {
        super(I18nHelper.t(message));
    }

    public FaException(String message,Object... args) {
        super(I18nHelper.ft(message,args));
    }

    /**
     * 携带原始异常（cause 链）构造，避免丢失堆栈信息
     */
    public FaException(String message, Throwable cause) {
        super(I18nHelper.t(message), cause);
    }

    /**
     * 携带原始异常（cause 链）和格式化参数构造
     */
    public FaException(String message, Throwable cause, Object... args) {
        super(I18nHelper.ft(message, args), cause);
    }
}
