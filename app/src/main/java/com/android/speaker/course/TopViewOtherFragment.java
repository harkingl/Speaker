package com.android.speaker.course;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.chinsion.SpeakEnglish.R;

public class TopViewOtherFragment extends BaseFragment {
    private TextView mContentTv;
    private String mContent;
    public TopViewOtherFragment(String content) {
        this.mContent = content;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_view_other, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mContentTv = view.findViewById(R.id.view_content_tv);
    }

    private void initData() {
        mContentTv.setText(Html.fromHtml(mContent));
    }
}
