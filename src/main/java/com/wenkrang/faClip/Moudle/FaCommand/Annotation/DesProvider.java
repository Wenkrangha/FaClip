package com.wenkrang.faClip.Moudle.FaCommand.Annotation;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import org.jetbrains.annotations.NotNull;

public interface DesProvider {
    @NotNull String[] getDes(FaCmdContext faCmdContext);
}