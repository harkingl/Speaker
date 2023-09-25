package com.android.speaker.course;

import java.io.Serializable;

public class AnalysisItem implements Serializable {
    public String sentence;
    public String grammar;
    public String translation;
    public double startTime;
    public double endTime;
}
