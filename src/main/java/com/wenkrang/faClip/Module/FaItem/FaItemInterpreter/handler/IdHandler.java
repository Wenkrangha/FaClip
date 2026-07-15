package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler;

import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;

public class IdHandler implements FaItemHandler{
    @Override
    public String getNode() {
        return "id";
    }

    @Override
    public void handle(FaItem faItem, YamlConfiguration yamlConfiguration) {
        String id = yamlConfiguration.getString(getNode());

        if (id != null) {
            faItem.setNamespacedKey(new NamespacedKey(faItem.plugin, id));
        }else {
            i18nHelper.fw("FaItem.Exception.FaItemInterpreter.CannotFoundNode", getNode());
        }
    }
}
