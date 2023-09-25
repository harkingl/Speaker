package com.android.speaker.listen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleTagView;
import com.android.speaker.course.AnalysisItem;
import com.android.speaker.course.CourseLectureDetail;
import com.android.speaker.course.GetCourseLectureDetailRequest;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import java.util.List;

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
    private BlogDetail mDetail;
    private List<AnalysisItem> mList;
    private int mCurrentIndex;
    private int mCurrentTab = TAB_INDEX_VOICE;
    private boolean mIsOpen = false;
    // 是否收藏
    private boolean mIsFavorite = false;

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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mVoiceFragment = new BlogDetailVoiceFragment(this);
        transaction.add(R.id.blog_detail_container_fl, mVoiceFragment);
        mAiFragment = new BlogDetailAiFragment(this);
        transaction.add(R.id.blog_detail_container_fl, mAiFragment);
        transaction.commitAllowingStateLoss();

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
                    updateProgress();
                    mVoiceFragment.play(false);
                    mAiFragment.play(false);
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

        new GetCourseLectureDetailRequest(this, "40").schedule(false, new RequestListener<CourseLectureDetail>() {
            @Override
            public void onSuccess(CourseLectureDetail result) {
                getData(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void getData(CourseLectureDetail detail) {
        new GetProgramDetailRequest(this, "1").schedule(false, new RequestListener<BlogDetail>() {
            @Override
            public void onSuccess(BlogDetail result) {
                result.audioUrl = detail.audioSssKey;
                mList = detail.analysisItemList;
                result.list = detail.analysisItemList;
                preparePlayer(detail.audioSssKey);

                result.audioDuration = 0.5f;
                if(mVoiceFragment != null) {
                    mVoiceFragment.setData(result);
                }
                if(mAiFragment != null) {
                    mAiFragment.setData(result);
                }

                mDetail = result;
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void preparePlayer(String url) {
        mPlayer.addMediaItem(MediaItem.fromUri(url));
        mPlayer.prepare();
        mPlayer.play();

        mAiFragment.play(true);
        mVoiceFragment.play(true);
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
                    if(mPlayer.isPlaying()) {
                        sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 200);
                    }
                    break;
            }
        }
    };

    private void updateProgress() {
        int currentPositionMs = (int) mPlayer.getCurrentPosition();
        if(mList.get(mCurrentIndex).endTime*1000 < currentPositionMs && mCurrentIndex < mList.size()-1) {
            mCurrentIndex++;

            if(mCurrentTab == TAB_INDEX_AI) {
                mAiFragment.setSelectIndex(mCurrentIndex);
            }
        }
        if(mCurrentTab == TAB_INDEX_AI) {
            mAiFragment.updateProgress(currentPositionMs);
        } else {
            mVoiceFragment.updateProgress(currentPositionMs);
        }
    }

    private void changeTag(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case TAB_INDEX_VOICE:
                mVoiceTitleView.setColorBlockViewVisible(true);
                mAiTitleView.setColorBlockViewVisible(false);
                mTranslateIv.setVisibility(View.GONE);
//                if(mVoiceFragment == null) {
//                    mVoiceFragment = new BlogDetailVoiceFragment(this);
//                    transaction.add(R.id.blog_detail_container_fl, mVoiceFragment);
//                }
                transaction.show(mVoiceFragment);
                mVoiceFragment.updateProgress((int) mPlayer.getCurrentPosition());
                mCurrentTab = TAB_INDEX_VOICE;
                break;
            case TAB_INDEX_AI:
                mVoiceTitleView.setColorBlockViewVisible(false);
                mAiTitleView.setColorBlockViewVisible(true);
                mTranslateIv.setVisibility(View.VISIBLE);
//                if(mAiFragment == null) {
//                    mAiFragment = new BlogDetailAiFragment(this);
//                    transaction.add(R.id.blog_detail_container_fl, mAiFragment);
//                    mAiFragment.setData(mDetail);
//                }
                transaction.show(mAiFragment);
                mAiFragment.updateProgress((int) mPlayer.getCurrentPosition());
                mAiFragment.setSelectIndex(mCurrentIndex);
                mCurrentTab = TAB_INDEX_AI;
                break;
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

    @Override
    protected void onStop() {
        stopPlayer();
        super.onStop();
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
            case R.id.blog_detail_translate_iv:
                mIsOpen = !mIsOpen;
                mTranslateIv.setImageResource(mIsOpen ? R.drawable.ic_translate_ch_blue : R.drawable.ic_translate_en_blue);
                mAiFragment.setTranslateOpen(mIsOpen);
                break;
            case R.id.blog_detail_star_iv:
                addBlog();
                break;
        }
    }

    private void addBlog() {
        if(mIsFavorite) {

        } else {
            new AddBlogRequest(this, mItem.id).schedule(false, new RequestListener<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    ToastUtil.toastLongMessage("收藏成功");
                    mIsFavorite = true;
                    mStarIv.setImageResource(R.drawable.ic_star_blue_select);
                }

                @Override
                public void onFailed(Throwable e) {
                    ToastUtil.toastLongMessage(e.getMessage());
                }
            });
        }
    }

    @Override
    public void play() {
        if(mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.play();
        }
//        if(mCurrentTab == TAB_INDEX_AI) {
//            mAiFragment.play(mPlayer.isPlaying());
//        } else {
//            mVoiceFragment.play(mPlayer.isPlaying());
//        }
        mAiFragment.play(mPlayer.isPlaying());
        mVoiceFragment.play(mPlayer.isPlaying());
    }

    @Override
    public void seekTo(int positionMs) {
        mPlayer.seekTo(positionMs);
    }

    @Override
    public void jumpPrev() {
        if(mCurrentIndex <= 0) {
            return;
        }
        jumpToPosition(mCurrentIndex-1);
    }

    @Override
    public void jumpNext() {
        if(mCurrentIndex >= mList.size()-1) {
            return;
        }
        jumpToPosition(mCurrentIndex+1);
    }

    @Override
    public void jumpToPosition(int position) {
        if(position < 0 || position >= mList.size()) {
            return;
        }
        mCurrentIndex = position;
        AnalysisItem item = mList.get(position);
        mPlayer.seekTo((int)(item.startTime*1000));
        if(mCurrentTab == TAB_INDEX_AI) {
            mAiFragment.setSelectIndex(position);
        }
        updateProgress();
    }
}
