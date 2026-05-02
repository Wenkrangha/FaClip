package com.wenkrang.faClip.Moudle.FaMessage;

public interface MessageHandler {
    public void info(String s);
    public void warn(String s);
    public void error(String s);
    public void debug(String s);

    public void detailedInfo(String title, String des, String body);
    public void detailedWarn(String title, String des, String body);
    public void detailedError(String title, String des, String body);
    public void detailedDebug(String title, String des, String body);
}
