package com.android.speaker.home;

public interface IHomeCallBack {
    public static final int TAB_STUDY = 0;
    public static final int TAB_COURSE = 1;
    public static final int TAB_LISTEN = 2;
    public static final int TAB_ME = 3;
    void callback(int tabIndex, Object value);
}
