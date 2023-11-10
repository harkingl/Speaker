package com.android.speaker.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.PagedItemListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.chat.audio.Audio;
import com.android.speaker.course.WordInfo;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class NewWordListActivity extends BaseActivity implements View.OnClickListener, NewWordListAdapter.ICallBack {
    private static final String TAG = "NewWordListActivity";
    private static final int PAGE_SIZE = 20;
    private TitleBarLayout titleBarLayout;
    private PagedItemListView mListView;
    private TextView mRemoveTv;
    private NewWordListAdapter mAdapter;
    private List<WordInfo> mList;
    private int mPageNo = 1;
    private Audio mAudio;
    // 当前播放的item
    private WordInfo mWordInfo;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.new_word_title_bar);
        mListView = findViewById(R.id.new_word_list_lv);
        mRemoveTv = findViewById(R.id.new_word_remove_tv);
        mRemoveTv.setOnClickListener(this);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setImageResource(R.drawable.ic_delete);
        titleBarLayout.setTitle("生词本", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
        titleBarLayout.setOnRightClickListener(this);
    }

    private void initData() {
        mAudio = Audio.getInstance(this);
        mAudio.setPlayListener(new Audio.PlayListener() {
            @Override
            public void onFinishPlay() {
                if(mWordInfo != null) {
                    mWordInfo.isPlaying = false;
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e(TAG, e.getMessage());

                if(mWordInfo != null) {
                    mWordInfo.isPlaying = false;
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mList = new ArrayList<>();
        mAdapter = new NewWordListAdapter(this, mList, this);
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
        new GetNewWordListRequest(this, mPageNo, PAGE_SIZE).schedule(false, new RequestListener<List<WordInfo>>() {
            @Override
            public void onSuccess(List<WordInfo> result) {
                mListView.onLoadDone();
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

//        new GetWordsRequest(this, "40").schedule(false, new RequestListener<List<WordInfo>>() {
//            @Override
//            public void onSuccess(List<WordInfo> result) {
//                if(result != null && result.size() > 0) {
//                    mList.addAll(result);
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                ToastUtil.toastLongMessage(e.getMessage());
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == titleBarLayout.getRightGroup()) {
            onClickRightGroup();
        } else if(v == mRemoveTv) {
            onRemove();
        }
    }

    private void onRemove() {
        List<WordInfo> selectList = mAdapter.getSelectItems();

        if(selectList.size() == 0) {
            ToastUtil.toastLongMessage("请选中要勾选的生词");
            return;
        }

        List<String> idList = new ArrayList<>();
        for(WordInfo info : selectList) {
            idList.add(info.id);
        }

        new RemoveNewWordRequest(this, idList).schedule(true, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mList.removeAll(selectList);

                onClickRightGroup();
                refresh();
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void refresh() {
        mPageNo = 1;
        mList.clear();
        loadData();
    }

    @Override
    public void doPlay(WordInfo info) {
        if(info != null) {
            mAudio.startPlayer(info.audioUrl);
            info.isPlaying = true;
            mWordInfo = info;

            mAdapter.notifyDataSetChanged();
        }
    }

    private void onClickRightGroup() {
        isEdit = !isEdit;
        if(isEdit) {
            titleBarLayout.getRightIcon().setVisibility(View.GONE);
            titleBarLayout.getRightTitle().setText("取消");
            mRemoveTv.setVisibility(View.VISIBLE);
        } else {
//            titleBarLayout.getRightIcon().setImageResource(R.drawable.ic_delete);
            titleBarLayout.getRightIcon().setVisibility(View.VISIBLE);
            titleBarLayout.getRightTitle().setText("");
            mRemoveTv.setVisibility(View.GONE);
        }
        mAdapter.setIsEdit(isEdit);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mAudio.release();
        super.onDestroy();
    }
}