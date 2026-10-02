package com.wenkrang.faClip.module.faMessage.exception;

/**
 * 数据模块异常（数据读写、保存、插件实例缺失等）
 * <p>键命名规范：FaData.Error.{场景}.{具体错误} 或沿用
 * FaData.Exception.FaYamlData.* 存量键，
 * 直接传入 i18n 键，由基类 {@link FaException} 统一翻译</p>
 */
public class FaDataException extends FaException{
    public FaDataException(String message) {
        super(message);
    }

    public FaDataException(String message, Object... args) {
        super(message, args);
    }

    public FaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaDataException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
