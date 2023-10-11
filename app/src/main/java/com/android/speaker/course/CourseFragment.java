package com.android.speaker.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends BaseFragment implements View.OnClickListener {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private TextView mSceneMoreTv;
    private GridView mSceneTypeGv;
    private GridView mSceneCourseGv;
    private ListView mQualityLv;
    private ListView mSpecialLv;
    private CategoryAdapter mSceneTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mSceneMoreTv = view.findViewById(R.id.course_scene_more_tv);
        mSceneTypeGv = view.findViewById(R.id.course_scene_type_gv);
        mSceneCourseGv = view.findViewById(R.id.course_scene_course_gv);
        mQualityLv = view.findViewById(R.id.course_quality_lv);
        mSpecialLv = view.findViewById(R.id.course_special_lv);

        mSceneMoreTv.setOnClickListener(this);
    }

    private void initData() {
        // item之间的间隔
        int itemPadding = ScreenUtil.dip2px(15);
        // item宽度
        int itemWidth = ScreenUtil.getScreenWidth(getActivity())*150/393;
        // 计算GridView宽度
        int gridviewWidth = itemWidth*3 + itemPadding*2;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mSceneCourseGv.setLayoutParams(params);
        mSceneCourseGv.setColumnWidth(itemWidth);
        mSceneCourseGv.setHorizontalSpacing(itemPadding);
        mSceneCourseGv.setVerticalSpacing(itemPadding);
        mSceneCourseGv.setStretchMode(GridView.NO_STRETCH);

        new GetCategoryListRequest(getActivity()).schedule(false, new RequestListener<List<CategoryItem>>() {
            @Override
            public void onSuccess(List<CategoryItem> result) {
                if(result != null && result.size() > 0) {
                    initCategoryGv(result);
                    loadCourseList(result.get(0));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });

        loadQualityCourseList();

        loadSpecialCourseList();
    }

    private void initCategoryGv(List<CategoryItem> categoryList) {
        if(categoryList == null || categoryList.size() == 0) {
            return;
        }
        // item之间的间隔
        int scenePadding = ScreenUtil.dip2px(10);
        int sceneItemWidth = ScreenUtil.dip2px(90);

//        String[] names = {"职场生活", "职业技能", "求职面试", "日常生活", "出国旅游", "教育培训"};
//        List<SceneTypeItem> list = new ArrayList<>();
//        for(int i = 0; i < 6; i++) {
//            SceneTypeItem item = new SceneTypeItem();
//            item.name = names[i];
//            list.add(item);
//        }
        int sceneGridviewWidth = sceneItemWidth*categoryList.size() + scenePadding*(categoryList.size()-1);
        LinearLayout.LayoutParams sceneParams = new LinearLayout.LayoutParams(
                sceneGridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mSceneTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSceneTypeAdapter.setSelectIndex(position);
                mSceneTypeAdapter.notifyDataSetChanged();
                loadCourseList(mSceneTypeAdapter.getItems().get(position));
            }
        });
        mSceneTypeGv.setLayoutParams(sceneParams);
        mSceneTypeGv.setNumColumns(categoryList.size());
        mSceneTypeGv.setColumnWidth(sceneItemWidth);
        mSceneTypeGv.setHorizontalSpacing(scenePadding);
        mSceneTypeGv.setVerticalSpacing(scenePadding);
        mSceneTypeGv.setStretchMode(GridView.NO_STRETCH);
        mSceneTypeAdapter = new CategoryAdapter(getActivity(), categoryList);
        mSceneTypeGv.setAdapter(mSceneTypeAdapter);
    }

    private void loadCourseList(CategoryItem item) {
        new GetCourseListByMainRequest(getActivity(), 1, 6, item.id).schedule(false, new RequestListener<List<CourseItem>>() {
            @Override
            public void onSuccess(List<CourseItem> result) {
                if(result == null) {
                    result = new ArrayList<>();
                }
//                List<CourseItem> courseList = new ArrayList<>();
//                for(int i = 0; i < 6; i++) {
//                    CourseItem item = new CourseItem();
//                    item.type = 0;
//                    item.title = "title" + i;
//                    item.des = "会议要迟到了怎么办？如 何在途中向别人解";
//                    item.homePage = "https://dummyimage.com/250/ff00ff/000000";
//                    courseList.add(item);
//                }
                CourseAdapter adapter = new CourseAdapter(getActivity(), result, CourseUtil.TYPE_COURSE_PROJECT);
                mSceneCourseGv.setAdapter(adapter);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void loadQualityCourseList() {
        new GetQualityCourseListRequest(getActivity(), 1, DEFAULT_PAGE_SIZE).schedule(false, new RequestListener<List<CourseItem>>() {
            @Override
            public void onSuccess(List<CourseItem> result) {
                if(result != null && result.size() > 0) {
                    mQualityLv.setAdapter(new QualityCourseAdapter(getActivity(), result, CourseUtil.TYPE_COURSE_CATALOG));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void loadSpecialCourseList() {
        new GetSpecialCourseListRequest(getActivity(), 1, DEFAULT_PAGE_SIZE).schedule(false, new RequestListener<List<CourseItem>>() {
            @Override
            public void onSuccess(List<CourseItem> result) {
                if(result != null && result.size() > 0) {
                    mSpecialLv.setAdapter(new QualityCourseAdapter(getActivity(), result, CourseUtil.TYPE_COURSE_SPECIAL));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.course_scene_more_tv) {
            startActivity(new Intent(getActivity(), SceneCourseActivity.class));
        }
    }
}
