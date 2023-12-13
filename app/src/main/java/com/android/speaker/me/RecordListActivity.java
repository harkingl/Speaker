package com.android.speaker.me;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.base.component.TitleTagView;
import com.chinsion.SpeakEnglish.R;

/**
 * 学习记录
 */
public class RecordListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RecordListActivity";
    private static final int TAB_INDEX_COURSE = 0;
    private static final int TAB_INDEX_BLOG = 1;

    private TitleBarLayout mTitleBarLayout;
    private TitleTagView mCourseTitleView;
    private TitleTagView mBlogTitleView;
    private FrameLayout mContainerFl;
    private CourseRecordListFragment mCourseFragment;
    private BlogRecordListFragment mBlogFragment;
    private int mCurrentTab = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.record_title_bar);
        mCourseTitleView = findViewById(R.id.record_title_course_tv);
        mBlogTitleView = findViewById(R.id.record_title_blog_tv);
        mContainerFl = findViewById(R.id.record_container_fl);

        mCourseTitleView.setOnClickListener(this);
        mBlogTitleView.setOnClickListener(this);
    }

    private void configTitleBar() {
        mTitleBarLayout.setTitle("学习记录", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        changeTag(TAB_INDEX_COURSE);
    }

    private void changeTag(int index) {
        if(mCurrentTab == index) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case TAB_INDEX_COURSE:
                mCourseTitleView.setColorBlockViewVisible(true);
                mBlogTitleView.setColorBlockViewVisible(false);
                if(mCourseFragment == null) {
                    mCourseFragment = new CourseRecordListFragment();
                    transaction.add(R.id.record_container_fl, mCourseFragment);
                }
                transaction.show(mCourseFragment);
                mCurrentTab = TAB_INDEX_COURSE;
                break;
            case TAB_INDEX_BLOG:
                mBlogTitleView.setColorBlockViewVisible(true);
                mCourseTitleView.setColorBlockViewVisible(false);
                if(mBlogFragment == null) {
                    mBlogFragment = new BlogRecordListFragment();
                    transaction.add(R.id.record_container_fl, mBlogFragment);
                }
                transaction.show(mBlogFragment);
                mCurrentTab = TAB_INDEX_BLOG;
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction tran) {
        if (null != mCourseFragment) {
            tran.hide(mCourseFragment);
        }
        if (null != mBlogFragment) {
            tran.hide(mBlogFragment);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mCourseTitleView) {
            changeTag(TAB_INDEX_COURSE);
        } else if(v == mBlogTitleView) {
            changeTag(TAB_INDEX_BLOG);
        }
    }
}
