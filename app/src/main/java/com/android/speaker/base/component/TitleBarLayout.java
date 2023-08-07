package com.android.speaker.base.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

public class TitleBarLayout extends LinearLayout implements ITitleBarLayout {

    private LinearLayout mLeftGroup;
    private LinearLayout mRightGroup;
    private TextView mLeftTitle;
    private TextView mCenterTitle;
    private TextView mRightTitle;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private LinearLayout mTitleLayout;

    public TitleBarLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        inflate(context, R.layout.title_bar_layout, this);
        mTitleLayout = findViewById(R.id.page_title_layout);
        mLeftGroup = findViewById(R.id.page_title_left_group);
        mRightGroup = findViewById(R.id.page_title_right_group);
        mLeftTitle = findViewById(R.id.page_title_left_text);
        mRightTitle = findViewById(R.id.page_title_right_text);
        mCenterTitle = findViewById(R.id.page_title);
        mLeftIcon = findViewById(R.id.page_title_left_icon);
        mRightIcon = findViewById(R.id.page_title_right_icon);

//        LayoutParams params = (LayoutParams) mTitleLayout.getLayoutParams();
//        params.height = ScreenUtil.getPxByDp(50);
//        mTitleLayout.setLayoutParams(params);

        int iconSize = ScreenUtil.dip2px(20);
        ViewGroup.LayoutParams iconParams = mLeftIcon.getLayoutParams();
        iconParams.width = iconSize;
        iconParams.height = iconSize;
        mLeftIcon.setLayoutParams(iconParams);
        iconParams = mRightIcon.getLayoutParams();
        iconParams.width = iconSize;
        iconParams.height = iconSize;

        mRightIcon.setLayoutParams(iconParams);

    }

    public void setLeftReturnListener(Context context) {
        mLeftGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(TitleBarLayout.this.getWindowToken(), 0);
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public void setOnLeftClickListener(OnClickListener listener) {
        mLeftGroup.setOnClickListener(listener);
    }

    @Override
    public void setOnRightClickListener(OnClickListener listener) {
        mRightGroup.setOnClickListener(listener);
    }

    @Override
    public void setTitle(String title, Position position) {
        switch (position) {
            case LEFT:
                mLeftTitle.setText(title);
                break;
            case RIGHT:
                mRightTitle.setText(title);
                break;
            case MIDDLE:
                mCenterTitle.setText(title);
                break;
        }
    }

    @Override
    public LinearLayout getLeftGroup() {
        return mLeftGroup;
    }

    @Override
    public LinearLayout getRightGroup() {
        return mRightGroup;
    }

    @Override
    public ImageView getLeftIcon() {
        return mLeftIcon;
    }

    @Override
    public void setLeftIcon(int resId) {
        mLeftIcon.setBackgroundResource(resId);
    }

    @Override
    public ImageView getRightIcon() {
        return mRightIcon;
    }

    @Override
    public void setRightIcon(int resId) {
        mRightIcon.setBackgroundResource(resId);
    }

    @Override
    public TextView getLeftTitle() {
        return mLeftTitle;
    }

    @Override
    public TextView getMiddleTitle() {
        return mCenterTitle;
    }

    @Override
    public TextView getRightTitle() {
        return mRightTitle;
    }
}
