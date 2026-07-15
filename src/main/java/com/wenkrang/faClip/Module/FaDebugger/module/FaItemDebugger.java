package com.wenkrang.faClip.Module.FaDebugger.module;

import com.wenkrang.faClip.FaClip;
import com.wenkrang.faClip.Module.FaCommand.Annotation.Cmd;
import com.wenkrang.faClip.Module.FaCommand.Annotation.Debug;
import com.wenkrang.faClip.Module.FaCommand.Annotation.RequireOP;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.FaItemInterpreter;
import com.wenkrang.faClip.Module.FaItem.tagMgr;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;

public class FaItemDebugger {

    /**
     * 从 resources 加载 test.item，解析后给予玩家
     */
    @Cmd("fatest.item.load")
    @RequireOP
    @Debug
    public static void loadItem(FaCmdContext context) {
        Player player = (Player) context.sender();

        // 从 resources 加载 test.item
        InputStream is = FaClip.plugin.getResource("FaDebugger/FaItem/test.item");
        if (is == null) {
            System.out.println("[FaItemDebugger] 找不到 FaDebugger/FaItem/test.item");
            return;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(is));

        // 用 FaItemInterpreter 解析
        FaItemInterpreter interpreter = new FaItemInterpreter(FaClip.plugin);
        FaItem faItem = interpreter.interpreter(yaml);

        // 打印解析结果
        System.out.println("[FaItemDebugger] id: " + faItem.getNamespacedKey());
        System.out.println("[FaItemDebugger] type: " + faItem.getType());
        System.out.println("[FaItemDebugger] lore: " + faItem.getItemMeta().getLore());

        // 读取 tag
        tagMgr tagMgr = faItem.getTagMgr();
        System.out.println("[FaItemDebugger] tag.a: " + tagMgr.get("a"));
        System.out.println("[FaItemDebugger] tag.b: " + tagMgr.get("b"));
        System.out.println("[FaItemDebugger] tag.c: " + tagMgr.get("c"));

        // 给玩家物品
        player.getInventory().addItem(faItem);
        System.out.println("[FaItemDebugger] 物品已给予玩家");
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
