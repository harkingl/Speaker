package com.android.speaker.listen;

public interface IBlogDetailCallBack {
    void play();
    void seekTo(int positionMs);
    void jumpPrev();
    void jumpNext();
    void jumpToPosition(int position);
}
