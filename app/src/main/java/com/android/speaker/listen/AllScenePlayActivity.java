package com.android.speaker.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class AllScenePlayActivity extends BaseActivity implements View.OnClickListener {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private TitleBarLayout mTitleBarLayout;
    private GridView mScenePlayGv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_scene_play);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.scene_play_title_bar);
        mScenePlayGv = findViewById(R.id.scene_play_gv);

        mScenePlayGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScenePlayItem item = (ScenePlayItem) mScenePlayGv.getItemAtPosition(i);
                if(item != null) {
                    gotoPlayPage(item);
                }
            }
        });
    }

    private void gotoPlayPage(ScenePlayItem item) {
        Intent i = new Intent(this, ScenePlayListActivity.class);
        i.putExtra("scene_play_id", item.id);
        i.putExtra("scene_play_title", item.title);
        startActivity(i);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
        mTitleBarLayout.setTitle("场景连播", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        new GetScenePlayListRequest(this, 1, 100).schedule(false, new RequestListener<List<ScenePlayItem>>() {
            @Override
            public void onSuccess(List<ScenePlayItem> result) {
                if(result != null && result.size() > 0) {
                    mScenePlayGv.setAdapter(new ScenePlayAdapter(AllScenePlayActivity.this, result));
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
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        }
    }
}
