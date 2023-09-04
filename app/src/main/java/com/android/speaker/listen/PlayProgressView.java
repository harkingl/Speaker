package com.android.speaker.listen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

/**
 * 带有进度条的播放/暂停按钮
 */
public class PlayProgressView extends View {
    private static final int DEFAULT_SIZE = 22;

    private Context mContext;
    private Bitmap mPlayBm;
    private Bitmap mPauseBm;
    private int mSize;
    private int mProgressWidth;
    private int mCirclePoint;
    private int mRadius;
    private int mBgColor;
    private int mBorderColor;
    private int mProgressColor;
    private Paint mBgPaint;
    private Paint mProgressPaint;
    private RectF mProgressRect;
    private float mStartAngle = 0;
    private float mEndAngle = 0;
    private Bitmap mBitmap;
    private Rect mBitmapRect;

    public PlayProgressView(Context context) {
        this(context, null);
    }
    public PlayProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PlayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init();
    }

    private void init() {
        mSize = ScreenUtil.dip2px(DEFAULT_SIZE);
        mProgressWidth = ScreenUtil.dip2px(2);
        mCirclePoint = (int)(mSize*0.5);
        mRadius = (int) (mSize*0.5-mProgressWidth);
        mBgColor = ContextCompat.getColor(mContext, R.color.white);
        mBorderColor = ContextCompat.getColor(mContext, R.color.text_color_CACACA);
        mProgressColor = ContextCompat.getColor(mContext, R.color.common_purple_color);

        int bmOffset = (int) (mSize*0.5*0.5);
        mBitmapRect = new Rect(0, 0, mSize, mSize);

        mPlayBm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_play);
        mPauseBm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_stop);
        mBitmap = mPlayBm;

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        int progressSize = (int) (mSize - mProgressWidth*0.5);
        mProgressRect = new RectF((float) (mProgressWidth*0.5), (float) (mProgressWidth*0.5), progressSize, progressSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(mBorderColor);
        canvas.drawCircle(mCirclePoint, mCirclePoint, (int)(mSize*0.5), p);
        canvas.drawCircle(mCirclePoint, mCirclePoint, mRadius, mBgPaint);
        canvas.drawBitmap(mBitmap, null, mBitmapRect, mBgPaint);
//        mProgressPaint.setColor(mBorderColor);
//        canvas.drawCircle(mCirclePoint, mCirclePoint, (int)(mSize*0.5), mProgressPaint);
        mProgressPaint.setColor(mProgressColor);
        canvas.drawArc(mProgressRect, -90, mEndAngle, false, mProgressPaint);
    }

    public void play() {
        mBitmap = mPauseBm;
        invalidate();
    }

    public void pause() {
        mBitmap = mPlayBm;
        invalidate();
    }

    public void updateProgress(int progress) {
        mEndAngle = mStartAngle + progress*360/100;
        LogUtil.d("test", "######updateProgress#######" + mStartAngle + " " + mEndAngle);
        invalidate();
    }
}
