package com.android.speaker.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.update.VersionInfo;
import com.chinsion.SpeakEnglish.R;


public class DialogUtil {
    private static final String TAG = "DialogUtil";

    public static void showCommonDoubleDialog(final Context context, String title, String message, String leftText, String rightText, IDialogListener listener) {
        Builder builder = new Builder(context, R.style.CommonDialogStyle);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_common_view_double);
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = ScreenUtil.getScreenWidth(context);
        lp.width = (int)(width*0.8);//WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView titleView = (TextView) window.findViewById(R.id.dialog_common_view_title_text);
        titleView.setText(title);
        TextView messageView = (TextView) window.findViewById(R.id.dialog_common_view_message_text);
        if(!TextUtils.isEmpty(message)) {
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }
        TextView leftButton = (TextView) window.findViewById(R.id.dialog_common_view_button_left);
        TextView rightButton = (TextView) window.findViewById(R.id.dialog_common_view_button_right);
        if(!TextUtils.isEmpty(leftText)) {
            leftButton.setText(leftText);
        }
        if(!TextUtils.isEmpty(rightText)) {
            rightButton.setText(rightText);
        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onLeftClick();
                }
                dialog.dismiss();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onRightClick("");
                }
                dialog.dismiss();
            }
        });
    }

    public static void showCommonSingleDialog(final Context context, String title, String message, String btnText) {
        Builder builder = new Builder(context, R.style.CommonDialogStyle);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_common_view_single);
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = ScreenUtil.getScreenWidth(context);
        lp.width = (int)(width*0.8);//WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView titleView = (TextView) window.findViewById(R.id.dialog_common_view_title_text);
        titleView.setText(title);
        TextView messageView = (TextView) window.findViewById(R.id.dialog_common_view_message_text);
        if(!TextUtils.isEmpty(message)) {
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }
        TextView btn = (TextView) window.findViewById(R.id.dialog_common_view_button);
        TextView rightButton = (TextView) window.findViewById(R.id.dialog_common_view_button_right);
        if(!TextUtils.isEmpty(btnText)) {
            btn.setText(btnText);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showOpenVipDialog(final Context context, IDialogListener listener) {
        Builder builder = new Builder(context, R.style.CommonDialogStyle);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_open_vip);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        int width = ScreenUtil.getScreenWidth(context);
//        lp.width = (int)(width*0.8);//WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
        ImageView closeIv = window.findViewById(R.id.dialog_close_iv);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView btn = window.findViewById(R.id.dialog_open_tv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener != null) {
                    listener.onRightClick("");
                }
            }
        });
    }

    public static void showUpdateDialog(final Context context, VersionInfo info, IDialogListener listener) {
        Builder builder = new Builder(context, R.style.CommonDialogStyle);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_update_version);

        TextView versionTv = window.findViewById(R.id.dialog_version_tv);
        if(info != null && !TextUtils.isEmpty(info.versionName)) {
            versionTv.setText(info.versionName);
        }

        TextView updateBtn = window.findViewById(R.id.dialog_update_tv);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener != null) {
                    listener.onRightClick(info.updateUrl);
                }
            }
        });
        TextView cancelBtn = window.findViewById(R.id.dialog_cancel_tv);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener != null) {
                    listener.onLeftClick();
                }
            }
        });
    }

    public interface IDialogListener {
        void onLeftClick();

        void onRightClick(String s);
    }
}
