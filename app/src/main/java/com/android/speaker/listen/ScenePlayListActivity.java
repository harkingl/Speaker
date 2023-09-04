package com.android.speaker.listen;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.course.SceneCourseActivity;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

/***
 * 播放列表
 */
public class ScenePlayListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SceneCourseActivity.class.getSimpleName();
    private ImageView mTopImgIv;
    private ImageView mBackIv;
    private ImageView mFollowupIv;
    private TextView mTitleTv;
    private View mPlayListLayout;
    private View mPlayAllLayout;
    private TextView mPlayCountTv;
    private NoScrollListView mListView;
    private View mBottomLayout;
    private ImageView mBottomIv;
    private TextView mBottomTitleTv;
    private TextView mBottomTypeTv;
    private PlayProgressView mBottomPlayIv;
    private ImageView mBottomMenuIv;
    private ExoPlayer mPlayer;
    private List<ScenePlayDataItem> mList;
    private PlayAllListAdapter mAdapter;
    private List<ScenePlayDataItem> mPlayList;
    private ScenePlayItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_play_list);

        initView();
        initData();
    }

    private void initView() {
        mTopImgIv = findViewById(R.id.play_top_img_iv);
        mBackIv = findViewById(R.id.play_back_iv);
        mFollowupIv = findViewById(R.id.play_followup_iv);
        mTitleTv = findViewById(R.id.play_title_tv);
        mPlayListLayout = findViewById(R.id.play_list_ll);
        mPlayAllLayout = findViewById(R.id.play_all_ll);
        mPlayCountTv = findViewById(R.id.play_count_tv);
        mListView = findViewById(R.id.play_list_lv);
        mBottomLayout = findViewById(R.id.play_list_bottom_ll);
        mBottomIv = findViewById(R.id.play_list_bottom_iv);
        mBottomTitleTv = findViewById(R.id.play_list_bottom_title_tv);
        mBottomTypeTv = findViewById(R.id.play_list_bottom_type_tv);
        mBottomPlayIv = findViewById(R.id.play_list_bottom_play_iv);
        mBottomMenuIv = findViewById(R.id.play_list_bottom_menu_iv);

        mBackIv.setOnClickListener(this);
        mFollowupIv.setOnClickListener(this);
        mPlayAllLayout.setOnClickListener(this);
        mBottomPlayIv.setOnClickListener(this);
        mBottomMenuIv.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int playPos = mPlayList.indexOf(mList.get(i));
                if(playPos < 0) {
                    int newPlayPos = -1;
                    for(ScenePlayDataItem item : mPlayList) {
                        int itemPos = mList.indexOf(item);
                        if(itemPos > i) {
                            newPlayPos = mPlayList.indexOf(item);
                            break;
                        }
                    }
                    if(newPlayPos == -1) {
                        newPlayPos = mPlayList.size();
                    }
                    mPlayList.add(newPlayPos, mList.get(i));
                    if(!TextUtils.isEmpty(mList.get(i).audioOssKey)) {
                        mPlayer.addMediaItem(newPlayPos, MediaItem.fromUri(mList.get(i).audioOssKey));
                    }
                    seekToPosition(newPlayPos);
                } else {
                    seekToPosition(playPos);
                }
//                if(mPlayer.isPlaying()) {
//                    mPlayer.stop();
//                }
            }
        });

    }

    private void initData() {
        mItem = (ScenePlayItem) getIntent().getSerializableExtra("scene_play_item");
        if(mItem != null) {
            GlideUtil.loadImage(mTopImgIv, mItem.iconUrl, null);
            mTitleTv.setText(mItem.title);
        }
        mPlayer = new ExoPlayer.Builder(this).build();
        mPlayer.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                LogUtil.d(TAG, "onPlaybackStateChanged：" + playbackState);
                if(playbackState == Player.STATE_ENDED) {
                    mBottomPlayIv.pause();
                    stopPlayer();
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);

                if(reason == Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM) {
                    int position = mPlayer.getNextMediaItemIndex();
                    seekToPosition(position);
                }

                LogUtil.d(TAG, "onPlayWhenReadyChanged: " + playWhenReady + " " + reason);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
                Player.Listener.super.onPlayerError(error);
            }
        });

        mList = new ArrayList<>();
        mPlayList = new ArrayList<>();
//        for(int i = 0; i < 20; i++) {
//            ScenePlayDataItem item = new ScenePlayDataItem();
//            item.title = "Shopping overseas" + i;
//            item.des = "出国想要买买买？什么地方值得买？怎么…";
//            item.leveName = "A1";
//            mList.add(item);
//        }
//        mPlayList.addAll(mList);

//        mPlayer.addMediaItem(MediaItem.fromUri("http://amssamples.streaming.mediaservices.windows.net/91492735-c523-432b-ba01-faba6c2206a2/AzureMediaServicesPromo.ism/manifest"));
//        mPlayer.addMediaItem(MediaItem.fromUri("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8"));
//        mPlayer.addMediaItem(MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"));
////        mPlayer.addMediaItem(MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"));
//        mPlayer.addMediaItem(MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"));

//        mPlayer.prepare();
//        mPlayer.setPlayWhenReady(true);
//        mPlayer.setPauseAtEndOfMediaItems(true);
//        mPlayer.play();

        mAdapter = new PlayAllListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        new GetScenePlayDataListRequest(this, 1, 20, mItem.id).schedule(false, new RequestListener<List<ScenePlayDataItem>>() {
            @Override
            public void onSuccess(List<ScenePlayDataItem> result) {
                if(result != null) {
                    mList.addAll(result);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void seekToPosition(int position) {
        LogUtil.d(TAG, "Seek to position：" + position);
        if(position != -1) {
            ScenePlayDataItem item = mPlayList.get(position);
            mPlayer.seekTo(position);
            mPlayer.prepare();
            mPlayer.play();
            mAdapter.setSelectIndex(mList.indexOf(item));
            mAdapter.notifyDataSetChanged();

            GlideUtil.loadImage(mBottomIv, item.homePage, null);
            mBottomTitleTv.setText(item.title);
            mBottomTypeTv.setText(item.des);
            mBottomPlayIv.play();
            if(mBottomLayout.getVisibility() != View.VISIBLE) {
                mBottomLayout.setVisibility(View.VISIBLE);
            }

            mBottomPlayIv.updateProgress(0);
            mHandler.sendEmptyMessageDelayed(WHAT_PROGRESS, 1000);
        }
    }

    private static final int WHAT_PROGRESS = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_PROGRESS:
                    int progress = (int) ((mPlayer.getCurrentPosition()*100)/mPlayer.getDuration());
                    mBottomPlayIv.updateProgress(progress);
                    if(mPlayer.isPlaying()) {
                        sendEmptyMessageDelayed(WHAT_PROGRESS, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v == mBackIv) {
            finish();
        } else if(v == mFollowupIv) {
        } else if(v == mPlayAllLayout) {
            if(mList != null && mList.size() > 0) {
                mPlayList.clear();
                mPlayList.addAll(mList);
                for(ScenePlayDataItem item : mPlayList) {
                    if(!TextUtils.isEmpty(item.audioOssKey)) {
                        mPlayer.addMediaItem(MediaItem.fromUri(item.audioOssKey));
                    }
                }
//                mAdapter.setSelectIndex(mList.indexOf(mPlayList.get(0)));
//                mAdapter.notifyDataSetChanged();
//                mPlayer.prepare();
//                mPlayer.play();
                seekToPosition(0);
            }
        } else if(v == mBottomPlayIv) {
            if(mPlayer.isPlaying()) {
                mBottomPlayIv.pause();
                mPlayer.pause();
            } else {
                mBottomPlayIv.play();
                mPlayer.play();
                mHandler.sendEmptyMessageDelayed(WHAT_PROGRESS, 1000);
            }
        } else if(v == mBottomMenuIv) {
            showPlayListDialog(this);
        }
    }

    @Override
    protected void onStop() {
        stopPlayer();
        mHandler.removeMessages(WHAT_PROGRESS);
        super.onStop();
    }

    private void stopPlayer() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    private DialogPlayListAdapter mDialogAdapter;
    private void showPlayListDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_play_list, null);
        final AlertDialog dialog = builder.create();
        dialog.setView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_play_list);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        ImageView closeIv = (ImageView) window.findViewById(R.id.dialog_common_view_close_btn);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        NoScrollListView lv = (NoScrollListView) window.findViewById(R.id.dialog_common_lv);
        lv.setMaxHeight((int)(ScreenUtil.getScreenHeight(this)*0.34));
        mDialogAdapter = new DialogPlayListAdapter(this, mPlayList, new DialogPlayListAdapter.ICallback() {
            @Override
            public void doDelete(int position) {
                if(position >= 0 && position < mPlayList.size()) {
                    if(mPlayer.getCurrentMediaItemIndex() == position) {
                        mPlayer.stop();
                        mDialogAdapter.setSelectIndex(-1);
                        mAdapter.setSelectIndex(-1);
                        mAdapter.notifyDataSetChanged();
                    }
                    mPlayList.remove(position);
                    mPlayer.removeMediaItem(position);
                    mDialogAdapter.notifyDataSetChanged();
                }
            }
        });
        lv.setAdapter(mDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDialogAdapter.setSelectIndex(i);
                seekToPosition(i);
            }
        });
        mDialogAdapter.setSelectIndex(mPlayer.getCurrentMediaItemIndex());
    }
}