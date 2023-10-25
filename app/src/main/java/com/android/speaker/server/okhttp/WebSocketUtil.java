package com.android.speaker.server.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.course.ChatItem;
import com.android.speaker.server.util.UrlManager;
import com.android.speaker.util.LogUtil;

import org.json.JSONObject;

import java.time.Duration;
import java.util.UUID;

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

    public void setMessageListener(MessageReceivedListener messageListener) {
        this.mMessageListener = messageListener;
    }

    public ChatItem sendMessage(String message, String targetId) {
        JSONObject obj = new JSONObject();
        try {
            if(TextUtils.isEmpty(mConversationId)) {
                obj.put("sceneId", targetId);
            } else {
                obj.put("conversationId", mConversationId);
            }
            String uuid = UUID.randomUUID().toString();
            obj.put("uniqueId", uuid);
            obj.put("content", message);

            boolean success = mWebSocket.send(obj.toString());

            ChatItem item = new ChatItem();
            item.uniqueId = uuid;
            item.content = message;
            item.state = success ? ChatItem.STATE_ANALYSISING : ChatItem.STATE_SEND_FAILED;
            return item;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

        return null;
    }

    public ChatItem sendAudio(byte[] bytes, String uniqueId) {
        try {
            boolean success = mWebSocket.send(new ByteString(bytes));

            ChatItem item = new ChatItem();
            item.uniqueId = uniqueId;
            item.state = ChatItem.STATE_SENDING;
            return item;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

        return null;
    }

    private WebSocketListener mListener = new WebSocketListener() {
        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            LogUtil.d(TAG, "onMessage：" + text);

            if(mMessageListener != null) {
                mMessageListener.handleMessage(text);
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            LogUtil.d(TAG, "onMessage11：" + bytes);
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
