package com.android.speaker.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.bean.PagedListEntity;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.course.ChatReportActivity;
import com.android.speaker.course.CourseUtil;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChatHistoryListActivity";

    private static final int PAGE_SIZE = 20;
    private TitleBarLayout titleBarLayout;
    private PagedItemListView mListView;
    private ChatHistoryListAdapter mAdapter;
    private List<ChatReportInfo> mList;
    private int mPageNo = 1;
    private boolean mIsLoadMore = false;
    private String mSceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.chat_history_title_bar);
        mListView = findViewById(R.id.chat_history_list_lv);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("对话历史", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mSceneId = getIntent().getStringExtra(CourseUtil.KEY_SCENE_ID);

        mList = new ArrayList<>();
        mAdapter = new ChatHistoryListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                mIsLoadMore = true;
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
        new GetChatHistoryListRequest(this, mSceneId, mPageNo, PAGE_SIZE).schedule(false, new RequestListener<PagedListEntity<ChatReportInfo>>() {
            @Override
            public void onSuccess(PagedListEntity<ChatReportInfo> result) {
                mListView.setTotalPageNumber(result.getPageCount());
                mListView.setRecordCount(result.getRecordCount());
                mListView.onLoadDone();
                if(!mIsLoadMore) {
                    mList.clear();
                }
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

    private void gotoDetail(ChatReportInfo info) {
        Intent i = new Intent(this, ChatReportActivity.class);
        i.putExtra(CourseUtil.KEY_CHAT_REPORT_INFO, info);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        }
    }

    private void refresh() {
        mPageNo = 1;
        mIsLoadMore = false;
        loadData();
    }
}