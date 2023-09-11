package com.android.speaker.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.chinsion.SpeakEnglish.R;

public class TopViewFirstFragment extends BaseFragment {
    private TextView mTitleTv;
    private TextView mDescTv;
    private String mTitle;
    private String mDesc;
    public TopViewFirstFragment(String title, String desc) {
        this.mTitle = title;
        this.mDesc = desc;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_view_first, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mTitleTv = view.findViewById(R.id.view_title_tv);
        mDescTv = view.findViewById(R.id.view_desc_tv);
    }

    private void initData() {
        mTitleTv.setText(mTitle);
        mDescTv.setText(mDesc);
    }
}
