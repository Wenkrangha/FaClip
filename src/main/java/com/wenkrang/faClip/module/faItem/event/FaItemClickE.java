package com.wenkrang.faClip.module.faItem.event;

import com.wenkrang.faClip.module.faInterface.FaIntf;
import com.wenkrang.faClip.module.faInterface.FaIntfContext;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faItem.TagMgr;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faMessage.helper.I18nHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FaItemClickE implements Listener {
    private final Plugin plugin;
    private final FaItemInstance faItemInstance;

    public FaItemClickE(Plugin p, FaItemInstance faItemInstance) {
        plugin = p;
        this.faItemInstance = faItemInstance;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) throws InvocationTargetException, IllegalAccessException {
        ItemStack item = event.getItem();

        if (item != null && event.getHand() == EquipmentSlot.HAND && item.getItemMeta() != null) {
            TagMgr tagMgr = new TagMgr(plugin, item);

            if (tagMgr.has("event.item_click")) {
                String node = tagMgr.get("event.item_click");

                List<FaIntf> intf = faItemInstance.getFaInterfaceInstance().getIntf(node);

                if (!intf.isEmpty()) {
                    if (intf.size() == 1) {
                        FaIntfContext faIntfContext = new FaIntfContext();
                        faIntfContext.set("event", event);
                        faIntfContext.set("item", item);

                        intf.getFirst().invoke(this, faIntfContext, new String[0]);
                    }else {
                        Fm.error(I18nHelper.t("FaItem.Exception.FaItemInterpreter.EventConflict")
                                + ": " + node);
                    }
                }else {
                    Fm.warning(I18nHelper.ft("FaInterface.Exception.Instance.IntfNotFound", node));
                }
            }
        }
    }
}
