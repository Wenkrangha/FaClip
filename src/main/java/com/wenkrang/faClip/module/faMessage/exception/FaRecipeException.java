package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 配方模块运行时异常（配方事件调用等）
 * <p>声明式文件解析错误请使用 {@link FaDataParseException}；
 * 键命名规范：FaRecipe.Error.{场景}.{具体错误}，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaRecipeException extends FaException{
    public FaRecipeException(String message) {
        super(message);
    }

    public FaRecipeException(String message, Object... args) {
        super(message, args);
    }

    public FaRecipeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaRecipeException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
