package com.android.speaker.course;

import java.io.Serializable;

public class ChatItem implements Serializable {
    public String name;
    public String icon;
    public String content;
    public String audioOssKey;
    public String transfer;
    public String teacherInsights;
    public double startTime;
    public double endTime;
    public String uniqueId;
    public boolean isSuccess;
}
