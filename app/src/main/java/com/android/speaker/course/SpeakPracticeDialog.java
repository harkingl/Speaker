package com.android.speaker.course;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.speaker.course.wave.WaveLineView;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.FileUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import java.io.File;
import java.io.IOException;

/***
 * 练习-speak
 */
public class SpeakPracticeDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "SpeakPracticeDialog";
    private TextView mContentTv;
    private TextView mTranslateTv;
    private ImageView mVoiceIv;
    private View mRecordLayout;
    private TextView mRecordTv;
    private TextView mSkipTv;
    private View mScoreLayout;
    private TextView mScoreTv;
    private ImageView mMyRecordIv;
    private TextView mContinueTv;
    private View mWaveLayout;
    private WaveLineView mWaveView;
    private ProgressBar mLoadingBar;
    private TextView mTipTv;
    private Context mContext;

    private QuestionListener mListener;
    private ExoPlayer mPlayer;
    private MediaRecorder mediaRecorder;
    private QuestionInfo mInfo;
    // 录音文件路径
    private String mFilePath;

    public SpeakPracticeDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_practice_speak);
        mContentTv = findViewById(R.id.dialog_content_tv);
        mTranslateTv = findViewById(R.id.dialog_translate_tv);
        mVoiceIv = findViewById(R.id.dialog_voice_iv);
        mRecordLayout = findViewById(R.id.dialog_record_ll);
        mRecordTv = findViewById(R.id.dialog_record_tv);
        mSkipTv = findViewById(R.id.dialog_skip_tv);
        mScoreLayout = findViewById(R.id.dialog_score_ll);
        mScoreTv = findViewById(R.id.dialog_score_tv);
        mMyRecordIv = findViewById(R.id.dialog_my_record_iv);
        mContinueTv = findViewById(R.id.dialog_btn_continue_tv);
        mWaveLayout = findViewById(R.id.dialog_wave_ll);
        mWaveView = findViewById(R.id.dialog_wave_view);
        mLoadingBar = findViewById(R.id.dialog_loading_bar);
        mTipTv = findViewById(R.id.dialog_tip_tv);

        mRecordTv.setOnClickListener(this);
        mSkipTv.setOnClickListener(this);
        mMyRecordIv.setOnClickListener(this);
        mContinueTv.setOnClickListener(this);
        mWaveView.setOnClickListener(this);
    }

    public void setData(QuestionInfo info) {
        if (info == null) {
            return;
        }
        this.mInfo = info;

        initData();
    }

    private void initData() {
        mContentTv.setText(mInfo.question);
        mTranslateTv.setText(mInfo.translation);

        initPlayer();
        initRecorder();
    }

    private void initPlayer() {
        if(mPlayer != null) {
            return;
        }
        mPlayer = new ExoPlayer.Builder(mContext).build();
        mPlayer.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState == Player.STATE_ENDED) {
                    mVoiceIv.setImageResource(R.drawable.ic_question_voice_default);
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
                Player.Listener.super.onPlayerError(error);
            }
        });
    }

    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }

    private static final int WHAT_UPDATE_MIC = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_UPDATE_MIC:
                    updateMic();
                    break;
            }
        }
    };

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间
    private void updateMic() {
        if (mediaRecorder != null) {
            double ratio = (double)mediaRecorder.getMaxAmplitude() /BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            LogUtil.d(TAG,"分贝值：" + db);
            mWaveView.setVolume((int) db);
//            mWaveView.setVolume(new Random().nextInt(100));

            mHandler.sendEmptyMessageDelayed(1, SPACE);
        }
    }

    private void startRecording() {
        if(mediaRecorder == null) {
            initRecorder();
        }
        mFilePath = FileUtil.createRecordFile().getAbsolutePath();
        mediaRecorder.setOutputFile(mFilePath);
//        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        mediaRecorder.start();

        mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_MIC, SPACE);
    }

    private void stopRecording() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dialog_btn_continue_tv:
            case R.id.dialog_skip_tv:
                if(mListener != null) {
                    mListener.onContinue();
                }
                dismiss();
                break;
            case R.id.dialog_voice_iv:
                if (!mPlayer.isPlaying()) {
                    doPlay(false);
                }
                break;
            case R.id.dialog_record_tv:
                mRecordLayout.setVisibility(View.GONE);
                mWaveLayout.setVisibility(View.VISIBLE);
                mWaveView.startAnim();
                startRecording();
                break;
            case R.id.dialog_wave_view:
                stopRecording();
                mWaveView.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.VISIBLE);
                mTipTv.setText("智能打分中");
                getScore();
                break;
            case R.id.dialog_my_record_iv:
                doPlay(true);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWaveView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWaveView.onPause();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        stopPlayer();
        stopRecording();
        if(mWaveView != null) {
            mWaveView.release();
        }
    }

    private void doPlay(boolean isMy) {
        mPlayer.clearMediaItems();
        String url = "";
        if(isMy && !TextUtils.isEmpty(mFilePath)) {
            url = Uri.fromFile(new File(mFilePath)).toString();
        } else if(mInfo != null && !TextUtils.isEmpty(mInfo.audioUrl)) {
            url = mInfo.audioUrl;
            mVoiceIv.setImageResource(R.drawable.ic_question_voice_select);
        }
        if(!TextUtils.isEmpty(url)) {
            mPlayer.addMediaItem(MediaItem.fromUri(url));
            mPlayer.prepare();
            mPlayer.play();
        }
    }

    private void stopPlayer() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    private void getScore() {
        new GetScoreRequest(mContext, new File(mFilePath), mInfo.question).schedule(true, new RequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)) {
                    mScoreTv.setText(result);
                }
                mWaveLayout.setVisibility(View.GONE);
                mScoreLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());

                mScoreTv.setText("0");
                mWaveLayout.setVisibility(View.GONE);
                mScoreLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setListener(QuestionListener mListener) {
        this.mListener = mListener;
    }
}
