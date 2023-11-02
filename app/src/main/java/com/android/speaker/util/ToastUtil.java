package com.android.speaker.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.speaker.MainApplication;
import com.chinsion.SpeakEnglish.R;

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

    /***
     * 自定义toast 上面图片，下面文字
     * @param activity
     * @param resourceId
     * @param tip
     */
    public static void showDefineToast(Context activity, int resourceId, String tip) {
        Toast toast = new Toast(activity);
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int padding = ScreenUtil.dip2px(20);
        linearLayout.setPadding(padding, 20, padding, padding/2);

        View view = null;
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(resourceId);
        view = imageView;


        TextView mTv = new TextView(activity);
        mTv.setText(tip);
        mTv.setTextSize(15);
        mTv.setTextColor(Color.WHITE);
        mTv.setGravity(Gravity.CENTER);

        linearLayout.addView(view);
        linearLayout.addView(mTv);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.drawable.toast_bg_shape);
        int height = ScreenUtil.getScreenHeight(activity) / 3;
        toast.setView(linearLayout);
        toast.setGravity(Gravity.TOP, 0, height);
        toast.show();
    }
}
