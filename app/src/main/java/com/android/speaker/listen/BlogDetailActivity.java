package com.android.speaker.listen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.TitleTagView;
import com.android.speaker.course.CourseLectureDetail;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

public class BlogDetailActivity extends BaseActivity implements View.OnClickListener, IBlogDetailCallBack {
    private static final String TAG = "BlogDetailActivity";
    private static final int TAB_INDEX_VOICE = 0;
    private static final int TAB_INDEX_AI = 1;

    private ImageView mBackIv;
    private TitleTagView mVoiceTitleView;
    private TitleTagView mAiTitleView;
    private ImageView mStarIv;
    private ImageView mTranslateIv;
    private FrameLayout mContainerFl;
    private ExoPlayer mPlayer;
    private BlogDetailVoiceFragment mVoiceFragment;
    private BlogDetailAiFragment mAiFragment;
    private BlogItem mItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        initView();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.blog_detail_back_iv);
        mVoiceTitleView = findViewById(R.id.blog_detail_title_voice_tv);
        mAiTitleView = findViewById(R.id.blog_detail_title_ai_tv);
        mStarIv = findViewById(R.id.blog_detail_star_iv);
        mTranslateIv = findViewById(R.id.blog_detail_translate_iv);
        mContainerFl = findViewById(R.id.blog_detail_container_fl);

        mBackIv.setOnClickListener(this);
        mVoiceTitleView.setOnClickListener(this);
        mAiTitleView.setOnClickListener(this);
        mStarIv.setOnClickListener(this);
        mTranslateIv.setOnClickListener(this);
    }

    private void initData() {
        mItem = (BlogItem) getIntent().getSerializableExtra("blog_item");

        mVoiceFragment = new BlogDetailVoiceFragment(this);
        mAiFragment = new BlogDetailAiFragment(this);
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
                    mHandler.removeMessages(WHAT_UPDATE_PROGRESS);
                    // 更新进度100%
//                    if(mVoiceFragment != null) {
//                        mVoiceFragment.
//                    }
//                    mProgressBar.updateProgress((int) (mList.get(mList.size()-1).endTime*1000));
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);

                LogUtil.d(TAG, "onPlayWhenReadyChanged: " + playWhenReady + " " + reason);
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);

                if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
                    int currentWindowIndex = mPlayer.getCurrentWindowIndex();
                    long currentPositionMs = mPlayer.getCurrentPosition();
                    LogUtil.d(TAG, "onPositionDiscontinuity: currentWindowIndex=" + currentWindowIndex
                            + ", currentPositionMs=" + currentPositionMs);

//                    Message msg = mHandler.obtainMessage(WHAT_UPDATE_PROGRESS);
//                    msg.arg1 = (int) currentPositionMs;
//                    mHandler.sendMessageDelayed(msg, 100);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);

                if(isPlaying) {
                    mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 200);
                } else {
                    mHandler.removeMessages(WHAT_UPDATE_PROGRESS);
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
                Player.Listener.super.onPlayerError(error);
            }
        });

        changeTag(TAB_INDEX_VOICE);
        new GetProgramDetailRequest(this, "1").schedule(false, new RequestListener<BlogDetail>() {
            @Override
            public void onSuccess(BlogDetail result) {
                mVoiceFragment.setData(result);
                mAiFragment.setData(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private static final int WHAT_UPDATE_DURATION = 1;
    private static final int WHAT_HIDE_DURATION = 2;
    private static final int WHAT_UPDATE_PROGRESS = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case WHAT_UPDATE_PROGRESS:
                    updateProgress();
//                    if(mPlayer.isPlaying()) {
//                        sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 200);
//                    }
                    break;
            }
        }
    };

    private void updateProgress() {
        int currentPositionMs = (int) mPlayer.getCurrentPosition();

    }

    private void changeTag(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = null;
        switch (index) {
            case TAB_INDEX_VOICE:
                mVoiceTitleView.setColorBlockViewVisible(true);
                mAiTitleView.setColorBlockViewVisible(false);
                mTranslateIv.setVisibility(View.GONE);
                fragment = mVoiceFragment;
                break;
            case TAB_INDEX_AI:
                mVoiceTitleView.setColorBlockViewVisible(false);
                mAiTitleView.setColorBlockViewVisible(true);
                mTranslateIv.setVisibility(View.VISIBLE);
                fragment = mAiFragment;
                break;
        }
        if(fragment != null) {
            transaction.replace(R.id.blog_detail_container_fl, fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction tran) {
        if (null != mVoiceFragment) {
            tran.hide(mVoiceFragment);
        }
        if (null != mAiFragment) {
            tran.hide(mAiFragment);
        }
    }

    private void stopPlayer() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.blog_detail_back_iv:
                finish();
                break;
            case R.id.blog_detail_title_voice_tv:
                changeTag(TAB_INDEX_VOICE);
                break;
            case R.id.blog_detail_title_ai_tv:
                changeTag(TAB_INDEX_AI);
                break;
        }
    }

    @Override
    public void play() {
        mPlayer.play();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void seekTo(int positionMs) {
        mPlayer.seekTo(positionMs);
    }
}
