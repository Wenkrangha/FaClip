package com.wenkrang.faClip.module.faMessage.exception;

import com.wenkrang.faClip.module.faData.FaData;

/**
 * 声明式文件解析异常（.item / .inv / .re 等配置文件的字段缺失或非法）
 * <p>FaItem、FaWindow、FaRecipe 的解析 Handler 共用此异常，
 * 通过 i18nKey 指定各模块自己的错误消息键</p>
 */
public class FaDataParseException extends FaException{
    /**
     * 通用构造：使用 FaData.Exception.FaYamlData.NotFound 键
     * @param faData 出错的声明式文件
     * @param key 缺失的节点名
     */
    public FaDataParseException(FaData faData, String key) {
        super("FaData.Exception.FaYamlData.NotFound", key, faData.getFile().getPath());
    }

    /**
     * 自定义 i18n 键构造，消息格式统一为两个占位符：%s(节点名) %s(文件路径)
     * @param faData 出错的声明式文件
     * @param node 出错/缺失的节点名（或非法值）
     * @param i18nKey 各模块的错误消息键
     */
    public FaDataParseException(FaData faData, String node, String i18nKey) {
        super(i18nKey, node, faData.getFile().getPath());
    }
}
