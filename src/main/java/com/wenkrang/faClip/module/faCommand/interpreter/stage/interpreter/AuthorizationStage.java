package com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter;

import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.SimpleStage;
import com.wenkrang.faClip.module.faMessage.Fm;

import java.util.List;

import static com.wenkrang.faClip.module.faMessage.helper.I18nHelper.t;

/**
 * 统一授权检查阶段
 * <p>合并了 OP 检查、权限检查、玩家检查三个阶段。
 * 通过 {@link FaCmd#canExecute} 和 {@link FaCmd#getRejectReason} 委托决策。</p>
 */
public class AuthorizationStage implements SimpleStage {
    @Override
    public boolean check(FaCmd cmd, FaCmdContext faCmdContext, List<FaCmd> faCmds) {
        if (cmd == null) return false;
        
        String reason = cmd.getRejectReason(faCmdContext.sender());
        if (reason != null) {
            Fm.error(faCmdContext.sender(), t(reason));
            return false;
        }
        
        return true;
    }
}
