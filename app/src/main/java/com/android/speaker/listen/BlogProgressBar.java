package com.android.speaker.listen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.TimeUtil;
import com.chinsion.SpeakEnglish.R;

/**
 * 博客详情页面进度条
 */
public class BlogProgressBar extends RelativeLayout {
    private ColumnProgressView mProgressBar;
    private View mSlideView;
    private TextView mLeftTimeTv;
    private TextView mRightTimeTv;
    private Context mContext;
    private LayoutParams mSlideParams;
    private OnProgressChangedListener mListener;
    private int mDurationMs;
    private float mMaxSlidingWidth = -1;

    public BlogProgressBar(Context context) {
        this(context, null);
    }
    public BlogProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public BlogProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(mMaxSlidingWidth == -1) {
            mMaxSlidingWidth = mProgressBar.getWidth();// - mSlideView.getWidth() - ScreenUtil.dip2px(10);
        }
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_blog_progress_bar, null);
        mProgressBar = view.findViewById(R.id.view_progress_bar);
        mSlideView = view.findViewById(R.id.view_slide_iv);
        mLeftTimeTv = view.findViewById(R.id.view_left_time_tv);
        mRightTimeTv = view.findViewById(R.id.view_right_time_tv);
        View progressLayout = view.findViewById(R.id.view_progress_rl);

        mSlideParams = (LayoutParams) mSlideView.getLayoutParams();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(view, params);

        progressLayout.setOnTouchListener(new OnTouchListener() {
            private float lastX = -1;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        refreshView((int) (event.getX()-lastX));
                        lastX = event.getX();
                        break;
                }
                return true;
            }
        });
    }

    private void refreshView(int x) {
        mSlideParams.leftMargin += x;
        if(mSlideParams.leftMargin <= -30) {
            mSlideParams.leftMargin = -30;
        } else if(mSlideParams.leftMargin >= mMaxSlidingWidth) {
            mSlideParams.leftMargin = (int) mMaxSlidingWidth;
        }
//        mSlideView.scrollBy(x, 0);
        mSlideView.setLayoutParams(mSlideParams);
        int currentDuration = (int) ((float)(mSlideParams.leftMargin+30)/mMaxSlidingWidth*mDurationMs);
        mProgressBar.setProgress((float)(mSlideParams.leftMargin+30)/mMaxSlidingWidth);
        if(mListener != null) {
            mListener.onProgressChanged(mDurationMs, currentDuration);
        }
    }

    public void start(int durationMs) {
        mLeftTimeTv.setText("0:00");
        mRightTimeTv.setText(TimeUtil.timeToString(durationMs/1000));
        this.mDurationMs = durationMs;
    }

    public void updateProgress(int currentPositionMs) {
        mProgressBar.setProgress((float)currentPositionMs/mDurationMs);
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
