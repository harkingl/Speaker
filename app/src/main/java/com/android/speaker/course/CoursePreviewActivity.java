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
import com.android.speaker.favorite.AddCourseFavoriteRequest;
import com.android.speaker.favorite.RemoveBlogFavoriteRequest;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.study.SpeakerDetailActivity;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import org.apmem.tools.layouts.FlowLayout;

/***
 * 课程预览
 */
public class CoursePreviewActivity extends BaseActivity implements View.OnClickListener {

    private View mTopLayout;
    private ImageView mBackIv;
    private ImageView mStarIv;
    private TextView mTitleTv;
    private TextView mDescTv;
    private FlowLayout mTagLayout;
    private TextView mWordsTv;
    private TextView mSceneTitleTv;
    private TextView mSceneDescTv;
    private TextView mCourseInstructionTv;
    private TextView mRolePlayTv;
    private TextView mStartTv;
    private View mWordsLayout;
    private View mSceneLayout;
    private View mCourseInstructionLayout;
    private View mRolePlayLayout;
    private boolean mIsFavorite = false;
    private String mFavoriteId;
    private int mType;
    private String mOpenSpeakId;
    private String mId;
    private CoursePreviewInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_preview);

        initView();
        initData();
    }

    private void initView() {
        mTopLayout = findViewById(R.id.preview_top_rl);
        mBackIv = findViewById(R.id.preview_back_iv);
        mStarIv = findViewById(R.id.preview_followup_iv);
        mTitleTv = findViewById(R.id.preview_title_tv);
        mDescTv = findViewById(R.id.preview_desc_tv);
        mTagLayout = findViewById(R.id.preview_tags_fl);
        mWordsTv = findViewById(R.id.preview_words_tv);
        mSceneTitleTv = findViewById(R.id.preview_scenes_title_tv);
        mSceneDescTv = findViewById(R.id.preview_scenes_desc_tv);
        mCourseInstructionTv = findViewById(R.id.preview_course_instruction_tv);
        mRolePlayTv = findViewById(R.id.preview_roleplay_tv);
        mStartTv = findViewById(R.id.preview_btn_start_tv);
        mWordsLayout = findViewById(R.id.preview_words_ll);
        mSceneLayout = findViewById(R.id.preview_scenes_ll);
        mCourseInstructionLayout = findViewById(R.id.preview_course_instruction_ll);
        mRolePlayLayout = findViewById(R.id.preview_roleplay_ll);

        mBackIv.setOnClickListener(this);
        mStarIv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
        mWordsLayout.setOnClickListener(this);
        mSceneLayout.setOnClickListener(this);
        mCourseInstructionLayout.setOnClickListener(this);
        mRolePlayLayout.setOnClickListener(this);
    }

    private void initData() {
        mId = getIntent().getStringExtra(CourseUtil.KEY_COURSE_ID);
        mType = getIntent().getIntExtra(CourseUtil.KEY_COURSE_TYPE, CourseUtil.TYPE_COURSE_CATALOG);

        new GetCoursePreviewRequest(this, mId).schedule(false, new RequestListener<CoursePreviewInfo>() {
            @Override
            public void onSuccess(CoursePreviewInfo result) {
                if(result != null) {
                    setView(result);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(CoursePreviewInfo info) {
        if(info == null) {
            return;
        }
        if(!TextUtils.isEmpty(info.homePage)) {
            GlideUtil.loadTargetView(mTopLayout, info.homePage);
        }
        mTitleTv.setText(info.title);
        mDescTv.setText(info.des);
        if(!TextUtils.isEmpty(info.favoritesId) && !"null".equals(info.favoritesId)) {
            mIsFavorite = true;
            mFavoriteId = info.favoritesId;
            mStarIv.setImageResource(R.drawable.ic_star_white_select);
        }
        mOpenSpeakId = info.openSpeakId;

        if(info.tips != null && info.tips.length > 0) {
            mTagLayout.removeAllViews();
            for(String tagName : info.tips) {
                mTagLayout.addView(generateTagView(tagName));
            }
            mTagLayout.setVisibility(View.VISIBLE);
        }
        String words = info.words == null ? "" : TextUtils.join(", ", info.words);
        mWordsTv.setText("词汇：" + words);
        mSceneTitleTv.setText(info.sceneSpeakTitle);
        mSceneDescTv.setText("场景：" + info.sceneSpeak);
        mCourseInstructionTv.setText("语法点：" + info.sceneProject);
        mRolePlayTv.setText("场景描述：" + info.openSpeak);

        this.mInfo = info;
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
            i.putExtra("id", mId);
            startActivity(i);
        } else if(id == R.id.preview_words_ll) {
            Intent i = new Intent(this, WordPracticeActivity.class);
            i.putExtra("id", mId);
            startActivity(i);
        } else if(id == R.id.preview_course_instruction_ll) {
            if(mInfo != null) {
                Intent i = new Intent(this, CourseLectureDetailActivity.class);
                i.putExtra(CourseUtil.KEY_COURSE_PREVIEW_INFO, mInfo);
                startActivity(i);
            }
        } else if(id == R.id.preview_scenes_ll) {
            if(mInfo != null) {
                Intent i = new Intent(this, SceneSpeakActivity.class);
                i.putExtra(CourseUtil.KEY_COURSE_PREVIEW_INFO, mInfo);
                startActivity(i);
            }
        } else if(id == R.id.preview_followup_iv) {
            addBlog();
        } else if(id == R.id.preview_roleplay_ll) {
            Intent intent = new Intent(this, SpeakerDetailActivity.class);
            intent.putExtra("open_speak_id", mOpenSpeakId);
            startActivity(intent);
        }
    }

    private void addBlog() {
        if(mIsFavorite) {
            new RemoveBlogFavoriteRequest(this, mFavoriteId).schedule(false, new RequestListener<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
//                    ToastUtil.toastLongMessage("取消收藏成功");
                    mIsFavorite = false;
                    mStarIv.setImageResource(R.drawable.ic_star_white);
                }

                @Override
                public void onFailed(Throwable e) {
                    ToastUtil.toastLongMessage(e.getMessage());
                }
            });
        } else {
            new AddCourseFavoriteRequest(this, mId, mType).schedule(false, new RequestListener<String>() {
                @Override
                public void onSuccess(String result) {
                    ToastUtil.toastLongMessage("收藏成功");
                    mIsFavorite = true;
                    mFavoriteId = result;
                    mStarIv.setImageResource(R.drawable.ic_star_white_select);
                }

                @Override
                public void onFailed(Throwable e) {
                    ToastUtil.toastLongMessage(e.getMessage());
                }
            });
        }
    }
}
