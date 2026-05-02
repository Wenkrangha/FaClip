package com.wenkrang.faClip.Moudle.FaMessage;

import com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper;

public class Fm implements MessageHandler {
    private final i18nHelper i18nHelper;

    public Fm(i18nHelper i18nHelper) {
        this.i18nHelper = i18nHelper;
    }

    public Fm() {
        this.i18nHelper = new i18nHelper();
    }

    @Override
    public void info(String s) {

    }

    @Override
    public void warn(String s) {

    }

    @Override
    public void error(String s) {

    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void detailedInfo(String title, String des, String body) {

    }

    @Override
    public void detailedWarn(String title, String des, String body) {

    }

    @Override
    public void detailedError(String title, String des, String body) {

    }

    @Override
    public void detailedDebug(String title, String des, String body) {

    }

    public i18nHelper getI18nHelper() {
        return i18nHelper;
    }
}
