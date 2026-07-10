package com.wenkrang.faClip.Module.FaItem;

import java.util.HashMap;
import java.util.Map;

public class FaItemInstance {
    private final Map<String,FaItem> faItems;

    public FaItemInstance() {
        this.faItems = new HashMap<>();
    }

    public FaItem getFaItem(String key) {
        return faItems.get(key);
    }
}
