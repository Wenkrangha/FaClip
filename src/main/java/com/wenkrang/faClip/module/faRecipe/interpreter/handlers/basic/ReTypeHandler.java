package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;

public class ReTypeHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        if (faData.has("type")) {
            faRecipe.type = faData.getString("type");
        }else if (!faData.has("template")) {
            throw new FaDataParseException(faData, "type", "FaRecipe.Exception.FaReInterpreter.NotFound");
        }
    }
}
