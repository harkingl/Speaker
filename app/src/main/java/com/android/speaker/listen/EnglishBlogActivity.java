package com.android.speaker.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class EnglishBlogActivity extends BaseActivity implements View.OnClickListener {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private TitleBarLayout mTitleBarLayout;
    private ListView mRecommendLv;
    private GridView mProgramGv;
    private GridView mHotGv;
    private ProgramListAdapter mProgramAdapter;
    private HotBlogListAdapter mHotBlogAdapter;
    private BlogListAdapter mRecommendBlogAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_blog);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.blog_title_bar);
        mRecommendLv = findViewById(R.id.blog_recommend_lv);
        mProgramGv = findViewById(R.id.blog_program_gv);
        mHotGv = findViewById(R.id.blog_hot_gv);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
        mTitleBarLayout.setTitle("英语博客", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
//        // item之间的间隔
//        int itemPadding = ScreenUtil.dip2px(15);
//        // item宽度
//        int itemWidth = ScreenUtil.getScreenWidth(getActivity())*150/393;
//        // 计算GridView宽度
//        int gridviewWidth = itemWidth*3 + itemPadding*2;
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//        mSceneCourseGv.setLayoutParams(params);
//        mSceneCourseGv.setColumnWidth(itemWidth);
//        mSceneCourseGv.setHorizontalSpacing(itemPadding);
//        mSceneCourseGv.setVerticalSpacing(itemPadding);
//        mSceneCourseGv.setStretchMode(GridView.NO_STRETCH);
//
//        new GetRecommendCourseListRequest(getActivity()).schedule(false, new RequestListener<List<CategoryItem>>() {
//            @Override
//            public void onSuccess(List<CategoryItem> result) {
//                if(result != null && result.size() > 0) {
//                    initCategoryGv(result);
//                    loadCourseList(result.get(0));
//                }
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                ToastUtil.toastLongMessage(e.getMessage());
//            }
//        });

        loadRecommendBlogList();
        loadProgramList();
        loadHotBlogList();
    }

    private void loadRecommendBlogList() {
        new GetBlogListRequest(this, 1, DEFAULT_PAGE_SIZE).schedule(false, new RequestListener<List<BlogItem>>() {
            @Override
            public void onSuccess(List<BlogItem> result) {
                if(result != null && result.size() > 0) {
                    mRecommendBlogAdapter = new BlogListAdapter(EnglishBlogActivity.this, result);
                    mRecommendLv.setAdapter(mRecommendBlogAdapter);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void loadProgramList() {
        new GetProgramListRequest(this, 1, DEFAULT_PAGE_SIZE).schedule(false, new RequestListener<List<ProgramItem>>() {
            @Override
            public void onSuccess(List<ProgramItem> result) {
                if(result != null && result.size() > 0) {
                    initProgramGv(result);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void loadHotBlogList() {
        new GetHotBlogListRequest(this, 1, DEFAULT_PAGE_SIZE).schedule(false, new RequestListener<List<BlogItem>>() {
            @Override
            public void onSuccess(List<BlogItem> result) {
                if(result != null && result.size() > 0) {
                    initHotBlogGv(result);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void initProgramGv(List<ProgramItem> list) {
        if(list == null || list.size() == 0) {
            return;
        }
        // item之间的间隔
        int itemPadding = ScreenUtil.dip2px(15);
        int itemWidth = ScreenUtil.dip2px(130);

        int sceneGridviewWidth = itemWidth*list.size() + itemPadding*(list.size()-1);
        LinearLayout.LayoutParams sceneParams = new LinearLayout.LayoutParams(
                sceneGridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mProgramGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EnglishBlogActivity.this, ProgramDetailActivity.class);
                intent.putExtra("programItem", ((ProgramItem)mProgramAdapter.getItem(position)));
                startActivity(intent);
            }
        });
        mProgramGv.setLayoutParams(sceneParams);
        mProgramGv.setNumColumns(list.size());
        mProgramGv.setColumnWidth(itemWidth);
        mProgramGv.setHorizontalSpacing(itemPadding);
        mProgramGv.setVerticalSpacing(itemPadding);
        mProgramGv.setStretchMode(GridView.NO_STRETCH);
        mProgramAdapter = new ProgramListAdapter(this, list, itemWidth);
        mProgramGv.setAdapter(mProgramAdapter);
    }

    private void initHotBlogGv(List<BlogItem> list) {
        if(list == null || list.size() == 0) {
            return;
        }
        int columnCount = list.size()/3;
        if(list.size()%3 != 0) {
            columnCount += 1;
        }
        // item之间的间隔
        int itemPadding = ScreenUtil.dip2px(15);
        int itemVerticalPadding = ScreenUtil.dip2px(20);
        int itemWidth = ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(55);

        int sceneGridviewWidth = itemWidth*columnCount + itemPadding*(columnCount-1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                sceneGridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mHotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoBlogDetailPage(((BlogItem)mHotBlogAdapter.getItem(position)));
            }
        });
        mHotGv.setLayoutParams(params);
        mHotGv.setNumColumns(columnCount);
        mHotGv.setColumnWidth(itemWidth);
        mHotGv.setHorizontalSpacing(itemPadding);
        mHotGv.setVerticalSpacing(itemVerticalPadding);
        mHotGv.setStretchMode(GridView.NO_STRETCH);
        mHotBlogAdapter = new HotBlogListAdapter(this, list, columnCount);
        mHotGv.setAdapter(mHotBlogAdapter);
    }

    private void gotoBlogDetailPage(BlogItem item) {
        Intent i = new Intent(this, BlogDetailActivity.class);
        i.putExtra("blog_item", item);
        this.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        }
    }
}
