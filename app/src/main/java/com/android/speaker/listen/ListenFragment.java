package com.android.speaker.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.NoScrollGridView;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class ListenFragment extends BaseFragment implements View.OnClickListener {
    private TextView mPodcastMoreTv;
    private NoScrollListView mPodcastLv;
    private TextView mSceneMoreTv;
    private NoScrollGridView mSceneGv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listen, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mPodcastMoreTv = view.findViewById(R.id.listen_podcast_more_tv);
        mPodcastLv = view.findViewById(R.id.listen_podcast_lv);
        mSceneMoreTv = view.findViewById(R.id.listen_scene_more_tv);
        mSceneGv = view.findViewById(R.id.listen_scene_gv);

        mPodcastMoreTv.setOnClickListener(this);
        mSceneMoreTv.setOnClickListener(this);
        mSceneGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScenePlayItem item = (ScenePlayItem) mSceneGv.getItemAtPosition(i);
                if(item != null) {
                    gotoPlayPage(item);
                }
            }
        });
        mPodcastLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogItem item = (BlogItem) mPodcastLv.getItemAtPosition(position);
                if(item != null) {
                    gotoBlogDetailPage(item);
                }
            }
        });
    }

    private void initData() {
        loadBlogList();
        loadScenePlayList();
    }

    private void loadBlogList() {
        new GetBlogListRequest(getActivity(), 1, 3).schedule(false, new RequestListener<List<BlogItem>>() {
            @Override
            public void onSuccess(List<BlogItem> result) {
                if(result != null && result.size() > 0) {
                    mPodcastLv.setAdapter(new BlogListAdapter(getActivity(), result));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void loadScenePlayList() {
        new GetScenePlayListRequest(getActivity(), 1, 6).schedule(false, new RequestListener<List<ScenePlayItem>>() {
            @Override
            public void onSuccess(List<ScenePlayItem> result) {
                if(result != null && result.size() > 0) {
                    mSceneGv.setAdapter(new ScenePlayAdapter(getActivity(), result));
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
        switch (id) {
            case R.id.listen_scene_more_tv:
                gotoAllScenePlayPage();
                break;
            case R.id.listen_podcast_more_tv:
                gotoBlogPage();
                break;
        }
    }

    private void gotoPlayPage(ScenePlayItem item) {
        Intent i = new Intent(getActivity(), ScenePlayListActivity.class);
        i.putExtra("scene_play_item", item);
        startActivity(i);
    }
    private void gotoBlogPage() {
        startActivity(new Intent(getActivity(), EnglishBlogActivity.class));
    }

    private void gotoAllScenePlayPage() {
        Intent i = new Intent(getActivity(), AllScenePlayActivity.class);
        startActivity(i);
    }

    private void gotoBlogDetailPage(BlogItem item) {
        Intent i = new Intent(getActivity(), BlogDetailActivity.class);
        i.putExtra("blog_item", item);
        startActivity(i);
    }
}
