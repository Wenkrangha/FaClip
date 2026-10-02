package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 接口模块异常
 * <p>键命名规范：FaInterface.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaIntfException extends FaException{
    public FaIntfException(String message) {
        super(message);
    }

    public FaIntfException(String message, Object... args) {
        super(message, args);
    }

    public FaIntfException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaIntfException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
