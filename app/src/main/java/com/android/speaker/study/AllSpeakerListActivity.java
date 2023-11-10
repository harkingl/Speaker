package com.android.speaker.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollGridView;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.course.CategoryItem;
import com.android.speaker.course.GetCategoryListRequest;
import com.android.speaker.course.SceneCourseCategoryAdapter;
import com.android.speaker.course.SceneCourseChildCategoryAdapter;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 全部开口说页面
 */
public class AllSpeakerListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = AllSpeakerListActivity.class.getSimpleName();

    private static final int PAGE_SIZE = 20;

    private ImageView mBackIv;
    private NoScrollGridView mParentCategoryGv;
    private NoScrollListView mChildCategoryLv;
    private PagedItemListView mCourseLv;
    private SceneCourseCategoryAdapter mParentCategoryAdapter;
    private SceneCourseChildCategoryAdapter mChildCategoryAdapter;
    private SpeakerListAdapter mCourseAdapter;
    private List<OpenSpeakerInfo> mList;
    private CategoryItem mCurrSubItem;
    private int mPageNo = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.common_purple_color));
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_all_speaker_list);

        mBackIv = findViewById(R.id.speaker_list_back_iv);
        mParentCategoryGv = findViewById(R.id.parent_course_category_gv);
        mChildCategoryLv = findViewById(R.id.child_course_category_lv);
        mCourseLv = findViewById(R.id.speaker_list_lv);

        mBackIv.setOnClickListener(this);
    }

    private void initData() {
        mChildCategoryAdapter = new SceneCourseChildCategoryAdapter(this, new ArrayList<>());
        mChildCategoryLv.setAdapter(mChildCategoryAdapter);
        mChildCategoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChildCategoryAdapter.setSelectIndex(position);
                mChildCategoryAdapter.notifyDataSetChanged();
                mCurrSubItem = (CategoryItem)mChildCategoryAdapter.getItem(position);
                loadCourseList(true);
            }
        });
        mList = new ArrayList<>();
        mCourseAdapter = new SpeakerListAdapter(this, mList);
        mCourseLv.setAdapter(mCourseAdapter);
        mCourseLv.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                loadCourseList(false);
            }
        });

        new GetCategoryListRequest(this).schedule(false, new RequestListener<List<CategoryItem>>() {
            @Override
            public void onSuccess(List<CategoryItem> result) {
                if(result != null && result.size() > 0) {
                    initParentCategoryGv(result);
                    loadChildCategoryLv(result.get(0));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void initParentCategoryGv(List<CategoryItem> categoryList) {
        if(categoryList == null || categoryList.size() == 0) {
            return;
        }
        // item之间的间隔
        int scenePadding = ScreenUtil.dip2px(15);
        int sceneItemWidth = ScreenUtil.dip2px(60);

        int sceneGridviewWidth = sceneItemWidth*categoryList.size() + scenePadding*(categoryList.size()-1);
        LinearLayout.LayoutParams sceneParams = new LinearLayout.LayoutParams(
                sceneGridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mParentCategoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mParentCategoryAdapter.setSelectIndex(position);
                mParentCategoryAdapter.notifyDataSetChanged();
                loadChildCategoryLv(mParentCategoryAdapter.getItems().get(position));
            }
        });
        mParentCategoryGv.setLayoutParams(sceneParams);
        mParentCategoryGv.setNumColumns(categoryList.size());
        mParentCategoryGv.setColumnWidth(sceneItemWidth);
        mParentCategoryGv.setHorizontalSpacing(scenePadding);
        mParentCategoryGv.setVerticalSpacing(scenePadding);
        mParentCategoryGv.setStretchMode(GridView.NO_STRETCH);
        mParentCategoryAdapter = new SceneCourseCategoryAdapter(this, categoryList);
        mParentCategoryGv.setAdapter(mParentCategoryAdapter);
    }

    private void loadChildCategoryLv(CategoryItem parentItem) {
        if(parentItem != null) {
            mChildCategoryAdapter.setData(parentItem.subList);
            if(parentItem.subList != null && parentItem.subList.size() > 0) {
                mChildCategoryAdapter.setSelectIndex(0);
                mCurrSubItem = parentItem.subList.get(0);
                loadCourseList(true);
            }
        }
    }

    private void loadCourseList(boolean isRefresh) {
        if(mCurrSubItem == null) {
            return;
        }
        if(isRefresh) {
            mPageNo = 1;
            mList.clear();
        }
        new GetSpeakerListBySubRequest(this, mPageNo, PAGE_SIZE, mCurrSubItem.id).schedule(false, new RequestListener<List<OpenSpeakerInfo>>() {
            @Override
            public void onSuccess(List<OpenSpeakerInfo> result) {
                mCourseLv.onLoadDone();
//                mCourseLv.setCurrentPage(result.pageNo);
//                mCourseLv.setTotalPageNumber(result.pageCount);
                if(result != null) {
                    mList.addAll(result);
                }
                mCourseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable e) {
                mCourseLv.onLoadFailed();
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.speaker_list_back_iv:
                finish();
                break;
        }
    }
}
