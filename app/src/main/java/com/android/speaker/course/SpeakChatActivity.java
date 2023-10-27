package com.android.speaker.course;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.chat.audio.AudioButton;
import com.android.speaker.favorite.FavoriteItem;
import com.android.speaker.favorite.GetFavoriteListRequest;
import com.android.speaker.home.FragmentAdapter;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.server.okhttp.WebSocketUtil;
import com.android.speaker.study.SpeakerDetailInfo;
import com.android.speaker.util.FileUtil;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/***
 * 开口说对话
 */
public class SpeakChatActivity extends BaseActivity implements View.OnClickListener, SpeakChatAdapter.ICallBack {

    private static final String TAG = "SpeakChatActivity";
    private static final int MAX_PROGRESS = 100;

    private ImageView mTopIv;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTranslateIv;
    private ViewPager2 mViewPager;
    private LinearLayout mIndicationsLayout;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private View mBottomLayout;
    private ImageView mAudioIv;
    private ImageView mInputIv;
    private EditText mInputTv;
    private ImageView mTipIv;
    private TextView mPointTv;
    private AudioButton mAudioBtn;
    private View mEmptyLayout;
    private List<ImageView> mIndicationViews = new ArrayList<>();
    private List<BaseFragment> mPageList = new ArrayList<>();
    private ArrayList<ChatItem> mList;
    private SpeakChatAdapter mAdapter;
    private boolean mIsOpen = true;
    private ExoPlayer mPlayer;
    private SpeakerDetailInfo mInfo;
    private SpeakChatDetail mDetail;
    private long mEndTimeMs;
    private WebSocketUtil mSocketUtil;
    private List<String> mContentList;
    private int mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_chat);

        initView();
        initData();
    }

    private void initView() {
        mTopIv = findViewById(R.id.chat_top_img_iv);
        mBackIv = findViewById(R.id.chat_back_iv);
        mTitleTv = findViewById(R.id.chat_title_tv);
        mTranslateIv = findViewById(R.id.chat_translate_iv);
        mViewPager = findViewById(R.id.chat_view_pager);
        mIndicationsLayout = findViewById(R.id.chat_indications_ll);
        mListView = findViewById(R.id.chat_data_lv);
        mProgressBar = findViewById(R.id.chat_progress_bar);
        mBottomLayout = findViewById(R.id.chat_bottom_ll);
        mAudioIv = findViewById(R.id.chat_btn_audio_iv);
        mInputIv = findViewById(R.id.chat_btn_input_iv);
        mInputTv = findViewById(R.id.chat_input_et);
        mTipIv = findViewById(R.id.chat_tip_iv);
        mAudioBtn = findViewById(R.id.chat_audio_btn);
        mPointTv = findViewById(R.id.chat_view_point_tv);
        mEmptyLayout = findViewById(R.id.chat_empty_ll);

        mBackIv.setOnClickListener(this);
        mTranslateIv.setOnClickListener(this);
        mAudioIv.setOnClickListener(this);
        mInputIv.setOnClickListener(this);
        mTipIv.setOnClickListener(this);
        mPointTv.setOnClickListener(this);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                updateIndicationView(position);
            }
        });

        mInputTv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = mInputTv.getText().toString();
                    if(TextUtils.isEmpty(text)) {
                        ToastUtil.toastLongMessage("请输入内容");
                        return true;
                    }
                    sendMessage(text);
                    return true;
                }
                return false;
            }
        });

        mAudioBtn.setListener(new AudioButton.AudioFinishListener() {
            @Override
            public void onAudioFinish(boolean isSuccess) {
                if(isSuccess && !TextUtils.isEmpty(mAudioBtn.getAudioPath())) {
                    sendAudio(mAudioBtn.getAudioPath());
                }
            }
        });
    }

    private void initData() {
        mInfo = (SpeakerDetailInfo) getIntent().getSerializableExtra(CourseUtil.KEY_SPEAK_DETAIL);

        mContentList = new ArrayList<>();

        mSocketUtil = new WebSocketUtil();
        mSocketUtil.setMessageListener(mListener);

        mTranslateIv.setImageResource(R.drawable.ic_translate_zh);

        if(!TextUtils.isEmpty(mInfo.ossUrl)) {
            GlideUtil.loadImage(mTopIv, mInfo.ossUrl, null);
        }
        mTitleTv.setText(mInfo.title);

        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        mPlayer = new ExoPlayer.Builder(this).build();

        mList = new ArrayList<>();
        mAdapter = new SpeakChatAdapter(this, mList, true);
        mAdapter.setCallback(this);
        mListView.setAdapter(mAdapter);

        new GetSpeakChatDetailRequest(this, mInfo.id).schedule(false, new RequestListener<SpeakChatDetail>() {
            @Override
            public void onSuccess(SpeakChatDetail result) {
                setView(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(SpeakChatDetail detail) {
        if(detail.scrollTitleList != null && detail.scrollTitleList.size() > 0) {
//            mPageList.add(new TopViewFirstFragment(mInfo.title, mInfo.des));
            for(SpeakChatDetail.HeaderContent content : detail.scrollTitleList) {
                mPageList.add(new TopViewOtherFragment(content.context));
            }
            FragmentAdapter adapter = new FragmentAdapter(this);
            adapter.setFragmentList(mPageList);
            mViewPager.setAdapter(adapter);

            initIndications(mPageList.size());
            updateIndicationView(0);
        }

        if(detail.chatItemList != null && detail.chatItemList.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            mList.addAll(detail.chatItemList);
            mAdapter.notifyDataSetChanged();
        }

        mSocketUtil.connect();
        mDetail = detail;
    }

    private void initIndications(int length) {
        for (int i = 0; i < length; i++){
            mIndicationViews.add(createImage());
        }
    }

    private ImageView createImage(){
        ImageView point = new ImageView(this);
        point.setImageResource(R.drawable.ic_indication_unselect);
        point.setPadding(0,0,ScreenUtil.dip2px(2),0);
        point.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIndicationsLayout.addView(point);
        return point;
    }

    private void updateIndicationView(int pos) {
        for (ImageView view : mIndicationViews) {
            view.setImageResource(R.drawable.ic_indication_unselect);
        }
        mIndicationViews.get(pos).setImageResource(R.drawable.ic_indication_selected);
    }

    private void sendMessage(String text) {
        if(mInfo != null) {
//            ChatItem item = mSocketUtil.sendMessage(text, mInfo.id);
            ChatItem item = mSocketUtil.sendMessage(text, "40");
            if(item != null) {
                item.name = mDetail.myName;
                item.isMySelf = true;
                mList.add(item);
                mContentList.add(text);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mList.size()-1);
                mInputTv.setText("");
                doContentAnalysis(item);

                if(mEmptyLayout.getVisibility() == View.VISIBLE) {
                    mEmptyLayout.setVisibility(View.GONE);
                }
            }
        }
        hideSoftInput();
    }

    private void doContentAnalysis(final ChatItem item) {
        if(item == null || item.state != ChatItem.STATE_ANALYSISING) {
            return;
        }

        new GetAnalysisRequest(this, mContentList, item).schedule(true, new RequestListener<ChatItem>() {
            @Override
            public void onSuccess(ChatItem result) {
                result.state = ChatItem.STATE_FINISH;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable e) {
                LogUtil.e(TAG, e.getMessage());
                if(mContentList.size() > 0) {
                    mContentList.remove(mContentList.size()-1);
                }
            }
        });
    }

    private void sendAudio(String path) {
        try {
            UUID uuid = UUID.randomUUID();
            byte[] uuidBytes = ByteBuffer.allocate(16)
                    .putLong(uuid.getMostSignificantBits())
                    .putLong(uuid.getLeastSignificantBits())
                    .array();
            byte[] audioData = readBytesFromFile(new File(path));
            byte[] combinedData = new byte[uuidBytes.length + audioData.length];
            System.arraycopy(uuidBytes, 0, combinedData, 0, uuidBytes.length);
            System.arraycopy(audioData, 0, combinedData, uuidBytes.length, audioData.length);
            ChatItem item = mSocketUtil.sendAudio(combinedData, uuid.toString());
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

    private static byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fis.getChannel();
        int size = (int) channel.size();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        channel.read(buffer);
        buffer.flip();
        byte[] data = new byte[size];
        buffer.get(data);
        fis.close();
        return data;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.chat_back_iv) {
            finish();
        } else if(id == R.id.chat_translate_iv) {
            mIsOpen = !mIsOpen;
            mTranslateIv.setImageResource(mIsOpen ? R.drawable.ic_translate_zh : R.drawable.ic_translate_en);
            mAdapter.setIsOpen(mIsOpen);
        } else if(id == R.id.chat_tip_iv) {
            showTipDialog(this);
        } else if(id == R.id.chat_btn_input_iv) {
            mAudioIv.setVisibility(View.VISIBLE);
            mInputIv.setVisibility(View.GONE);
            mAudioBtn.setVisibility(View.GONE);
            mInputTv.setVisibility(View.VISIBLE);
        } else if(id == R.id.chat_btn_audio_iv) {
            mAudioIv.setVisibility(View.GONE);
            mInputIv.setVisibility(View.VISIBLE);
            mAudioBtn.setVisibility(View.VISIBLE);
            mInputTv.setVisibility(View.GONE);
        } else if(id == R.id.chat_view_point_tv) {
            gotoReportPage();
        }
    }

    private void gotoReportPage() {
        Intent i = new Intent(this, ChatReportActivity.class);
        i.putExtra(CourseUtil.KEY_TITLE, mInfo.title);
        i.putExtra(CourseUtil.KEY_CHAT_LIST, mList);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void stopPlayer() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void doPlay(ChatItem item) {
        if(item == null || mPlayer.isPlaying()) {
            return;
        }

//        mVoiceIv.setImageResource(R.drawable.ic_question_voice_select);
        mPlayer.clearMediaItems();
        if(!TextUtils.isEmpty(item.audioOssKey)) {
            mPlayer.addMediaItem(MediaItem.fromUri(item.audioOssKey));
            mPlayer.prepare();
            mPlayer.play();
        }
    }

    private void updateProgress(boolean isEnd) {
        if(isEnd) {
            mProgress = MAX_PROGRESS;
            mProgressBar.setProgress(mProgress);
            mBottomLayout.setVisibility(View.GONE);
            mPointTv.setVisibility(View.VISIBLE);
            return;
        }
        if(mProgress < 50) {
            mProgress += 10;
        } else if(mProgress < 80) {
            mProgress += 5;
        } else if(mProgress < 90) {
            mProgress += 2;
        } else {
            mProgress += 1;
        }
        if(mProgress >= MAX_PROGRESS) {
            mProgress = MAX_PROGRESS - 1;
        }
        mProgressBar.setProgress(mProgress);
    }

    @Override
    protected void onDestroy() {
        stopPlayer();
        if(mSocketUtil != null) {
            mSocketUtil.disConnect();
        }
        if(mAudioBtn != null) {
            mAudioBtn.release();
        }
        super.onDestroy();
    }

    private WebSocketUtil.MessageReceivedListener mListener = new WebSocketUtil.MessageReceivedListener() {
        @Override
        public void handleMessage(String message) {
            try {
                JSONObject obj = new JSONObject(message);

                String id = obj.optString("id");
                String content = obj.optString("data");
                if(TextUtils.isEmpty(content)) {
                    return;
                }
                if(!TextUtils.isEmpty(id)) {
                    sendMessage(content);
                } else {
                    ChatItem item = new ChatItem();
                    item.state = ChatItem.STATE_FINISH;
                    item.name = mDetail.userName;
                    item.isMySelf = false;
                    mList.add(item);
                    mAdapter.notifyDataSetChanged();
                    if(mEmptyLayout.getVisibility() == View.VISIBLE) {
                        mEmptyLayout.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
            updateProgress(false);
            LogUtil.d(TAG, message);
        }
    };

    private void showTipDialog(final Context context) {
        if(mDetail == null || mDetail.showTitles == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_chat_tip, null);
        final AlertDialog dialog = builder.create();
        dialog.setView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_chat_tip);
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

        lv.setAdapter(new ChatTipListAdapter(this, mDetail.showTitles));
    }
}
