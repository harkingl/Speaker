package com.android.speaker.server.okhttp;

/***
 * 请求回调
 * @param <R>
 */
public interface RequestListener<R>{
    void onSuccess(R result);
    void onFailed(Throwable e);
}
