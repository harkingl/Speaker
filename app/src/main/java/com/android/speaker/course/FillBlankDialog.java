package com.android.speaker.course;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.speaker.base.RoundedBackgroundSpan;
import com.chinsion.SpeakEnglish.R;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class FillBlankDialog extends Dialog implements View.OnClickListener{
    private static final String PLACEHOLDER_TEXT = "******";
    private TextView mContentTv;
    private FlowLayout mTagLayout;
    private TextView mCommitTv;
    private View mBottomLayout;
    private TextView mAnswerAnalysisTv;
    private TextView mContinueTv;
    private TextView mSkipTv;
    private Context mContext;
    private SpannableStringBuilder mSpannableBuilder;
    private List<String> mSelectList;
    private List<String> mAnswerList;
    private List<AnswerRange> mAnswerRangeList;
    private List<String> mMySelectList = new ArrayList<>();

    private QuestionListener mListener;
    private QuestionInfo mInfo;
    public FillBlankDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_fill_blank);
        mContentTv = findViewById(R.id.dialog_content_tv);
        mTagLayout = findViewById(R.id.dialog_tags_fl);
        mCommitTv = findViewById(R.id.dialog_btn_commit_tv);
        mBottomLayout = findViewById(R.id.dialog_bottom_ll);
        mAnswerAnalysisTv = findViewById(R.id.dialog_answer_analysis_tv);
        mContinueTv = findViewById(R.id.dialog_btn_continue_tv);
        mSkipTv = findViewById(R.id.dialog_skip_tv);

        mCommitTv.setOnClickListener(this);
        mAnswerAnalysisTv.setOnClickListener(this);
        mContinueTv.setOnClickListener(this);
        mSkipTv.setOnClickListener(this);
    }

    public void setData(QuestionInfo info) {
        if (info == null) {
            return;
        }
        mInfo = info;

        String text = info.question;
        mAnswerRangeList = new ArrayList<>();
        for(int i = 0; i < info.answerList.size(); i++) {
            String placeHolder = "{{%" + (i+1) + "}}";
            int index = text.indexOf(placeHolder);
            if(index >= 0) {
                AnswerRange range = new AnswerRange();
                range.startIndex = index;
                range.endIndex = index + PLACEHOLDER_TEXT.length();
                range.blankCount = PLACEHOLDER_TEXT.length();

                mAnswerRangeList.add(range);
                text = text.replace(placeHolder, PLACEHOLDER_TEXT);
            }
        }

        mSpannableBuilder = new SpannableStringBuilder(text + "\n");
        mSelectList = info.questionSelect;
        mAnswerList = info.answerList;

        for(AnswerRange range : mAnswerRangeList) {
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4DB6AC"));
            RoundedBackgroundSpan span = new RoundedBackgroundSpan(mContext);
            span.setPlaceHolder(PLACEHOLDER_TEXT);
            mSpannableBuilder.setSpan(span, range.startIndex, range.endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
        mContentTv.setText(mSpannableBuilder);

        setTagView();
    }

    private void setTagView() {
        if(mSelectList == null || mSelectList.size() == 0) {
            return;
        }
        mTagLayout.removeAllViews();
        for(String tagName : mSelectList) {
            mTagLayout.addView(generateTagView(tagName));
        }
    }

    private View generateTagView(String tagName) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tag_fill_blank_item, null);
        TextView tv = v.findViewById(R.id.tag_name_tv);
        tv.setText(tagName);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()) {
                    v.setSelected(false);
                    int index = mMySelectList.indexOf(tagName);
                    if(index != -1) {
                        mMySelectList.set(index, "");
                        fillAnswer("", index);
                    }
                } else {
                    v.setSelected(true);
                    int index = 0;
                    for(String text : mMySelectList) {
                       if(TextUtils.isEmpty(text)) {
                           break;
                       }
                       index++;
                    }
                    if(index >= mAnswerRangeList.size()) {
                        return;
                    }
                    if(index >= mMySelectList.size()) {
                        mMySelectList.add(tagName);
                    } else {
                        mMySelectList.set(index, tagName);
                    }
                    fillAnswer(tagName, index);
                    if(index == mAnswerRangeList.size()-1) {
                        mSkipTv.setVisibility(View.GONE);
                        mCommitTv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return v;
    }

    private void fillAnswer(String answer, int position) {
        if(position >= mAnswerRangeList.size()) {
            return;
        }
        // 替换答案
        AnswerRange range = mAnswerRangeList.get(position);
        int newEndIndex = range.endIndex;
        if(TextUtils.isEmpty(answer)) {
            mSpannableBuilder.replace(range.startIndex, range.endIndex, PLACEHOLDER_TEXT);
            newEndIndex = range.startIndex + PLACEHOLDER_TEXT.length();
        } else {
            mSpannableBuilder.replace(range.startIndex, range.endIndex, answer);
            newEndIndex = range.startIndex + answer.length();
        }

        int difference = newEndIndex - range.endIndex;
        range.endIndex = newEndIndex;

        // 更新内容
        mContentTv.setText(mSpannableBuilder);

        for (int i = position+1; i < mAnswerRangeList.size(); i++) {
            // 获取下一个答案原来的范围
            AnswerRange oldNextRange = mAnswerRangeList.get(i);

            // 更新下一个答案的范围
            oldNextRange.startIndex += difference;
            oldNextRange.endIndex += difference;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dialog_btn_commit_tv:
                onCommit();
                break;
            case R.id.dialog_answer_analysis_tv:
                if(mListener != null) {
                    mListener.answerAnalysis(mInfo.answerAnalysis);
                }
                dismiss();
                break;
            case R.id.dialog_btn_continue_tv:
            case R.id.dialog_skip_tv:
                if(mListener != null) {
                    mListener.onContinue();
                }
                dismiss();
                break;
        }
    }

    private void onCommit() {
        mCommitTv.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.VISIBLE);

        for(int i = 0; i < mMySelectList.size(); i++) {
            if(i >= mAnswerList.size()) {
                break;
            }
            AnswerRange range = mAnswerRangeList.get(i);
            int green = mContext.getResources().getColor(R.color.common_green_color);
            int white = mContext.getResources().getColor(R.color.white);
            int red = mContext.getResources().getColor(R.color.common_red_color);

            int backgroundColor = mMySelectList.get(i).equals(mAnswerList.get(i)) ? green : red;
            mSpannableBuilder.setSpan(new RoundedBackgroundSpan(mContext, backgroundColor, white, 16), range.startIndex, range.endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        mContentTv.setText(mSpannableBuilder);

//        if(mListener != null) {
//            mListener.onCommit();
//        }
    }

    public void setListener(QuestionListener mListener) {
        this.mListener = mListener;
    }

    class AnswerRange {
        public int startIndex;
        public int endIndex;
        public int blankCount;
    }
}
