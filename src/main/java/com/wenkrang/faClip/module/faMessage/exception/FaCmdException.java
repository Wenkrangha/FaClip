package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 命令模块异常
 * <p>键命名规范：FaCommand.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaCmdException extends FaException{
    public FaCmdException(String message) {
        super(message);
    }

    public FaCmdException(String message, Object... args) {
        super(message, args);
    }

    public FaCmdException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaCmdException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
