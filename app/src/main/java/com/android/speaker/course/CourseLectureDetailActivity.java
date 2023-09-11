package com.android.speaker.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.home.FragmentAdapter;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 课程预览
 */
public class CourseLectureDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private ImageView mTranslateIv;
    private ImageView mNoteIv;
    private ViewPager2 mViewPager;
    private LinearLayout mIndicationsLayout;
    private NoScrollListView mListView;
    private TextView mStartTv;
    private CourseItem mInfo;
    private List<ImageView> mIndicationViews = new ArrayList<>();
    private List<BaseFragment> mPageList = new ArrayList<>();
    private AnalysisListAdapter mAdapter;
    private boolean mIsOpen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_lecture_detail);

        initView();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.lecture_back_iv);
        mTranslateIv = findViewById(R.id.lecture_translate_iv);
        mNoteIv = findViewById(R.id.lecture_note_iv);
        mViewPager = findViewById(R.id.lecture_view_pager);
        mIndicationsLayout = findViewById(R.id.lecture_indications_ll);
        mListView = findViewById(R.id.lecture_analysis_lv);
        mStartTv = findViewById(R.id.lecture_btn_start_tv);
        mListView.setMaxHeight(ScreenUtil.getScreenHeight(this));

        mBackIv.setOnClickListener(this);
        mTranslateIv.setOnClickListener(this);
        mNoteIv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                updateIndicationView(position);
            }
        });
    }

    private void initData() {
        mInfo = (CourseItem) getIntent().getSerializableExtra("course_item");

        mTranslateIv.setImageResource(R.drawable.ic_translate_zh);

        new GetCourseLectureDetailRequest(this, mInfo.id).schedule(false, new RequestListener<CourseLectureDetail>() {
            @Override
            public void onSuccess(CourseLectureDetail result) {
                setView(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(CourseLectureDetail detail) {
        if(detail.headerContentList != null && detail.headerContentList.size() > 0) {
            mPageList.add(new TopViewFirstFragment(mInfo.title, mInfo.des));
            for(CourseLectureDetail.HeaderContent content : detail.headerContentList) {
                mPageList.add(new TopViewOtherFragment(content.context));
            }
            FragmentAdapter adapter = new FragmentAdapter(this);
            adapter.setFragmentList(mPageList);
            mViewPager.setAdapter(adapter);

            initIndications(mPageList.size());
            updateIndicationView(0);
        }

        if(detail.analysisItemList != null && detail.analysisItemList.size() > 0) {
            mAdapter = new AnalysisListAdapter(this, detail.analysisItemList, true);
            mListView.setAdapter(mAdapter);
        }
    }

    private void initIndications(int length) {
        for (int i = 0; i < length; i++){
            mIndicationViews.add(createImage());
        }
    }

    private ImageView createImage(){
        ImageView point = new ImageView(this);
        point.setImageResource(R.drawable.ic_indication_unselect);
        point.setPadding(0,0,ScreenUtil.dip2px(2),0);
        point.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIndicationsLayout.addView(point);
        return point;
    }

    private void updateIndicationView(int pos) {
        for (ImageView view : mIndicationViews) {
            view.setImageResource(R.drawable.ic_indication_unselect);
        }
        mIndicationViews.get(pos).setImageResource(R.drawable.ic_indication_selected);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.lecture_back_iv) {
            finish();
        } else if(id == R.id.lecture_translate_iv) {
            mIsOpen = !mIsOpen;
            mTranslateIv.setImageResource(mIsOpen ? R.drawable.ic_translate_zh : R.drawable.ic_translate_en);
            mAdapter.setIsOpen(mIsOpen);
        } else if(id == R.id.preview_btn_start_tv) {
            Intent i = new Intent(this, WordPracticeActivity.class);
            i.putExtra("id", mInfo.id);
            startActivity(i);
        }
    }
}
