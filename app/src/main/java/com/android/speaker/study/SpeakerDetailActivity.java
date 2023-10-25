package com.android.speaker.study;

import android.content.Intent;
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
import com.android.speaker.course.CourseUtil;
import com.android.speaker.course.SpeakChatActivity;
import com.android.speaker.listen.GetSpeakerDetailRequest;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.StringUtil;
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
    private TextView mStartTv;
    private NoScrollListView mListView;
    private SpeakerDetailListAdapter mAdapter;
    private List<ExampleInfo> mList;
    private SpeakerDetailInfo mInfo;

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
        mStartTv = findViewById(R.id.detail_btn_start_tv);

        mBackIv.setOnClickListener(this);
        mHistoryIv.setOnClickListener(this);
        mStartTv.setOnClickListener(this);
    }

    private void initData() {
        String id = getIntent().getStringExtra("open_speak_id");

        mList = new ArrayList<>();
        mAdapter = new SpeakerDetailListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        new GetSpeakerDetailRequest(this, id).schedule(false, new RequestListener<SpeakerDetailInfo>() {
            @Override
            public void onSuccess(SpeakerDetailInfo result) {
                setView(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(SpeakerDetailInfo info) {
        if(info == null) {
            return;
        }
        this.mInfo = info;

        if(!TextUtils.isEmpty(info.ossUrl)) {
            GlideUtil.loadImage(mTopIv, info.ossUrl, null);
        }
        mTitleTv.setText(info.title);

        if(!TextUtils.isEmpty(info.sceneDes)) {
            String unescapedString = StringUtil.unescapeString(info.sceneDes.substring(2, info.sceneDes.length()-2));
            mSimulationContentTv.setText(Html.fromHtml(unescapedString));
        }
        if(info.exampleInfoList != null) {
            mList.addAll(info.exampleInfoList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.detail_back_iv) {
            finish();
        } else if(id == R.id.detail_btn_start_tv) {
            Intent i = new Intent(this, SpeakChatActivity.class);
            i.putExtra(CourseUtil.KEY_SPEAK_DETAIL, mInfo);
            startActivity(i);
            finish();
        }
    }
}
