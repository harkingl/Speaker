package com.android.speaker.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class CourseRecordListFragment extends BaseFragment {
    private static final int PAGE_SIZE = 20;
    private PagedItemListView mListView;
    private ImageView mEmptyIv;
    private CourseRecordListAdapter mAdapter;
    private List<RecordInfo> mList;
    private int mPageNo = 1;
    private boolean mIsLoadMore = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_list, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        initData();
    }

    private void initView(View view) {
        mListView = view.findViewById(R.id.lv);
        mEmptyIv = view.findViewById(R.id.empty_iv);
    }

    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new CourseRecordListAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        mListView.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                mIsLoadMore = true;
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        new GetCourseRecordListRequest(getActivity(), mPageNo, PAGE_SIZE).schedule(false, new RequestListener<List<RecordInfo>>() {
            @Override
            public void onSuccess(List<RecordInfo> result) {
                mListView.onLoadDone();
                if(!mIsLoadMore) {
                    if(result == null || result.size() == 0) {
                        mEmptyIv.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        return;
                    }
                    mList.clear();
                }
                if(result != null) {
                    mList.addAll(result);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable e) {
                mListView.onLoadFailed();
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }
}
