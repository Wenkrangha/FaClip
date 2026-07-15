package com.wenkrang.faClip.Module.FaDebugger.module;

import com.wenkrang.faClip.FaClip;
import com.wenkrang.faClip.Helper.ResourceHelper;
import com.wenkrang.faClip.Module.FaCommand.Annotation.Cmd;
import com.wenkrang.faClip.Module.FaCommand.Annotation.Debug;
import com.wenkrang.faClip.Module.FaCommand.Annotation.RequireOP;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaItem.FaItemInstance;
import com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.FaItemInterpreter;
import com.wenkrang.faClip.Module.FaItem.tagMgr;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class FaItemDebugger {

    /**
     * 从 resources 加载 test.item，解析后给予玩家
     */
    @Cmd("fatest.item.load")
    @RequireOP
    @Debug
    public static void loadItem(FaCmdContext context) {
        Player player = (Player) context.sender();

        FaItemInstance faItemInstance = new FaItemInstance(FaClip.plugin);
        faItemInstance.loadAll();
    }

    /**
     * 读取主手物品的 tag 信息
     */
    @Cmd("fatest.item.readtag")
    @RequireOP
    @Debug
    public static void readTag(FaCmdContext context) {
        Player player = (Player) context.sender();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (itemInMainHand.getType().isAir()) {
            System.out.println("[FaItemDebugger] 主手没有物品");
            return;
        }

        FaItem faItem = new FaItem(FaClip.plugin, itemInMainHand);
        tagMgr tagMgr = faItem.getTagMgr();

        System.out.println("[FaItemDebugger] type: " + faItem.getType());
        System.out.println("[FaItemDebugger] lore: " + faItem.getItemMeta().getLore());
        System.out.println("[FaItemDebugger] tag.a: " + tagMgr.get("a"));
        System.out.println("[FaItemDebugger] tag.b: " + tagMgr.get("b"));
        System.out.println("[FaItemDebugger] tag.c: " + tagMgr.get("c"));
    }
}
