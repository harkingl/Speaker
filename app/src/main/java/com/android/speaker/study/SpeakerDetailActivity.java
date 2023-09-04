package com.android.speaker.study;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.listen.GetSpeakerDetailRequest;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 开口说详情
 */
public class SpeakerDetailActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarLayout mTitleBarLayout;
    private ImageView mBackIv;
    private ImageView mHistoryIv;
    private ImageView mTopIv;
    private TextView mTitleTv;
    private TextView mDescTv;
    private TextView mSimulationContentTv;
    private NoScrollListView mListView;
    private SpeakerDetailListAdapter mAdapter;
    private List<ExampleInfo> mList;
    private OpenSpeakerInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_speaker_detail);

        initView();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.detail_back_iv);
        mHistoryIv = findViewById(R.id.detail_hostory_iv);
        mTopIv = findViewById(R.id.detail_top_img_iv);
        mTitleTv = findViewById(R.id.detail_title_tv);
        mDescTv = findViewById(R.id.detail_desc_tv);
        mSimulationContentTv = findViewById(R.id.detail_simulation_content_tv);
        mListView = findViewById(R.id.detail_list_lv);
//        mListView.setMaxHeight(ScreenUtil.dip2px(240));

        mBackIv.setOnClickListener(this);
        mHistoryIv.setOnClickListener(this);
    }

    private void initData() {
        mInfo = (OpenSpeakerInfo) getIntent().getSerializableExtra("speaker_info");

        if(!TextUtils.isEmpty(mInfo.ossUrl)) {
            GlideUtil.loadImage(mTopIv, mInfo.ossUrl, null);
        }
        mTitleTv.setText(mInfo.title);

        mList = new ArrayList<>();
        mAdapter = new SpeakerDetailListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        new GetSpeakerDetailRequest(this, mInfo.id).schedule(false, new RequestListener<SpeakerDetailInfo>() {
            @Override
            public void onSuccess(SpeakerDetailInfo result) {
                if(!TextUtils.isEmpty(result.sceneDes)) {
//                    mDescTv.setText(Html.fromHtml(result.sceneDes));
                    mSimulationContentTv.setText(Html.fromHtml(getResources().getString(R.string.sample)));
                }
                if(result.exampleInfoList != null) {
                    mList.addAll(result.exampleInfoList);
                    mAdapter.notifyDataSetChanged();
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
        if(id == R.id.detail_back_iv) {
            finish();
        }
    }
}
