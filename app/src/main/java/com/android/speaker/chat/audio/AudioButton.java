package com.android.speaker.chat.audio;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.android.speaker.util.FileUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.io.File;

public class AudioButton extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TAG = "AudioButton";
    private static final int REQ_RECORD = 111;
    private static final int DISTANCE_Y_CANCEL = 50;
    private float startY;
    private float endY;

    private Audio audio;
    private Dialog dialog;
    private View inputLayout;
    private TextView inputTv;
    private AudioAnimationView inputAnimView;
    private View cancelLayout;
    private AudioAnimationView cancelAnimView;
    private TextView tipTv;
    private ImageView closeIv;
    private ImageView bottomIv;
    private View bottomSyncLayout;
    private ProgressBar bottomSyncPb;
    private ImageView syncOkIv;

    private static final int DEFAULT_TIME = Audio.DEFAULT_MAX_DURATION;// 默认最大时间
    private static final int MIN_TIME = 1; // 最短录制时间，默认1s

    private static int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ANALYSISING = 2;// 分析中
    private static final int RECORD_ANALYSIS_FINISH = 3;// 分析中
    private static int RECORD_ED = 4; // 完成录音

    private static int recordState = 0; // 录音的状态

    private static float recodeTime = 0.0f; // 录音的时间
    private float voiceValue = 0.0f; // 麦克风获取的音量值

    private File audioFile;// 保存的语音文件

    private Context context;

    private AudioFinishListener listener;

    public AudioButton(Context context) {
        this(context, null, 0);
    }

    public AudioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Audio.RecordListener recordListener;

    public AudioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initAudio();
        initDialog();
    }

    private void initAudio() {
        audio = Audio.getInstance(context);
        recordListener = new Audio.RecordListener() {
            @Override
            public void onCompleted(String filePath) {
                onFinish();
            }

            @Override
            public void onError() {
                deleteAudio();
            }
        };

        audio.setRecordListener(recordListener);
    }

    private void onFinish() {
        stopRecord();
        if (recodeTime < MIN_TIME) {
            hideVoiceDialog();
            deleteAudio();
            ToastUtil.showDefineToast(context, R.drawable.ic_toast_tip,
                    "说话时间太短");// 显示录音时间太短对话框
            recordState = RECORD_NO;
        } else {
            recordState = RECORD_ANALYSISING;
            tipTv.setText("取消");
            bottomIv.setVisibility(GONE);
            bottomSyncLayout.setVisibility(VISIBLE);
            bottomSyncPb.setVisibility(VISIBLE);
            syncOkIv.setVisibility(GONE);
            // 显示播放文件
            if (audioFile != null) {
                listener.onAudioFinish(true);
            } else {
                listener.onAudioFinish(false);
            }
        }
    }

    public interface AudioFinishListener {
        public void onAudioFinish(boolean isSuccess);
        public void onAnalysisFinish(String content);
    }

    public AudioFinishListener getListener() {
        return listener;
    }

    public void setListener(AudioFinishListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_UP && recordState == RECORD_ING) {
//            stopRecord();
//        }
        return super.dispatchTouchEvent(ev);
    }

    private void deleteAudio() {
        if (audioFile != null && audioFile.exists()) {
            audioFile.delete();
            audioFile = null;
        }
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!checkRecordPermission()) {
                    break;
                }

                startY = event.getY();
                if (recordState != RECORD_ING) {// 不等于正在录音
                    startRecord();
                    startAnimThread();
                }
        break;
            case MotionEvent.ACTION_MOVE:
                endY = event.getY();
                checkPosition(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (recordState == RECORD_ING) {
                    onFinish();
                }
                break;
        }
        return true;
    }

    private void checkPosition(float x, float y) {
        if(y <= bottomIv.getTop() && cancelLayout.getVisibility() == GONE) {
            tipTv.setText("松开 取消");
//            fadeInAnim(cancelLayout);
//            fadeOutAnim(inputLayout);
            cancelLayout.setVisibility(VISIBLE);
            inputLayout.setVisibility(GONE);
            inputAnimView.stopAnimation();
            cancelAnimView.startAnimation();
        } else if(y > bottomIv.getTop() && inputLayout.getVisibility() == GONE) {
            tipTv.setText("松开 发送");
            cancelLayout.setVisibility(GONE);
            inputLayout.setVisibility(VISIBLE);
//            fadeInAnim(inputAnimView);
//            fadeOutAnim(cancelAnimView);
            inputAnimView.startAnimation();
            cancelAnimView.stopAnimation();
        }
    }

    private void fadeInAnim(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setVisibility(View.VISIBLE);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    private void fadeOutAnim(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }

        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    private Runnable animRunnable = new Runnable() {

        @Override
        public void run() {
            recodeTime = 0.0f;
            while (recordState == RECORD_ING) {
                try {
                    Thread.sleep(200);
                    recodeTime += 0.2;
                    voiceValue = audio.getAmplitude();// 返回振幅
                    imgHandle.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    LogUtil.e(TAG, e.getMessage());
                }
            }
        }

        Handler imgHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        if (recordState == RECORD_ING) {
                            recordState = RECORD_ED;
                            hideVoiceDialog();
                            audio.stopRecord();// 停止录音
                            voiceValue = 0.0f;
                        }
                        break;
                    case 1:// 显示振幅图片
                        setDialogImage();
                        break;
                    default:
                        break;
                }
            }
        };
    };

    // 录音Dialog图片随声音大小切换
    void setDialogImage() {
        if(inputLayout.getVisibility() == VISIBLE) {
            inputAnimView.updateVoiceValue(voiceValue);
        } else {
            cancelAnimView.updateVoiceValue(voiceValue);
        }
//        if ((Math.abs(endY - startY) > 100)) {
//            dialog_img.setImageResource(R.drawable.gh_base_release_finger_cancel);// 提示松开手指，取消发送
//            tv_releaseFinger.setText(context.getResources().getString(R.string.cancel_record));
//            tv_releaseFinger.setBackgroundResource(R.drawable.gh_base_release_finger_cancel_bg);
//            return;
//        }
//
//        tv_releaseFinger.setText(context.getResources().getString(R.string.fingerup_cancel_send));
//        tv_releaseFinger.setBackgroundResource(R.drawable.gh_base_up_finger_bg);
//        if (voiceValue < 100.0) {
//            dialog_img.setImageResource(R.drawable.gh_cm_record_animate_00);
//        } else if (voiceValue >= 100.0 && voiceValue < 400) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_01);
//        } else if (voiceValue >= 400.0 && voiceValue < 1600) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_02);
//        } else if (voiceValue >= 1600.0 && voiceValue < 3500) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_03);
//        } else if (voiceValue >= 3500.0 && voiceValue < 7000) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_04);
//        } else if (voiceValue >= 7000.0 && voiceValue < 10000) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_05);
//        } else if (voiceValue > 10000.0 && voiceValue < 12000) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_06);
//        } else if (voiceValue >= 12000.0) {
//            dialog_img.setImageResource(R.drawable.gh_base_record_animate_07);
//        }
    }

    private void startAnimThread() {
        new Thread(animRunnable).start();
    }

    private void startRecord() {
        recordState = RECORD_ING;
        tipTv.setText("松开 取消");
        bottomIv.setVisibility(VISIBLE);
        bottomSyncLayout.setVisibility(GONE);
//        audio.setInfoListener(infoListener);
        showVoiceDialog();
        audioFile = FileUtil.createRecordFile();
        audio.startRecord(audioFile.getAbsolutePath(), DEFAULT_TIME * 1000);
        inputAnimView.startAnimation();
        inputTv.setText("...");
    }

    private void stopRecord() {
        if (recordState == RECORD_ING) {
            recordState = RECORD_ED;
            audio.stopRecord();
            voiceValue = 0.0f;
        }
    }

    private void showVoiceDialog() {
        dialog.show();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ScreenUtil.getScreenWidth(context);
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
    }

    private void hideVoiceDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void initDialog() {
        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_audio_record);
        inputLayout = dialog.findViewById(R.id.dialog_input_anim_ll);
        inputTv = (TextView) dialog.findViewById(R.id.dialog_voice_input_tv);
        inputAnimView = dialog.findViewById(R.id.dialog_audio_input_anim_view);
        cancelLayout = dialog.findViewById(R.id.dialog_cancel_anim_ll);
        cancelAnimView = dialog.findViewById(R.id.dialog_audio_cancel_anim_view);
        tipTv = dialog.findViewById(R.id.dialog_record_tip_tv);
        closeIv = dialog.findViewById(R.id.dialog_record_close_iv);
        bottomIv = dialog.findViewById(R.id.dialog_bottom_iv);
        bottomSyncLayout = dialog.findViewById(R.id.dialog_bottom_sync_ll);
        bottomSyncPb = dialog.findViewById(R.id.dialog_bottom_sync_pb);
        syncOkIv = dialog.findViewById(R.id.dialog_bottom_sync_ok_iv);

        closeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordState == RECORD_ANALYSISING || recordState == RECORD_ANALYSIS_FINISH) {
                    hideVoiceDialog();
                    deleteAudio();
                    recordState = RECORD_NO;
                }
            }
        });

        bottomSyncLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordState == RECORD_ANALYSIS_FINISH) {
                    if(listener != null) {
                        listener.onAnalysisFinish(mContent);
                    }
                    hideVoiceDialog();
                    recordState = RECORD_NO;
                }
            }
        });
    }

    public String getAudioPath() {
        return audioFile.getAbsolutePath();
    }

    private boolean checkRecordPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    REQ_RECORD);
        } else {
            return true;
        }

        return false;
    }

    public void release() {
        if(audio != null) {
            audio.release();
        }
    }

    private String mContent;
    public void analysisFinish(String content) {
        this.mContent = content;
        if(!TextUtils.isEmpty(content)) {
            inputTv.setText(content);
        }
        bottomSyncPb.setVisibility(GONE);
        syncOkIv.setVisibility(VISIBLE);
        recordState = RECORD_ANALYSIS_FINISH;
    }
}
