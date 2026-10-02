package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 物品模块异常（物品标签、物品数据等）
 * <p>键命名规范：FaItem.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaItemException extends FaException{
    public FaItemException(String message) {
        super(message);
    }

    public FaItemException(String message, Object... args) {
        super(message, args);
    }

    public FaItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaItemException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
