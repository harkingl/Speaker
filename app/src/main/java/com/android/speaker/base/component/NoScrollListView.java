package com.android.speaker.base.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

public class NoScrollListView extends ListView {
	private int mMaxHeight;

	public NoScrollListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NoScrollListView(Context context) {
		this(context, null);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 默认最大高度为屏幕高度的0.6
		mMaxHeight = (int) (ScreenUtil.getScreenHeight(context)*0.6);

//		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ConstraintHeightListView, 0, defStyle);
//		try {
//			mMaxHeight = array.getDimension(R.styleable.ConstraintHeightListView_maxHeight, -1);
//		} finally {
//			array.recycle();
//		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取lv本身高度
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		//限制高度小于lv高度,设置为限制高度
		if (mMaxHeight <= specSize && mMaxHeight > -1) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(Float.valueOf(mMaxHeight).intValue(),
					MeasureSpec.AT_MOST);
		} else {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	public void setMaxHeight(int maxHeight) {
		if(maxHeight > 0) {
			this.mMaxHeight = maxHeight;
		}
	}
}
