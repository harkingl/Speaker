package com.android.speaker.course;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.util.List;

public class WordPracticeActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarLayout mTitleBarLayout;
    private ProgressBar mProgressBar;
    private TextView mCurrentTv;
    private TextView mWordTv;
    private TextView mPronunciationTv;
    private TextView mExplainTv;
    private ImageView mVoiceTv;
    private String mId;
    private List<WordInfo> mList;
    private WordInfo mCurrInfo;
    private ExoPlayer mPlayer;

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

        mVoiceTv.setOnClickListener(this);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
        mTitleBarLayout.setTitle("词汇热身", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mId = getIntent().getStringExtra("id");

        mPlayer = new ExoPlayer.Builder(this).build();

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
    }

    private void setView(WordInfo info, int position) {
        mProgressBar.setMax(mList.size());
        mProgressBar.setProgress(position+1);
        mCurrentTv.setText((position+1) + "/" + mList.size());
        mWordTv.setText(info.word);

        mPlayer.clearMediaItems();
        mPlayer.addMediaItem(MediaItem.fromUri(info.audioUrl));

        mCurrInfo = info;
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mVoiceTv) {
            doPlay();
        }
    }

    private void doPlay() {
        if(mCurrInfo != null) {
            mPlayer.seekTo(0);
            mPlayer.prepare();
            mPlayer.play();
        }
    }
}
