package com.wenkrang.faClip.module.faCommand.interpreter.stage;

import com.wenkrang.faClip.module.faCommand.FaCmd;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;

import java.util.List;

public interface SimpleStage {
    boolean check(FaCmd cmd, FaCmdContext faCmdContext, List<FaCmd> faCmds);
}
