package com.wenkrang.faClip.Moudle.FaCommand.FaHelperGenerator;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdInterpreter;
import org.jetbrains.annotations.NotNull;

public class FaHelperGenerator {
    private final FaCmdInstance faCmdInstance;

    public FaHelperGenerator(@NotNull FaCmdInstance Instance) {
        faCmdInstance = Instance;
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }

    public void generate(String node) {

    }
}
