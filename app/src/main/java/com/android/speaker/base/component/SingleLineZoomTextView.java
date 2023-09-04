package com.android.speaker.base.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class SingleLineZoomTextView extends TextView {

    private Paint mPaint;
    private float mTextSize;

    public SingleLineZoomTextView(Context context) {
        super(context);
    }
    public SingleLineZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SingleLineZoomTextView(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            mTextSize = this.getTextSize();
            mPaint = new Paint();
            mPaint.set(this.getPaint());
            int drawWid = 0;
            Drawable[] draws = getCompoundDrawables();
            for (int i = 0; i < draws.length; i++) {
                if(draws[i]!= null){
                    drawWid += draws[i].getBounds().width();
                }
            }
            //获得当前TextView的有效宽度
            int availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight()- getCompoundDrawablePadding()- drawWid;
            //所有字符所占像素宽度
            float textWidths = getTextLength(mTextSize, text);
            while(textWidths > availableWidth){
                mPaint.setTextSize(--mTextSize);
                textWidths = getTextLength(mTextSize, text);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
    }

    /**
     * @param textSize
     * @param text
     * @return 字符串所占像素宽度
     */
    private float getTextLength(float textSize,String text){
        mPaint.setTextSize(textSize);
        return mPaint.measureText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(getText().toString(), this.getWidth());
    }

}
