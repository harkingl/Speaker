package com.android.speaker.course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.speaker.util.TimeUtil;
import com.chinsion.SpeakEnglish.R;

/**
 * 带有播放时间的进度条
 */
public class PlayProgressBar extends RelativeLayout {
    private ProgressBar mProgressBar;
    private ImageView mSlideIv;
    private TextView mLeftTimeTv;
    private TextView mRightTimeTv;
    private Context mContext;
    private LayoutParams mSlideParams;
    private OnProgressChangedListener mListener;
    private int mDurationMs;
    private float mMaxSlidingWidth = -1;

    public PlayProgressBar(Context context) {
        this(context, null);
    }
    public PlayProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PlayProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(mMaxSlidingWidth == -1) {
            mMaxSlidingWidth = mProgressBar.getWidth() - mSlideIv.getWidth();
        }
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_play_progress_bar, null);
        mProgressBar = view.findViewById(R.id.view_progress_bar);
        mSlideIv = view.findViewById(R.id.view_slide_iv);
        mLeftTimeTv = view.findViewById(R.id.view_left_time_tv);
        mRightTimeTv = view.findViewById(R.id.view_right_time_tv);

        mSlideParams = (LayoutParams) mSlideIv.getLayoutParams();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(view, params);

//        mSlideIv.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        refreshView((int) event.getX());
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void refreshView(int x) {
        mSlideParams.leftMargin += x;
        int currentDuration = (int) ((float)mSlideParams.leftMargin/mMaxSlidingWidth*mDurationMs);
        mProgressBar.setProgress(currentDuration);
        if(mListener != null) {
            mListener.onProgressChanged(mDurationMs, currentDuration);
        }
    }

    public void start(int durationMs) {
        mLeftTimeTv.setText("0:00");
        mRightTimeTv.setText(TimeUtil.timeToString(durationMs/1000));
        mProgressBar.setMax(durationMs);
        this.mDurationMs = durationMs;
    }

    public void updateProgress(int currentPositionMs) {
        mProgressBar.setProgress(currentPositionMs);
        mLeftTimeTv.setText(TimeUtil.timeToString(currentPositionMs/1000));

        mSlideParams.leftMargin = (int) ((float)currentPositionMs/mDurationMs*mMaxSlidingWidth);
        requestLayout();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int total, int current);
    }
}
