package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler;

import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;

public class NameHandler implements FaItemHandler{
    @Override
    public String getNode() {
        return "name";
    }

    @Override
    public void handle(FaItem faItem, YamlConfiguration yamlConfiguration) {
        ItemMeta itemMeta = faItem.getItemMeta();

        String name = yamlConfiguration.getString(getNode());

        if (name != null) {
            itemMeta.setDisplayName(name);

            faItem.setItemMeta(itemMeta);
        }else {
            i18nHelper.fw("FaItem.Exception.FaItemInterpreter.CannotFoundNode", getNode());
        }
    }
}
