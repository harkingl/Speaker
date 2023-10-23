package com.android.speaker.course;

import java.io.Serializable;

public class ChatItem implements Serializable {
    public static final int STATE_SENDING = 0;
    public static final int STATE_SEND_FAILED = 1;
    public static final int STATE_ANALYSISING = 2;
    public static final int STATE_FINISH = 3;

    public String name;
    public String icon;
    public String content;
    public String audioOssKey;
    public String transfer;
    public String uniqueId;
    public int state;
    public String score;
    public String analysis;
}
