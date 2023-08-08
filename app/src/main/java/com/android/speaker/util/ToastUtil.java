package com.android.speaker.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.speaker.MainApplication;

public class ToastUtil {

    private final static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;

    public static void toastLongMessage(final String message) {
        toastMessage(message, true);
    }

    public static void toastShortMessage(final String message) {
        toastMessage(message, false);
    }

    private static void toastMessage(final String message, boolean isLong) {
        if(TextUtils.isEmpty(message)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
                toast = Toast.makeText(MainApplication.getInstance(), message,
                        isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                View view = toast.getView();
                if (view != null) {
                    TextView textView = view.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setGravity(Gravity.CENTER);
                    }
                }
//                int height = ScreenUtil.getScreenHeight(ServiceInitializer.getAppContext());
//                toast.setGravity(Gravity.CENTER, 0, height/2);

                toast.show();
            }
        });
    }
}
