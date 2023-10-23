package com.android.speaker.chat.audio;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * 录音动画
 */

public class AudioAnimationView extends View {
    private static final int DEFAULT_COUNT = 9;

    public static final int VOICE_TYPE_MIN = 0;
    public static final int VOICE_TYPE_MID = 1;
    public static final int VOICE_TYPE_MAX = 2;

    private final int height = 60;
    private final int minWidth = 60;
    private final int midWidth = 120;
    private final int maxWidth = 180;

    private Paint mPaint;
    private DrawFilter mDrawFilter;
    private int paintColor = getResources().getColor(R.color.black);

    private ArrayList<Float> voices;
    private ArrayList<Boolean> voicesAdd;

    private int mTotalHeight;
    private boolean needRunning = false;
    private int voicesType = VOICE_TYPE_MIN;
    private float columnWidth;

    public AudioAnimationView(Context context) {
        this(context, null);
    }

    public AudioAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void init() {
        columnWidth = ScreenUtil.dip2px(2);
        voices = new ArrayList<>();
        mPaint = new Paint();
        // 去除画笔锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(paintColor);
        // 设置风格为实线
        mPaint.setStyle(Paint.Style.FILL);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioAnimalView);
        if (typedArray != null) {
            paintColor = typedArray.getColor(R.styleable.AudioAnimalView_paint_color, getResources().getColor(R.color.black));
            voicesType = typedArray.getColor(R.styleable.AudioAnimalView_animation_lenght, VOICE_TYPE_MIN);
            typedArray.recycle();
        }
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        needRunning = true;
        postInvalidate();
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        needRunning = false;
    }

    private boolean flag = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);

        float firstHeight = flag ? voices.get(0)*1.1f : voices.get(0)*0.9f;
        if(firstHeight > mTotalHeight) {
            firstHeight = mTotalHeight;
        }
        canvas.drawRect(0, (mTotalHeight-firstHeight) / 2, columnWidth, (mTotalHeight+firstHeight) / 2, mPaint);
        for (int i = 1; i < voices.size(); i++) {
            float startX = columnWidth*2*i;
            float itemHeight = flag ? voices.get(i)*1.1f : voices.get(i)*0.9f;
            if(itemHeight > mTotalHeight) {
                itemHeight = mTotalHeight;
            }
            canvas.drawRect(startX, (mTotalHeight-itemHeight) / 2, startX + columnWidth,
                    (mTotalHeight+itemHeight) / 2, mPaint);
        }
        if (needRunning) {
            postInvalidateDelayed(100);
        }
    }

    public void updateVoiceValue(float voiceValue) {
        Random random = new Random();
        voiceValue = random.nextInt(12000);
        float height = columnWidth;
        if (voiceValue < 100.0) {
            height = columnWidth;
        } else if (voiceValue >= 100.0 && voiceValue < 400) {
            height = mTotalHeight*0.2f;
        } else if (voiceValue >= 400.0 && voiceValue < 1600) {
            height = mTotalHeight*0.3f;
        } else if (voiceValue >= 1600.0 && voiceValue < 3500) {
            height = mTotalHeight*0.4f;
        } else if (voiceValue >= 3500.0 && voiceValue < 7000) {
            height = mTotalHeight*0.5f;
        } else if (voiceValue >= 7000.0 && voiceValue < 10000) {
            height = mTotalHeight*0.6f;
        } else if (voiceValue > 10000.0 && voiceValue < 12000) {
            height = mTotalHeight*0.8f;
        } else if (voiceValue >= 12000.0) {
            height = mTotalHeight;
        }
        voices.add(0, height);
        voices.remove(voices.size()-1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalHeight = h;
        if(voices.size() == 0) {
//            for(int i = 0; i < DEFAULT_COUNT; i++) {
//                voices.add(mTotalHeight*0.2f);
//            }
            voices.add(mTotalHeight*0.3f);
            voices.add(mTotalHeight*0.8f);
            voices.add(mTotalHeight*1f);
            voices.add(mTotalHeight*0.2f);
            voices.add(mTotalHeight*0.4f);
            voices.add(mTotalHeight*0.6f);
            voices.add(mTotalHeight*0.1f);
            voices.add(mTotalHeight*0.3f);
            voices.add(mTotalHeight*0.2f);
        }
    }
}
