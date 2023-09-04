package com.android.speaker.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

/***
 * 课程预览
 */
public class CoursePreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private ImageView mTopIv;
    private TextView mTitleTv;
    private TextView mDescTv;
    private FlowLayout mTagLayout;
    private TextView mWordsTv;
    private TextView mSceneTv;
    private TextView mCourseInstructionTv;
    private TextView mRolePlayTv;
    private TextView mStartTv;
    private CourseItem mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_preview);

        initView();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.preview_back_iv);
        mTopIv = findViewById(R.id.preview_top_img_iv);
        mTitleTv = findViewById(R.id.preview_title_tv);
        mDescTv = findViewById(R.id.preview_desc_tv);
        mTagLayout = findViewById(R.id.preview_tags_fl);
        mWordsTv = findViewById(R.id.preview_words_tv);
        mSceneTv = findViewById(R.id.preview_scenes_tv);
        mCourseInstructionTv = findViewById(R.id.preview_course_instruction_tv);
        mRolePlayTv = findViewById(R.id.preview_roleplay_tv);
        mStartTv = findViewById(R.id.preview_btn_start_tv);

        mBackIv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
    }

    private void initData() {
        mInfo = (CourseItem) getIntent().getSerializableExtra("course_item");

        if(!TextUtils.isEmpty(mInfo.homePage)) {
            GlideUtil.loadImage(mTopIv, mInfo.homePage, null);
        }
        mTitleTv.setText(mInfo.title);
        mDescTv.setText(mInfo.des);

        new GetCoursePreviewRequest(this, mInfo.id).schedule(false, new RequestListener<CoursePreviewInfo>() {
            @Override
            public void onSuccess(CoursePreviewInfo result) {
                if(result != null) {
                    setBottomView(result);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setBottomView(CoursePreviewInfo info) {
        if(info == null) {
            return;
        }
        if(info.tips != null && info.tips.length > 0) {
            mTagLayout.removeAllViews();
            for(String tagName : info.tips) {
                mTagLayout.addView(generateTagView(tagName));
            }
            mTagLayout.setVisibility(View.VISIBLE);
        }
        String words = info.words == null ? "" : TextUtils.join(", ", info.words);
        mWordsTv.setText("词汇：" + words);
        mSceneTv.setText("场景：" + info.sceneSpeak);
        mCourseInstructionTv.setText("语法点：" + info.sceneProject);
        mRolePlayTv.setText("场景描述：" + info.openSpeak);
    }

    private View generateTagView(String tagName) {
        View v = LayoutInflater.from(this).inflate(R.layout.preview_tag_item, null);
        TextView tv = v.findViewById(R.id.preview_tag_name_tv);
        tv.setText(tagName);

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.preview_back_iv) {
            finish();
        } else if(id == R.id.preview_btn_start_tv) {
            Intent i = new Intent(this, WordPracticeActivity.class);
            i.putExtra("id", mInfo.id);
            startActivity(i);
        }
    }
}
