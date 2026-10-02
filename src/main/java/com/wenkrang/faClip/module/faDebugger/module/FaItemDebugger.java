package com.wenkrang.faClip.module.faDebugger.module;

import com.wenkrang.faClip.module.FaClip;
import com.wenkrang.faClip.module.faCommand.annotation.*;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faItem.FaItem;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faItem.TagMgr;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FaItemDebugger {
    @Cmd("fatest")
    @OnlyForHelp
    @Help("测试命令")
    @Debug
    public static void fatest(){}

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
        TagMgr tagMgr = faItem.getTagMgr();

        System.out.println("[FaItemDebugger] type: " + faItem.getType());
        System.out.println("[FaItemDebugger] lore: " + faItem.getItemMeta().getLore());
        System.out.println("[FaItemDebugger] tag.a: " + tagMgr.get("a"));
        System.out.println("[FaItemDebugger] tag.b: " + tagMgr.get("b"));
        System.out.println("[FaItemDebugger] tag.c: " + tagMgr.get("c"));
    }
}
