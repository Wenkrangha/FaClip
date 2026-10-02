package com.wenkrang.faClip.module.faCommand.interpreter.stage.interpreter;

import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import com.wenkrang.faClip.module.faCommand.interpreter.stage.SimpleStage;
import com.wenkrang.faClip.module.faMessage.Fm;

import java.util.List;

import static com.wenkrang.faClip.module.faMessage.helper.I18nHelper.t;

public class EmptyCheckStage implements SimpleStage {
    @Override
    public boolean check(FaCmd cmd, FaCmdContext faCmdContext, List<FaCmd> faCmds) {
        if (faCmds.isEmpty()) {
            Fm.error(faCmdContext.sender(), t("FaCommand.Error.Interpreter.NotFound"));
            return false;
        }
        assert cmd != null;
        return true;
    }
}
