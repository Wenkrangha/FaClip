package com.wenkrang.faClip.Module.FaCommand.Annotation;

import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import org.jetbrains.annotations.NotNull;

public interface DesProvider {
    @NotNull String[] getDes(FaCmdContext faCmdContext);
}