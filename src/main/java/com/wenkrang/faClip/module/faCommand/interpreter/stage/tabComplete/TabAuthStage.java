package com.wenkrang.faClip.module.faCommand.interpreter.stage.tabComplete;

import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.SimpleStage;

import java.util.List;

/**
 * 统一 Tab 补全授权检查阶段
 * <p>合并了 Tab OP 检查、Tab 权限检查、Tab 玩家检查三个阶段。
 * 通过 {@link FaCmd#canExecute} 委托决策。</p>
 */
public class TabAuthStage implements SimpleStage {
    @Override
    public boolean check(FaCmd cmd, FaCmdContext faCmdContext, List<FaCmd> faCmds) {
        return cmd.canExecute(faCmdContext.sender());
    }
}
