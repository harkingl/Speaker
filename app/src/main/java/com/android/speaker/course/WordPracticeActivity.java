package com.android.speaker.course;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
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
import java.util.ArrayList;
import java.util.List;

public class WordPracticeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WordPracticeActivity";

    private static final int REQ_RECORD = 111;

    private TitleBarLayout mTitleBarLayout;
    private ProgressBar mProgressBar;
    private TextView mCurrentTv;
    private TextView mWordTv;
    private TextView mPronunciationTv;
    private TextView mExplainTv;
    private ImageView mVoiceTv;
    private WaveLineView mWaveView;
    private ProgressBar mLoadingBar;
    private TextView mTipTv;
    private View mBottomView;
    private TextView mMyRecordTv;
    private TextView mReplayTv;
    private TextView mScoreTv;
    private TextView mNextTv;
    private String mId;
    private List<WordInfo> mList;
    private WordInfo mCurrInfo;
    private ExoPlayer mPlayer;
    private MediaRecorder mediaRecorder;
    // 1：播放中 2：录音中 3：评分中 4：评分
    private int mCurrentStep = 1;
    // 录音文件路径
    private String mFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_practice);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.word_title_bar);
        mProgressBar = findViewById(R.id.word_progress_bar);
        mCurrentTv = findViewById(R.id.word_current_tv);
        mWordTv = findViewById(R.id.word_word_tv);
        mPronunciationTv = findViewById(R.id.word_pronunciation_tv);
        mExplainTv = findViewById(R.id.word_explain_tv);
        mVoiceTv = findViewById(R.id.word_voice_iv);
        mWaveView = findViewById(R.id.word_wave_view);
        mLoadingBar = findViewById(R.id.word_bottom_loading_bar);
        mTipTv = findViewById(R.id.word_tip_tv);
        mBottomView = findViewById(R.id.word_bottom_ll);
        mMyRecordTv = findViewById(R.id.word_my_record_tv);
        mReplayTv = findViewById(R.id.word_replay_tv);
        mScoreTv = findViewById(R.id.word_score_tv);
        mNextTv = findViewById(R.id.word_next_tv);

        mVoiceTv.setOnClickListener(this);
        mWaveView.setOnClickListener(this);
        mMyRecordTv.setOnClickListener(this);
        mReplayTv.setOnClickListener(this);
        mNextTv.setOnClickListener(this);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setImageResource(R.drawable.ic_add_word);
        mTitleBarLayout.setTitle("词汇热身", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
        mTitleBarLayout.setOnRightClickListener(this);
    }

    private void initData() {
        mId = getIntent().getStringExtra("id");

        mPlayer = new ExoPlayer.Builder(this).build();
        mPlayer.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                LogUtil.d(TAG, "onPlaybackStateChanged：" + playbackState);
                if(playbackState == Player.STATE_ENDED) {
//                    stopPlayer();
                    if(mCurrentStep == 1) {
                        mCurrentStep = 2;
                        updateBottomView();
                    }
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);

                if(reason == Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM) {
                    int position = mPlayer.getNextMediaItemIndex();
                }

                LogUtil.d(TAG, "onPlayWhenReadyChanged: " + playWhenReady + " " + reason);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
                Player.Listener.super.onPlayerError(error);
            }
        });

        new GetWordsRequest(this, mId).schedule(false, new RequestListener<List<WordInfo>>() {
            @Override
            public void onSuccess(List<WordInfo> result) {
                if(result != null && result.size() > 0) {
                    mList = result;
                    setView(result.get(0), 0);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });

//        updateBottomView();
    }

    private void setView(WordInfo info, int position) {
        mProgressBar.setMax(mList.size());
        mProgressBar.setProgress(position+1);
        mCurrentTv.setText((position+1) + "/" + mList.size());
        mWordTv.setText(info.word);
        mPronunciationTv.setText(info.mark);
        mExplainTv.setText(getExplain(info));

        mCurrInfo = info;
    }

    private String getExplain(WordInfo info) {
        String content = "";
        if(info.wordExplain != null) {
            if(info.wordExplain.n != null) {
                content = TextUtils.join(",", info.wordExplain.n);
            } else if(info.wordExplain.adj != null) {
                content = TextUtils.join(",", info.wordExplain.adj);
            } else if(info.wordExplain.adv != null) {
                content = TextUtils.join(",", info.wordExplain.adv);
            } else if(info.wordExplain.v != null) {
                content = TextUtils.join(",", info.wordExplain.v);
            }
        }

        return content;
    }

    private void updateBottomView() {
        if(mCurrentStep == 1) {
            mTipTv.setText("跟读提示：听到“滴”声后开始录音");
            mTipTv.setVisibility(View.VISIBLE);
            mWaveView.setVisibility(View.GONE);
            mLoadingBar.setVisibility(View.GONE);
            mBottomView.setVisibility(View.GONE);
        } else if(mCurrentStep == 2) {
            if(checkRecordPermission()) {
                mTipTv.setText("点击波纹结束录音");
                mTipTv.setVisibility(View.VISIBLE);
                mWaveView.setVisibility(View.VISIBLE);
                mLoadingBar.setVisibility(View.GONE);
                mBottomView.setVisibility(View.GONE);

                mWaveView.startAnim();
                startRecording();
            }
        } else if(mCurrentStep == 3) {
            mTipTv.setText("智能打分中");
            mTipTv.setVisibility(View.VISIBLE);
            mWaveView.setVisibility(View.GONE);
            mLoadingBar.setVisibility(View.VISIBLE);
            mBottomView.setVisibility(View.GONE);

            getScore();
        } else if(mCurrentStep == 4) {
            mTipTv.setVisibility(View.GONE);
            mWaveView.setVisibility(View.GONE);
            mLoadingBar.setVisibility(View.GONE);
            mBottomView.setVisibility(View.VISIBLE);
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mFilePath = FileUtil.createRecordFile().getAbsolutePath();
        mediaRecorder.setOutputFile(mFilePath);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        mediaRecorder.start();

        mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_MIC, SPACE);
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

    private void stopRecording() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private boolean checkRecordPermission() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    REQ_RECORD);
        } else {
            return true;
        }

        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();

        mWaveView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveView.onPause();
    }

    @Override
    protected void onStop() {
        stopPlayer();
        stopRecording();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mWaveView.release();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_RECORD) {
            updateBottomView();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mVoiceTv) {
            if(!mPlayer.isPlaying()) {
                doPlay(false);
            }
        } else if(v == mWaveView) {
            stopRecording();
            mCurrentStep = 3;
            updateBottomView();
        } else if(v == mMyRecordTv) {
            doPlay(true);
        } else if(v == mReplayTv) {
            mCurrentStep = 1;
            updateBottomView();
            doPlay(false);
        } else if(v == mNextTv) {
            int index = mList.indexOf(mCurrInfo);
            if(index+1 < mList.size()) {
                mCurrentStep = 1;
                updateBottomView();
                setView(mList.get(index+1), index+1);
            }
        } else if(v == mTitleBarLayout.getRightGroup()) {
            addNewWord();
        }
    }

    private void addNewWord() {
        List<String> list = new ArrayList<>();
        list.add(mCurrInfo.word);
        new AddNewWordRequest(this, list).schedule(true, new RequestListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailed(Throwable e) {

            }
        });
    }

    private void doPlay(boolean isMy) {
        mPlayer.clearMediaItems();
        String url = "";
        if(isMy && !TextUtils.isEmpty(mFilePath)) {
            url = Uri.fromFile(new File(mFilePath)).toString();
        } else if(mCurrInfo != null && !TextUtils.isEmpty(mCurrInfo.audioUrl)) {
            url = mCurrInfo.audioUrl;
        }
        if(!TextUtils.isEmpty(url)) {
            mPlayer.addMediaItem(MediaItem.fromUri(url));
            if(!isMy) {
                // 添加滴提示音
                String alertVoiceUrl = "android.resource://" + getPackageName() + "/" + R.raw.alert_voice;
                mPlayer.addMediaItem(MediaItem.fromUri(Uri.parse(alertVoiceUrl)));
            }
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
        new GetScoreRequest(this, new File(mFilePath), mCurrInfo.word).schedule(true, new RequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)) {
                    mScoreTv.setText(result);
                }
                mCurrentStep = 4;
                updateBottomView();
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }
}
