package com.android.speaker.listen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.course.PlayProgressBar;
import com.chinsion.SpeakEnglish.R;

public class BlogDetailAiFragment extends BaseFragment implements View.OnClickListener {
    private ListView mListView;
    private View mScrollSelectLayout;
    private TextView mScrollDurationTv;
    private PlayProgressBar mProgressBar;
    private ImageView mJumpPrevIv;
    private ImageView mJumpNextIv;
    private ImageView mPlayIv;
    private BlogDetail mDetail;
    private IBlogDetailCallBack mCallback;

    public BlogDetailAiFragment(IBlogDetailCallBack callBack) {
        this.mCallback = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog_detail_ai, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        mListView = view.findViewById(R.id.ai_analysis_lv);
        mScrollSelectLayout = view.findViewById(R.id.ai_scroll_select_ll);
        mScrollDurationTv = view.findViewById(R.id.ai_scroll_duration_tv);
        mProgressBar = view.findViewById(R.id.ai_progress_bar);
        mJumpPrevIv = view.findViewById(R.id.ai_jump_prev_iv);
        mJumpNextIv = view.findViewById(R.id.ai_jump_next_iv);
        mPlayIv = view.findViewById(R.id.ai_play_iv);

        mJumpPrevIv.setOnClickListener(this);
        mJumpNextIv.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
    }

    public void setData(BlogDetail detail) {
        if(detail != null) {
            this.mDetail = detail;
        }
    }

    public void updateProgress(int currentPositionMs) {
//        int index = mAdapter.getSelectIndex();
//        if(mList.get(index).endTime*1000 < currentPositionMs && index < mList.size()-1) {
//            mAdapter.setSelectIndex(index+1);
//            mAdapter.notifyDataSetChanged();
//            if(mScrollSelectLayout.getVisibility() != View.VISIBLE) {
//                mListView.setSelection(index);
//            }
//        }
        mProgressBar.updateProgress(currentPositionMs);
    }

    @Override
    public void onClick(View v) {

    }
}
