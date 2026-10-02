package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 命令模块异常
 * <p>键命名规范：FaIoC.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaIoCException extends FaException{
    public FaIoCException(String message) {
        super(message);
    }

    public FaIoCException(String message, Object... args) {
        super(message, args);
    }

    public FaIoCException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaIoCException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
