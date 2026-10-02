package com.wenkrang.faClip.module.faDebugger.module;

import com.wenkrang.faClip.module.FaClip;
import com.wenkrang.faClip.module.faCommand.annotation.Cmd;
import com.wenkrang.faClip.module.faCommand.annotation.Debug;
import com.wenkrang.faClip.module.faCommand.annotation.ForPlayer;
import com.wenkrang.faClip.module.faCommand.annotation.RequireOP;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faInterface.annotation.Intf;
import com.wenkrang.faClip.module.faInterface.FaIntfContext;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faItem.interpreter.helper.ItemDataHelper;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faWindow.FaInventory;
import com.wenkrang.faClip.module.faWindow.FaWindow;
import com.wenkrang.faClip.module.faWindow.FaWindowInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.List;

public class FaInvDebugger {
    public static FaWindowInstance faWindowInstance = null;
    public static FaItemInstance faItemInstance = null;

    @Cmd("fatest.inv.load")
    @RequireOP
    @ForPlayer
    @Debug
    public static void load(FaCmdContext faCmdContext) {
        ItemDataHelper.init(FaClip.plugin);
        faItemInstance = new FaItemInstance(FaClip.plugin);
        faItemInstance.loadAll();
        faItemInstance.autoRegister();

        faWindowInstance = new FaWindowInstance(FaClip.plugin, faItemInstance);
        faWindowInstance.loadAll();
        faWindowInstance.autoRegister();

        Fm.debug(faCmdContext.sender(), "FaWin调试套件已加载");
    }

    @Cmd("fatest.inv.open")
    @RequireOP
    @ForPlayer
    @Debug
    public static void open(FaCmdContext faCmdContext, String id) {
        Player player = (Player) faCmdContext.sender();

        FaInventory faInventory = faWindowInstance.getFaInventory(id);

        if (faInventory != null) {
            List<Integer> button = faInventory.getNote("button");

            button.forEach(i -> faInventory.set(i, faItemInstance.getFaItem("button")));

            FaWindow faWindow = new FaWindow(faWindowInstance, player);

            faWindow.open(faInventory);
        }
    }

    @Intf("fatest.inv.init")
    public static void onInit(FaIntfContext faIntfContext) {
        InventoryOpenEvent event = faIntfContext.get("event");

        Player player = (Player) event.getPlayer();

        Fm.info(player, "test");
    }
}
