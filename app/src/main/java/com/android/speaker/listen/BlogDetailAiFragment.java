package com.android.speaker.listen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.course.AnalysisItem;
import com.android.speaker.course.AnalysisListAdapter;
import com.android.speaker.course.PlayProgressBar;
import com.android.speaker.util.TimeUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class BlogDetailAiFragment extends BaseFragment implements View.OnClickListener, PlayProgressBar.OnProgressChangedListener {
    private ListView mListView;
    private View mScrollSelectLayout;
    private TextView mScrollDurationTv;
    private PlayProgressBar mProgressBar;
    private ImageView mJumpPrevIv;
    private ImageView mJumpNextIv;
    private ImageView mPlayIv;
    private BlogDetail mDetail;
    private IBlogDetailCallBack mCallback;
    private List<AnalysisItem> mList;
    private AnalysisListAdapter mAdapter;
    private boolean mIsOpen;
    private int mCurrSelectPosition = -1;

    public BlogDetailAiFragment(IBlogDetailCallBack callBack) {
        this.mCallback = callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog_detail_ai, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mListView = view.findViewById(R.id.ai_analysis_lv);
        mScrollSelectLayout = view.findViewById(R.id.ai_scroll_select_ll);
        mScrollDurationTv = view.findViewById(R.id.ai_scroll_duration_tv);
        mProgressBar = view.findViewById(R.id.ai_progress_bar);
        mJumpPrevIv = view.findViewById(R.id.ai_jump_prev_iv);
        mJumpNextIv = view.findViewById(R.id.ai_jump_next_iv);
        mPlayIv = view.findViewById(R.id.ai_play_iv);

        mJumpPrevIv.setOnClickListener(this);
        mJumpNextIv.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
        mProgressBar.setOnProgressChangedListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mCallback != null) {
                    mCallback.jumpToPosition(position);
                }
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeMessages(WHAT_HIDE_DURATION);
                        mScrollSelectLayout.setVisibility(View.VISIBLE);
                        setScrollDuration();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_DURATION, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(WHAT_HIDE_DURATION, 2000);
                        break;
                }
                return false;
            }
        });
    }

    public void setData(BlogDetail detail) {
        if(detail != null) {
            this.mDetail = detail;
            this.mList = detail.list;
            mAdapter = new AnalysisListAdapter(getActivity(), mList, mIsOpen);
            mListView.setAdapter(mAdapter);
            mProgressBar.start((int) (detail.audioDuration*60*1000));
        }
    }

    public void setTranslateOpen(boolean isOpen) {
        this.mIsOpen = isOpen;
        mAdapter.setIsOpen(isOpen);
        mAdapter.notifyDataSetChanged();
    }

    public void play(boolean isPlay) {
        mPlayIv.setImageResource(isPlay ? R.drawable.ic_course_stop : R.drawable.ic_course_play);
    }

    public void updateProgress(int currentPositionMs) {
        if(mProgressBar != null) {
            mProgressBar.updateProgress(currentPositionMs);
        }
    }

    public void setSelectIndex(int index) {
        if(index < 0 || index >= mList.size()) {
            return;
        }
        mAdapter.setSelectIndex(index);
        mAdapter.notifyDataSetChanged();
        if(mScrollSelectLayout.getVisibility() != View.VISIBLE && index > 0) {
            mListView.setSelection(index -1);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ai_jump_prev_iv:
                if(mCallback != null) {
                    mCallback.jumpPrev();
                }
                break;
            case R.id.ai_jump_next_iv:
                if(mCallback != null) {
                    mCallback.jumpNext();
                }
                break;
            case R.id.ai_play_iv:
                if(mCallback != null) {
                    mCallback.play();
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(int total, int current) {
        if(mCallback != null) {
            mCallback.seekTo(current);
        }
    }

    private void setScrollDuration() {
        View firstItem = mListView.getChildAt(0);
        int scrollSelectViewTop = mScrollSelectLayout.getTop();
        int position = -1;
        if(firstItem.getBottom() > scrollSelectViewTop) {
            position = ((AnalysisListAdapter.ViewHolder)firstItem.getTag()).position;
        } else {
            for(int i = 1; i < mListView.getChildCount(); i++) {
                View itemView = mListView.getChildAt(i);
                if(itemView.getBottom() >= scrollSelectViewTop) {
                    position = ((AnalysisListAdapter.ViewHolder)itemView.getTag()).position;
                    break;
                }
            }
        }
        if(position != -1 && position != mCurrSelectPosition) {
            mCurrSelectPosition = position;
            mScrollDurationTv.setText(TimeUtil.timeToString((int)(mList.get(position).startTime)));
        }
    }

    private static final int WHAT_UPDATE_DURATION = 1;
    private static final int WHAT_HIDE_DURATION = 2;
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
            }
        }
    };
}
