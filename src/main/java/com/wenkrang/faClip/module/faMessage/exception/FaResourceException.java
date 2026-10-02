package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 资源包模块异常
 * <p>键命名规范：FaResource.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaResourceException extends FaException{
    public FaResourceException(String message) {
        super(message);
    }

    public FaResourceException(String message, Object... args) {
        super(message, args);
    }

    public FaResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaResourceException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
