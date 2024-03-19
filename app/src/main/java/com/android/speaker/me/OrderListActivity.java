package com.android.speaker.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.bean.PagedListEntity;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OrderListActivity";

    private static final int REQ_DETAIL = 22;
    private static final int PAGE_SIZE = 20;
    private TitleBarLayout titleBarLayout;
    private PagedItemListView mListView;
    private OrderListAdapter mAdapter;
    private List<OrderInfo> mList;
    private int mPageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.order_title_bar);
        mListView = findViewById(R.id.order_list_lv);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("我的订单", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mList = new ArrayList<>();
        mAdapter = new OrderListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                loadData();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoDetail(mList.get(position));
            }
        });

        loadData();
    }

    private void loadData() {
        new GetOrderListRequest(this, mPageNo, PAGE_SIZE).schedule(false, new RequestListener<PagedListEntity<OrderInfo>>() {
            @Override
            public void onSuccess(PagedListEntity<OrderInfo> result) {
                mListView.setTotalPageNumber(result.getPageCount());
                mListView.setRecordCount(result.getRecordCount());
                mListView.onLoadDone();
                if(result.getList() != null) {
                    mList.addAll(result.getList());
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

    private void gotoDetail(OrderInfo info) {
//        Intent i = new Intent(this, NoteDetailActivity.class);
//        i.putExtra("note_info", info);
//        startActivityForResult(i, REQ_DETAIL);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_DETAIL && resultCode == RESULT_OK) {
            refresh();
        }
    }

    private void refresh() {
        mPageNo = 1;
        mList.clear();
        loadData();
    }
}