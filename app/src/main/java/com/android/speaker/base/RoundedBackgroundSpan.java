package com.android.speaker.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

public class RoundedBackgroundSpan extends ReplacementSpan {

    private static int CORNER_RADIUS = ScreenUtil.dip2px(5);
    private int backgroundColor = 0;
    private int textColor = 0;
    private float textSize;
    private Context mContext;
    private String placeHolder;

    public RoundedBackgroundSpan(Context context) {
        this(context, context.getResources().getColor(R.color.common_line_color), context.getResources().getColor(R.color.text_color_1), 16);
    }

    public RoundedBackgroundSpan(Context context, int backgroundColor, int textColor, int textSize) {
        super();
        this.mContext = context;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSize, context.getResources().getDisplayMetrics());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(x, top+2, x + measureText(paint, text, start, end), bottom-2);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        String content = text.subSequence(start, end).toString();
        if(!TextUtils.isEmpty(placeHolder) && placeHolder.equals(content.trim())) {
            paint.setColor(backgroundColor);
        } else {
            paint.setColor(textColor);
            canvas.drawText(text, start, end, x+20, y, paint);
        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(measureText(paint, text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end) + 40;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }
}
