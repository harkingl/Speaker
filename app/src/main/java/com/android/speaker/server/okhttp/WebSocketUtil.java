package com.android.speaker.server.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.server.util.UrlManager;
import com.android.speaker.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketUtil {
    private static final String TAG = WebSocketUtil.class.getSimpleName();
    private static final int NORMAL_CLOSE_STATE = 1000;
    private OkHttpClient mClient;
    private WebSocket mWebSocket;
    private MessageReceivedListener mMessageListener;
    private String mConversationId;

    public WebSocketUtil() {
        mClient = new OkHttpClient().newBuilder().pingInterval(Duration.ofSeconds(40)).build();
    }

    public void connect() {
        Request request = new Request.Builder().url(UrlManager.WEBSOCKET_URL).build();
        mWebSocket = mClient.newWebSocket(request, mListener);
    }

    public void sendMessage(String message, String targetId) {
        if(TextUtils.isEmpty(message)) {
            return;
        }
        JSONObject obj = new JSONObject();
        try {
            if(TextUtils.isEmpty(mConversationId)) {
                obj.put("sceneId", targetId);
            } else {
                obj.put("conversationId", mConversationId);
            }
            obj.put("content", message);

            mWebSocket.send(obj.toString());
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    private WebSocketListener mListener = new WebSocketListener() {
        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            LogUtil.d(TAG, "onMessage：" + text);

            if(mMessageListener != null) {
                String content = text;
                try {
                    JSONObject obj = new JSONObject(text);
                    if(TextUtils.isEmpty(mConversationId)) {
                        mConversationId = obj.optString("conversationId");
                        content = obj.optString("data");
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                }
                mMessageListener.handleMessage(content);
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            LogUtil.d(TAG, "onMessage：" + bytes);
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
            LogUtil.d(TAG, "onClosed：" + reason);

            mHandler.sendEmptyMessageDelayed(WHAT_RETRY, 1000);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            LogUtil.d(TAG, "onFailure：" + t.getMessage());

            mHandler.sendEmptyMessageDelayed(WHAT_RETRY, 1000);
        }
    };

    private static final int WHAT_RETRY = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what == WHAT_RETRY) {
                connect();
            }
        }
    };

    public void disConnect() {
        mHandler.removeMessages(WHAT_RETRY);
        if(mWebSocket != null) {
            mWebSocket.close(NORMAL_CLOSE_STATE, null);
        }
        mClient.dispatcher().executorService().shutdown();
    }

    public interface MessageReceivedListener {
        void handleMessage(String message);
    }
}
