package com.wenkrang.faClip.Module.FaDebugger.module;

import com.wenkrang.faClip.Module.FaCommand.Annotation.Cmd;
import com.wenkrang.faClip.Module.FaCommand.Annotation.RequireOP;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaData.FaData;
import com.wenkrang.faClip.Module.FaData.FaItemData;
import com.wenkrang.faClip.Module.FaData.FaPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FaDataDebugger {
    @Cmd("fatest.data.yaml.set")
    @RequireOP
    public static void set() {
        FaData faData = new FaData("data/test");

        faData.set("test", "test");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("a");
        lore.add("b");
        lore.add("c");

        faData.set("list", lore);

        faData.save();
    }

    @Cmd("fatest.data.yaml.get")
    @RequireOP
    public static void get() {
        FaData faData = new FaData("data/test");

        System.out.println(faData.get("test"));
        System.out.println(faData.get("list"));
    }

    @Cmd("fatest.data.player.set")
    @RequireOP
    public static void set(FaCmdContext context) {
        FaData faData = new FaPlayerData((Player) context.sender(), "test");

        faData.set("test", "test");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("a");
        lore.add("b");
        lore.add("c");

        faData.set("list", lore);

        faData.save();
    }

    @Cmd("fatest.data.player.get")
    @RequireOP
    public static void get(FaCmdContext context) {
        FaData faData = new FaPlayerData((Player) context.sender(), "test");

        System.out.println(faData.get("test"));
        System.out.println(faData.get("list"));
    }

    // 对于物品的测试
    @Cmd("fatest.data.item.set")
    @RequireOP
    public static void setItem(FaCmdContext context) {
        Player player = (Player) context.sender();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        FaItemData faItemData = FaItemData.create(itemInMainHand);

        faItemData.set("test", "test");

        faItemData.save();
    }

    @Cmd("fatest.data.item.get")
    @RequireOP
    public static void getItem(FaCmdContext context) {
        Player player = (Player) context.sender();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        FaItemData faItemData = FaItemData.create(itemInMainHand);

        System.out.println(faItemData.get("test"));
    }
}
