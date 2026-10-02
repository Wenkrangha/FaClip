package com.wenkrang.faClip.module.faDebugger.module;

import com.wenkrang.faClip.module.FaClip;
import com.wenkrang.faClip.module.faCommand.annotation.Cmd;
import com.wenkrang.faClip.module.faCommand.annotation.Debug;
import com.wenkrang.faClip.module.faCommand.annotation.RequireOP;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faResource.BukkitResource;
import com.wenkrang.faClip.module.faResource.FaBukkitResourceManager;
import org.bukkit.entity.Player;

public class FaResourceDebugger {
    @Cmd("fatest.resource")
    @RequireOP
    @Debug
    public static void resource(FaCmdContext ctx) {
        FaBukkitResourceManager faBukkitResourceManager = new FaBukkitResourceManager(FaClip.plugin);

        BukkitResource test = new BukkitResource("test"
                , "https://gitee.com/yuzutan29/OpenNoteBlockStudio/raw/master/datafiles/Data/extranotes.zip"
        ,"609D2EA415906ECBA8171ADE91BDA1C1056244AB");
        faBukkitResourceManager.registerResource(test);

        faBukkitResourceManager.askFor(test, (Player) ctx.sender(), "这是FaClip的测试，没有什么作用");

        Fm.debug("资源包测试");
    }
}
