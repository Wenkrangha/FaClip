package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler;

import com.wenkrang.faClip.FaClip;
import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.FaItemInterpreter;
import com.wenkrang.faClip.Module.FaItem.tagMgr;
import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

public class TagHandler implements FaItemHandler{
    @Override
    public String getNode() {
        return "tag";
    }

    @Override
    public void handle(FaItem faItem, YamlConfiguration yamlConfiguration, FaItemInterpreter faItemInterpreter) {
        try {
            // 获取标签管理器
            tagMgr tagMgr = faItem.getTagMgr();

            // 获取配置节点
            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(getNode());

            // NPE
            if (configurationSection != null) {
                // 获取所有节点
                Set<String> keys = configurationSection.getKeys(false);

                // 历遍
                for (String key : keys) {
                    // 获取节点下的值
                    String value = configurationSection.getString(key);

                    // 写入物品的Tag
                    tagMgr.set(key, value);
                }
            }
        } catch (Exception e) {
            i18nHelper.fw("FaItem.Exception.FaItemInterpreter.CannotFoundNode", getNode());
            if (FaClip.debugger != null) e.printStackTrace();
        }
    }
}
