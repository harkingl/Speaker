package com.android.speaker.listen;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.util.GlideUtil;
import com.chinsion.SpeakEnglish.R;

public class BlogDetailVoiceFragment extends BaseFragment implements View.OnClickListener, BlogProgressBar.OnProgressChangedListener {
    private ImageView mTopIv;
    private TextView mTitleTv;
    private TextView mNameTv;
    private BlogProgressBar mProgressBar;
    private ImageView mJumpPrevIv;
    private ImageView mJumpNextIv;
    private ImageView mPlayIv;
    private TextView mInfoTv;
    private IBlogDetailCallBack mCallback;
    private BlogDetail mDetail;

    public BlogDetailVoiceFragment(IBlogDetailCallBack callBack) {
        this.mCallback = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog_detail_voice, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mTopIv = view.findViewById(R.id.voice_top_img_iv);
        mTitleTv = view.findViewById(R.id.voice_blog_title_tv);
        mNameTv = view.findViewById(R.id.voice_blog_name_tv);
        mProgressBar = view.findViewById(R.id.voice_progress_bar);
        mJumpPrevIv = view.findViewById(R.id.voice_jump_prev_iv);
        mJumpNextIv = view.findViewById(R.id.voice_jump_next_iv);
        mPlayIv = view.findViewById(R.id.voice_play_iv);
        mInfoTv = view.findViewById(R.id.voice_info_tv);

        mJumpPrevIv.setOnClickListener(this);
        mJumpNextIv.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
        mProgressBar.setOnProgressChangedListener(this);
    }

    public void setData(BlogDetail detail) {
        if(detail != null) {
            GlideUtil.loadCornerImage(mTopIv, detail.iconUrl, null, 10);
            mTitleTv.setText(detail.titleEn);
            if(!TextUtils.isEmpty(detail.name)) {
                mNameTv.setText(detail.name);
            }
            mProgressBar.start((int) (detail.audioDuration*60*1000));
            this.mDetail = detail;
        }
    }

    public void updateProgress(int currentPositionMs) {
        if(mProgressBar != null) {
            mProgressBar.updateProgress(currentPositionMs);
        }
    }

    public void play(boolean isPlay) {
        mPlayIv.setImageResource(isPlay ? R.drawable.ic_course_stop : R.drawable.ic_course_play);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.voice_jump_prev_iv:
                if(mCallback != null) {
                    mCallback.jumpPrev();
                }
                break;
            case R.id.voice_jump_next_iv:
                if(mCallback != null) {
                    mCallback.jumpNext();
                }
                break;
            case R.id.voice_play_iv:
                if(mCallback != null) {
                    mCallback.play();
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(int total, int current) {
        if(mCallback != null) {
            mCallback.seekTo(current);
        }
    }
}
