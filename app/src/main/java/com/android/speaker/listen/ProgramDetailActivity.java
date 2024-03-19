package com.android.speaker.listen;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.bean.PagedListEntity;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 英语博客-节目详情
 */
public class ProgramDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProgramDetailActivity.class.getSimpleName();

    private static final int PAGE_SIZE = 20;

    private TitleBarLayout mTitleBarLayout;
    private ImageView mImgIv;
    private TextView mNameTv;
    private TextView mAuthorTv;
    private TextView mDesTv;
    private TextView mCategoryTv;
    private PagedItemListView mListView;
    private List<BlogItem> mList;
    private ProgramSingleListAdapter mAdapter;
    private ProgramItem mItem;
    private int mPageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.program_detail_title_bar);
        mImgIv = findViewById(R.id.program_detail_img_iv);
        mNameTv = findViewById(R.id.program_detail_name_tv);
        mAuthorTv = findViewById(R.id.program_detail_author_tv);
        mDesTv = findViewById(R.id.program_detail_des_tv);
        mCategoryTv = findViewById(R.id.program_detail_category_tv);
        mListView = findViewById(R.id.program_detail_lv);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
//        mTitleBarLayout.setTitle("场景连播", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mItem = (ProgramItem) getIntent().getSerializableExtra("programItem");
        mList = new ArrayList<>();

        mAdapter = new ProgramSingleListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setLoadMoreListener(new PagedItemListView.LoadMoreListener() {
            @Override
            public void OnLoadMore(int currentPage) {
                mPageNo = currentPage;
                getBlogList();
            }
        });

        setTopView();
//        getDetail();
        getBlogList();
    }

//    private void getDetail() {
//        new GetProgramDetailRequest(this, mId).schedule(false, new RequestListener<BlogDetail>() {
//            @Override
//            public void onSuccess(BlogDetail result) {
//                setTopView(result);
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                ToastUtil.toastLongMessage(e.getMessage());
//            }
//        });
//    }

    private void getBlogList() {
        new GetBlogListForCategoryRequest(this, mItem.id, mPageNo, PAGE_SIZE).schedule(false, new RequestListener<PagedListEntity<BlogItem>>() {
            @Override
            public void onSuccess(PagedListEntity<BlogItem> result) {
                mListView.setTotalPageNumber(result.getPageCount());
                mListView.setRecordCount(result.getRecordCount());
                mListView.onLoadDone();
//                mListView.setCurrentPage(result.getPageNo());
                if(result.getList() != null) {
                    mList.addAll(result.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(Throwable e) {
                mListView.onLoadFailed();
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setTopView() {
        GlideUtil.loadCornerImage(mImgIv, mItem.iconUrl, null, 10);
        mNameTv.setText(mItem.title);
        mAuthorTv.setText(mItem.author);
        mDesTv.setText(mItem.des);
        mCategoryTv.setText(mItem.category);
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        }
    }
}