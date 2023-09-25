package com.android.speaker.listen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 柱状进度条
 */
public class ColumnProgressView extends View {
    private Context mContext;
    private int mDefaultColor;
    private int mProgressColor;
    private Paint mBgPaint;
    private Paint mProgressPaint;
    private int mWidth = -1;
    private int mHeight = -1;
    private int mColumnWidth = -1;
    private float mPadding = -1;
    private int mMaxHeight = -1;
    private List<Integer> mHeightList;
    private float mProgress;

    public ColumnProgressView(Context context) {
        this(context, null);
    }
    public ColumnProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ColumnProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(mWidth == -1 || mHeight == -1) {
            mWidth = getWidth();
            mHeight = getHeight();
        }
        if(mHeightList == null && right > 0) {
            int maxHeight = bottom-top-ScreenUtil.dip2px(12);
            int size = ScreenUtil.dip2px(8);
            int count = (int) ((mWidth+mPadding)/size);
            // 修正padding
            if((mWidth+mPadding)%size != 0) {
                mPadding = (float) (mWidth-mColumnWidth*count)/(count-1);
            }
            mHeightList = new ArrayList<>();
            Random random = new Random();
            for(int i = 0; i < count; i++) {
                int item = random.nextInt(maxHeight-30);
                mHeightList.add(item+30);
            }
        }
    }

    private void init() {
        mDefaultColor = ContextCompat.getColor(mContext, R.color.text_color_CACACA);
        mProgressColor = ContextCompat.getColor(mContext, R.color.common_green_color);

        mColumnWidth = ScreenUtil.dip2px(5);
        mPadding = ScreenUtil.dip2px(3);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mDefaultColor);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(mProgressColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mHeightList == null || mHeightList.size() == 0) {
            return;
        }

        float startX = 0;
        float progressMaxWidth = mProgress*mWidth;
        int bottomPadding = ScreenUtil.dip2px(6);
        for(int height : mHeightList) {
            canvas.drawRoundRect((float)startX, (float)mHeight-height-bottomPadding, (float)(startX+mColumnWidth), (float)mHeight-bottomPadding, 5, (float)height/8, mBgPaint);
            if(startX+mColumnWidth < progressMaxWidth) {
                canvas.drawRoundRect((float)startX, (float)mHeight-height-bottomPadding, (float)(startX+mColumnWidth), (float)mHeight-bottomPadding, 5, (float)height/8, mProgressPaint);
            }
            startX += mColumnWidth + mPadding;
        }
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}
