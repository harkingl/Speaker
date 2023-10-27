package com.android.speaker.favorite;

import android.os.Bundle;
import android.view.View;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends BaseActivity implements View.OnClickListener {
    private static final int PAGE_SIZE = 20;
    private TitleBarLayout titleBarLayout;
    private PagedItemListView mListView;
    private FavoriteListAdapter mAdapter;
    private List<FavoriteItem> mList;
    private int mPageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.favorite_title_bar);
        mListView = findViewById(R.id.favorite_list_lv);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("收藏", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);

    }

    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new FavoriteListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        new GetFavoriteListRequest(this, mPageNo, PAGE_SIZE).schedule(false, new RequestListener<List<FavoriteItem>>() {
            @Override
            public void onSuccess(List<FavoriteItem> result) {
                mListView.onLoadDone();
                if(result != null) {
                    mList.addAll(result);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable e) {
                mListView.onLoadDone();
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        }
    }
}