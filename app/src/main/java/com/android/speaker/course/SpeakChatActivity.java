package com.android.speaker.course;

import android.app.AlertDialog;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/***
 * 开口说对话
 */
public class SpeakChatActivity extends BaseActivity implements View.OnClickListener, SpeakChatAdapter.ICallBack {

    private static final String TAG = "SpeakChatActivity";

    private ImageView mTopIv;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTranslateIv;
    private ViewPager2 mViewPager;
    private LinearLayout mIndicationsLayout;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ImageView mAudioIv;
    private ImageView mInputIv;
    private EditText mInputTv;
    private ImageView mTipIv;
    private AudioButton mAudioBtn;
    private List<ImageView> mIndicationViews = new ArrayList<>();
    private List<BaseFragment> mPageList = new ArrayList<>();
    private List<ChatItem> mList;
    private SpeakChatAdapter mAdapter;
    private boolean mIsOpen = true;
    private ExoPlayer mPlayer;
    private SpeakerDetailInfo mInfo;
    private SpeakChatDetail mDetail;
    private long mEndTimeMs;
    private WebSocketUtil mSocketUtil;
    private List<String> mContentList;

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
        mAudioIv = findViewById(R.id.chat_btn_audio_iv);
        mInputIv = findViewById(R.id.chat_btn_input_iv);
        mInputTv = findViewById(R.id.chat_input_et);
        mTipIv = findViewById(R.id.chat_tip_iv);
        mAudioBtn = findViewById(R.id.chat_audio_btn);

        mBackIv.setOnClickListener(this);
        mTranslateIv.setOnClickListener(this);
        mAudioIv.setOnClickListener(this);
        mInputIv.setOnClickListener(this);
        mTipIv.setOnClickListener(this);
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
                    sendMessage();
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
        mProgressBar.setProgress(50);

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

        //TODO
        detail.userName = "Monica";
        detail.myName = "James";
        if(detail.chatItemList != null && detail.chatItemList.size() > 0) {
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

    private void sendMessage() {
        String text = mInputTv.getText().toString();
        if(TextUtils.isEmpty(text)) {
            ToastUtil.toastLongMessage("请输入内容");
            return;
        }
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
        FileInputStream objFileIS = null;
        FileOutputStream out = null;
        ByteArrayOutputStream objByteArrayOS = null;
        try {
            objFileIS = new FileInputStream(new File(path));

            File f = new File(FileUtil.ROOT_FILE_PATH, "byte.txt");
            if(!f.exists()) {
                f.createNewFile();
            }
            out = new FileOutputStream(f);
            objByteArrayOS = new ByteArrayOutputStream();
            String uuid = UUID.randomUUID().toString();
            objByteArrayOS.write(uuid.getBytes());
            out.write(uuid.getBytes());
            byte[] byteBufferString = new byte[1024];
            while (true) {
                int readNum = objFileIS.read(byteBufferString);
                if(readNum != -1) {
                    objByteArrayOS.write(byteBufferString, 0, readNum);
                    out.write(byteBufferString, 0, readNum);
                }
                if (readNum < 1024) {
                    break;
                }
            }
            ChatItem item = mSocketUtil.sendAudio(objByteArrayOS.toByteArray(), uuid);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if(objByteArrayOS != null) {
                try {
                    objByteArrayOS.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
        }

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
        }
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
//            ToastUtil.toastLongMessage(message);

            try {
                JSONObject obj = new JSONObject(message);

                ChatItem item = new ChatItem();
                item.uniqueId = obj.optString("uniqueId");
                item.content = obj.optString("data");
                item.state = ChatItem.STATE_FINISH;
                item.name = mDetail.userName;
                item.isMySelf = false;
                mList.add(item);
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
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
