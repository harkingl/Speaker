package com.android.speaker.course;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import org.apmem.tools.layouts.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * 选择题弹框
 */
public class SelectQuestionDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "SelectQuestionDialog";
    private View mVoiceLayout;
    private ImageView mVoiceIv;
    private TextView mContentTv;
    private FlowLayout mTagLayout;
    private TextView mCommitTv;
    private View mBottomLayout;
    private TextView mAnswerAnalysisTv;
    private TextView mContinueTv;
    private TextView mSkipTv;
    private Context mContext;
    private List<String> mSelectList;
    private List<String> mAnswerList;
    private List<String> mMySelectList = new ArrayList<>();
    private TextView mCurrentSelectTv;

    private QuestionListener mListener;
    private ExoPlayer mPlayer;
    private QuestionInfo mInfo;

    public SelectQuestionDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_select_question);
        mVoiceLayout = findViewById(R.id.dialog_voice_ll);
        mVoiceIv = findViewById(R.id.dialog_voice_iv);
        mContentTv = findViewById(R.id.dialog_content_tv);
        mTagLayout = findViewById(R.id.dialog_tags_fl);
        mCommitTv = findViewById(R.id.dialog_btn_commit_tv);
        mBottomLayout = findViewById(R.id.dialog_bottom_ll);
        mAnswerAnalysisTv = findViewById(R.id.dialog_answer_analysis_tv);
        mContinueTv = findViewById(R.id.dialog_btn_continue_tv);
        mSkipTv = findViewById(R.id.dialog_skip_tv);

        mVoiceIv.setOnClickListener(this);
        mCommitTv.setOnClickListener(this);
        mAnswerAnalysisTv.setOnClickListener(this);
        mContinueTv.setOnClickListener(this);
        mSkipTv.setOnClickListener(this);
    }


    public void setData(QuestionInfo info) {
        if (info == null) {
            return;
        }
        mSelectList = info.questionSelect;
        mAnswerList = info.answerList;
        int index = info.question.indexOf("{{%1}}");
        if(index >= 0) {
            String text = info.question.replace("{{%1}}", "        ");
            SpannableString content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), index, index+8, 0);
            mContentTv.setText(content);
        } else {
            mContentTv.setText(info.question);
        }

        if(info.type == QuestionInfo.TYPE_LISTEN_SELECT) {
            mVoiceLayout.setVisibility(View.VISIBLE);
            initPlayer();
        }

        this.mInfo = info;
        setTagView();
    }

    private void setTagView() {
        if(mSelectList == null || mSelectList.size() == 0) {
            return;
        }
        mTagLayout.post(new Runnable() {
            @Override
            public void run() {
                mTagLayout.removeAllViews();
                int itemWidth = mTagLayout.getWidth()/2;
                for(String tagName : mSelectList) {
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(itemWidth, FlowLayout.LayoutParams.WRAP_CONTENT);
                    mTagLayout.addView(generateTagView(tagName), params);
                }
            }
        });
    }

    private View generateTagView(String tagName) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tag_select_question_item, null);
        TextView tv = v.findViewById(R.id.tag_name_tv);
        tv.setText(tagName);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMySelectList.contains(tagName) || mBottomLayout.getVisibility() == View.VISIBLE) {
                    return;
                }

                if(checkAnswer(tagName)) {
                    tv.setBackgroundResource(R.drawable.green_bg_shape_5);
                    tv.setTextColor(mContext.getColor(R.color.white));
                    mCommitTv.setVisibility(View.GONE);
                    mSkipTv.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.VISIBLE);
                } else {
                    tv.setBackgroundResource(R.drawable.red_bg_shape_5);
                    tv.setTextColor(mContext.getColor(R.color.white));
                    mCurrentSelectTv = tv;
                    mHandler.sendEmptyMessageDelayed(WHAT_DELAY_SHOW, 2000);
                    mSkipTv.setVisibility(View.GONE);
                    mCommitTv.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;
    }

    private boolean checkAnswer(String selectAnswer) {
        if(TextUtils.isEmpty(selectAnswer) || mAnswerList == null || mAnswerList.size() == 0) {
            return false;
        }
        for(String answer : mAnswerList) {
            if(selectAnswer.contains(answer)) {
                return true;
            }
        }

        return false;
    }

    private void initPlayer() {
        if(mPlayer != null) {
            return;
        }
        mPlayer = new ExoPlayer.Builder(mContext).build();
        mPlayer.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState == Player.STATE_ENDED) {
                    mVoiceIv.setImageResource(R.drawable.ic_question_voice_default);
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
                Player.Listener.super.onPlayerError(error);
            }
        });
    }

    private static final int WHAT_DELAY_SHOW = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_DELAY_SHOW:
                    if(mCurrentSelectTv != null) {
                        mCurrentSelectTv.setBackgroundResource(R.drawable.tag_shape_default);
                        mCurrentSelectTv.setTextColor(mContext.getColor(R.color.text_color_CACACA));
                        mCurrentSelectTv.setEnabled(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dialog_btn_commit_tv:
            case R.id.dialog_answer_analysis_tv:
                if(mListener != null) {
                    mListener.answerAnalysis(mInfo.answerAnalysis);
                }
                dismiss();
                break;
            case R.id.dialog_btn_continue_tv:
            case R.id.dialog_skip_tv:
                if(mListener != null) {
                    mListener.onContinue();
                }
                dismiss();
                break;
            case R.id.dialog_voice_iv:
                if (!mPlayer.isPlaying()) {
                    doPlay();
                }
                break;
        }
    }

    private void onCommit() {
        mCommitTv.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.VISIBLE);

        for(int i = 0; i < mMySelectList.size(); i++) {
            if(i >= mAnswerList.size()) {
                break;
            }
            int green = mContext.getResources().getColor(R.color.common_green_color);
            int white = mContext.getResources().getColor(R.color.white);
            int red = mContext.getResources().getColor(R.color.common_red_color);

            int backgroundColor = mMySelectList.get(i).equals(mAnswerList.get(i)) ? green : red;
        }

//        if(mListener != null) {
//            mListener.onCommit();
//        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        stopPlayer();
    }

    private void doPlay() {
        mVoiceIv.setImageResource(R.drawable.ic_question_voice_select);
        mPlayer.clearMediaItems();
        if(!TextUtils.isEmpty(mInfo.audioUrl)) {
            mPlayer.addMediaItem(MediaItem.fromUri(mInfo.audioUrl));
            mPlayer.prepare();
            mPlayer.play();
        }
    }

    private void stopPlayer() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    public void setListener(QuestionListener mListener) {
        this.mListener = mListener;
    }

    class AnswerRange {
        public int startIndex;
        public int endIndex;
        public int blankCount;
    }
}
