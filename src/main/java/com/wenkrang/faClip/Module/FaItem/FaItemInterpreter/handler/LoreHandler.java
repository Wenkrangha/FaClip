package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler;

import com.wenkrang.faClip.FaClip;
import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LoreHandler implements FaItemHandler {
    @Override
    public String getNode() {
        return "lore";
    }

    @Override
    public void handle(FaItem faItem, YamlConfiguration yamlConfiguration) {
        try {
            List<String> lore = yamlConfiguration.getStringList(getNode());

            ItemMeta itemMeta = faItem.getItemMeta();
            itemMeta.setLore(lore);

            faItem.setItemMeta(itemMeta);
        } catch (Exception e) {
            i18nHelper.fw("FaItem.Exception.FaItemInterpreter.CannotFoundNode", getNode());
            if (FaClip.debugger != null) e.printStackTrace();
        }
    }
}
