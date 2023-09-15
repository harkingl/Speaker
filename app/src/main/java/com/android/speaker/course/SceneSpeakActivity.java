package com.android.speaker.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.home.FragmentAdapter;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.TimeUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

/***
 * 场景对话
 */
public class SceneSpeakActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SceneSpeakActivity";

    private ImageView mBackIv;
    private ImageView mTranslateIv;
    private ImageView mNoteIv;
    private ViewPager2 mViewPager;
    private LinearLayout mIndicationsLayout;
    private ListView mListView;
    private View mScrollSelectLayout;
    private TextView mScrollDurationTv;
    private TextView mStartTv;
    private View mBottomStartLayout;
    private View mBottomPlayLayout;
    private ImageView mJumpPrevIv;
    private ImageView mJumpNextIv;
    private ImageView mPlayIv;
    private PlayProgressBar mProgressBar;
    private CourseItem mInfo;
    private List<ImageView> mIndicationViews = new ArrayList<>();
    private List<BaseFragment> mPageList = new ArrayList<>();
    private List<SceneSpeakDetail.SpeakItem> mList;
    private SceneSpeakAdapter mAdapter;
    private boolean mIsOpen = true;
    private int mCurrSelectPosition = -1;
    private ExoPlayer mPlayer;
    private SceneSpeakDetail mDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_speak);

        initView();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.scene_speak_back_iv);
        mTranslateIv = findViewById(R.id.scene_speak_translate_iv);
        mNoteIv = findViewById(R.id.scene_speak_note_iv);
        mViewPager = findViewById(R.id.scene_speak_view_pager);
        mIndicationsLayout = findViewById(R.id.scene_speak_indications_ll);
        mListView = findViewById(R.id.scene_speak_analysis_lv);
        mScrollSelectLayout = findViewById(R.id.scene_speak_scroll_select_ll);
        mScrollDurationTv = findViewById(R.id.scene_speak_scroll_duration_tv);
        mStartTv = findViewById(R.id.scene_speak_btn_start_tv);
        mBottomStartLayout = findViewById(R.id.scene_speak_bottom_start_ll);
        mBottomPlayLayout = findViewById(R.id.scene_speak_bottom_play_ll);
        mJumpPrevIv = findViewById(R.id.scene_speak_jump_prev_iv);
        mPlayIv = findViewById(R.id.scene_speak_play_iv);
        mJumpNextIv = findViewById(R.id.scene_speak_jump_next_iv);
        mProgressBar = findViewById(R.id.scene_speak_progress_bar);

        mBackIv.setOnClickListener(this);
        mTranslateIv.setOnClickListener(this);
        mNoteIv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
        mJumpPrevIv.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
        mJumpNextIv.setOnClickListener(this);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                updateIndicationView(position);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpToPosition(position);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    mHandler.removeMessages(WHAT_HIDE_DURATION);
                    mScrollSelectLayout.setVisibility(View.VISIBLE);
                    setScrollDuration();
                } else if(scrollState == SCROLL_STATE_IDLE) {
                    mHandler.sendEmptyMessageDelayed(WHAT_HIDE_DURATION, 2000);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_DURATION, 200);
            }
        });

        mProgressBar.setOnProgressChangedListener(new PlayProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int total, int current) {
                if(current >= 0) {
                    for(SceneSpeakDetail.SpeakItem item : mList) {
                        if(current <= item.endTime*1000) {
                            jumpToPosition(mList.indexOf(item));
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setScrollDuration() {
        int paddingTop = ScreenUtil.dip2px(30);
        View firstItem = mListView.getChildAt(0);
        int scrollHeight = paddingTop - firstItem.getTop();
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        View firstVisibleView = mListView.getChildAt(firstVisiblePosition);
        int scrollSelectViewTop = mScrollSelectLayout.getTop();
        int position = -1;
        if(firstVisibleView.getBottom()-scrollHeight >= scrollSelectViewTop) {
            position = firstVisiblePosition;
        } else {
           for(int i = firstVisiblePosition + 1; i < mList.size(); i++) {
               View itemView = mListView.getChildAt(i);
               if(itemView.getBottom()-scrollHeight >= scrollSelectViewTop) {
                   position = i;
                   break;
               }
           }
        }
        if(position != -1 && position != mCurrSelectPosition) {
            mCurrSelectPosition = position;
            mScrollDurationTv.setText(TimeUtil.timeToString((int)(mList.get(position).startTime)));
        }
    }

    private void initData() {
        mInfo = (CourseItem) getIntent().getSerializableExtra("course_item");

        mTranslateIv.setImageResource(R.drawable.ic_translate_zh);

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
                    mProgressBar.updateProgress((int) (mList.get(mList.size()-1).endTime*1000));
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
                    mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 1000);
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

        new GetSceneSpeakDetailRequest(this, mInfo.id).schedule(false, new RequestListener<SceneSpeakDetail>() {
            @Override
            public void onSuccess(SceneSpeakDetail result) {
                setView(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(SceneSpeakDetail detail) {
        if(detail.headerContentList != null && detail.headerContentList.size() > 0) {
            mPageList.add(new TopViewFirstFragment(mInfo.title, mInfo.des));
            for(SceneSpeakDetail.HeaderContent content : detail.headerContentList) {
                mPageList.add(new TopViewOtherFragment(content.context));
            }
            FragmentAdapter adapter = new FragmentAdapter(this);
            adapter.setFragmentList(mPageList);
            mViewPager.setAdapter(adapter);

            initIndications(mPageList.size());
            updateIndicationView(0);
        }

        if(detail.speakItemList != null && detail.speakItemList.size() > 0) {
            mList = detail.speakItemList;
            mAdapter = new SceneSpeakAdapter(this, mList, true);
            mListView.setAdapter(mAdapter);
        }

        mDetail = detail;
    }

    private void initIndications(int length) {
        for (int i = 0; i < length; i++){
            mIndicationViews.add(createImage());
        }
    }

    private ImageView createImage(){
        ImageView point = new ImageView(this);
        point.setImageResource(R.drawable.ic_indication_unselect);
        point.setPadding(0,0,ScreenUtil.dip2px(2),0);
        point.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIndicationsLayout.addView(point);
        return point;
    }

    private void updateIndicationView(int pos) {
        for (ImageView view : mIndicationViews) {
            view.setImageResource(R.drawable.ic_indication_unselect);
        }
        mIndicationViews.get(pos).setImageResource(R.drawable.ic_indication_selected);
    }

    private static final int WHAT_UPDATE_DURATION = 1;
    private static final int WHAT_HIDE_DURATION = 2;
    private static final int WHAT_UPDATE_PROGRESS = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case WHAT_UPDATE_DURATION:
                    setScrollDuration();
                    break;
                case WHAT_HIDE_DURATION:
                    removeMessages(WHAT_UPDATE_DURATION);
                    mScrollSelectLayout.setVisibility(View.GONE);
                    break;
                case WHAT_UPDATE_PROGRESS:
                    updateProgress();
                    if(mPlayer.isPlaying()) {
                        sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 1000);
                    }
                    break;
            }
        }
    };

    private void updateProgress() {
        int currentPositionMs = (int) mPlayer.getCurrentPosition();
        int index = mAdapter.getSelectIndex();
        if(mList.get(index).endTime*1000 < currentPositionMs && index < mList.size()-1) {
            mAdapter.setSelectIndex(index+1);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(index+1);
        }
        mProgressBar.updateProgress(currentPositionMs);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.scene_speak_back_iv) {
            finish();
        } else if(id == R.id.scene_speak_translate_iv) {
            mIsOpen = !mIsOpen;
            mTranslateIv.setImageResource(mIsOpen ? R.drawable.ic_translate_zh : R.drawable.ic_translate_en);
            mAdapter.setIsOpen(mIsOpen);
        } else if(id == R.id.scene_speak_btn_start_tv) {
            if(mDetail != null && !TextUtils.isEmpty(mDetail.audioSssKey)) {
                mPlayer.addMediaItem(MediaItem.fromUri(mDetail.audioSssKey));
                mPlayer.prepare();
                mPlayer.play();
                mAdapter.setSelectIndex(0);

                mPlayIv.setImageResource(R.drawable.ic_course_stop);
                mProgressBar.start((int) (mList.get(mList.size()-1).endTime*1000));
                mBottomStartLayout.setVisibility(View.GONE);
                mBottomPlayLayout.setVisibility(View.VISIBLE);
//                mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 1000);
            }
        } else if(id == R.id.scene_speak_jump_prev_iv) {
            int currentIndex = mAdapter.getSelectIndex();
            if(currentIndex <= 0) {
                return;
            }
            jumpToPosition(currentIndex-1);
        } else if(id == R.id.scene_speak_play_iv) {
            if(mPlayer.isPlaying()) {
                mPlayIv.setImageResource(R.drawable.ic_course_play);
                mPlayer.pause();
//                mHandler.removeMessages(WHAT_UPDATE_PROGRESS);
            } else {
                mPlayIv.setImageResource(R.drawable.ic_course_stop);
                mPlayer.play();
//                mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_PROGRESS, 200);
            }
        } else if(id == R.id.scene_speak_jump_next_iv) {
            int currentIndex = mAdapter.getSelectIndex();
            if(currentIndex >= mList.size()-1) {
                return;
            }
            jumpToPosition(currentIndex+1);
        }
    }

    private void jumpToPosition(int position) {
        if(position < 0 || position >= mList.size()) {
            return;
        }
        SceneSpeakDetail.SpeakItem item = mList.get(position);
        mAdapter.setSelectIndex(position);
        mAdapter.notifyDataSetChanged();
        mPlayer.seekTo((int)(item.startTime*1000));
        mListView.setSelection(position);
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
}
