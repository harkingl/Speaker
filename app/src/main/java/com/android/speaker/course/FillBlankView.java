package com.android.speaker.course;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpanWatcher;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 填空题
 */
public class FillBlankView extends RelativeLayout {
    private TextView mTextView;
    private Context mContext;
    private SpannableStringBuilder mSpannableBuilder;
    private List<String> mAnswerList;
    private List<AnswerRange> mAnswerRangeList;

    public FillBlankView(Context context) {
        this(context, null);
    }
    public FillBlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FillBlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.view_fill_blank, this);

        mTextView = (TextView) findViewById(R.id.content_tv);
    }

    public void setData(String originContent, List<String> answerList) {
        if (TextUtils.isEmpty(originContent) || answerList == null
                || answerList.isEmpty()) {
            return;
        }

        mSpannableBuilder = new SpannableStringBuilder(originContent);
        mAnswerList = answerList;

        mAnswerRangeList = new ArrayList<>();
        for(int i = 0; i < originContent.length(); i++) {
            int indexStart = originContent.indexOf("{");
            int indexEnd = originContent.indexOf("}");
            if(indexStart >= 0 && indexEnd >= 0) {
                AnswerRange range = new AnswerRange();
                range.startIndex = indexStart;
                range.endIndex = indexEnd;
                mAnswerRangeList.add(range);
                mSpannableBuilder.replace(indexStart, indexEnd, getBlankContent(indexStart, indexEnd));

//                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4DB6AC"));
                BackgroundColorSpan colorSpan = new BackgroundColorSpan(getResources().getColor(R.color.common_line_color));
                mSpannableBuilder.setSpan(colorSpan, indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                i = indexEnd+1;
            }
        }

        // 设置下划线颜色
//        for (AnswerRange range : rangeList) {
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4DB6AC"));
//            content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        // 设置填空处点击事件
//        for (int i = 0; i < rangeList.size(); i++) {
//            AnswerRange range = rangeList.get(i);
//            BlankClickableSpan blankClickableSpan = new BlankClickableSpan(i);
//            content.setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        // 设置此方法后，点击事件才能生效
//        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        mTextView.setText(mSpannableBuilder);
    }

    private String getBlankContent(int start, int end) {
        String s = "";
        for(int i = start; i <= end; i++) {
            s += " ";
        }

        return s;
    }

    class AnswerRange {
        public int startIndex;
        public int endIndex;
    }
}
