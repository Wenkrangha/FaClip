package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 窗口模块运行时异常（窗口事件调用、分页工具等）
 * <p>声明式文件解析错误请使用 {@link FaDataParseException}；
 * 键命名规范：FaWindow.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaWindowException extends FaException{
    public FaWindowException(String message) {
        super(message);
    }

    public FaWindowException(String message, Object... args) {
        super(message, args);
    }

    public FaWindowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaWindowException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
