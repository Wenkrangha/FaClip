package com.wenkrang.faClip.module.faItem.interpreter.handler.extra;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faItem.FaItem;
import com.wenkrang.faClip.module.faItem.interpreter.FaItemInterpreter;
import com.wenkrang.faClip.module.faItem.interpreter.handler.FaItemHandler;

public class TemplateHandler implements FaItemHandler {
    @Override
    public String getNode() {
        return "template";
    }

    @Override
    public void handle(FaItem faItem, FaData faData, FaItemInterpreter faItemInterpreter) {
        if (faData.getString("template") != null) {
            String template = faData.getString("template");
            faItem.setTemplate(template);

            faData.template(FaData.getPluginResource(template, faItemInterpreter.plugin));
        }
    }
}
