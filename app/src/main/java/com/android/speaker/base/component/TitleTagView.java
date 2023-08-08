package com.android.speaker.base.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinsion.SpeakEnglish.R;

/**
 * Custom title tag view
 */
public class TitleTagView extends RelativeLayout {

    private String mName;
    protected TextView mNameText;

    public TitleTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_tag_view, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleTagView, 0, 0);
        try {
            mName = ta.getString(R.styleable.TitleTagView_name);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView() {
        mNameText = findViewById(R.id.tag_name_tv);
        mNameText.setText(mName);
    }
}
