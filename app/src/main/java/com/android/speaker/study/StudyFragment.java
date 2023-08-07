package com.android.speaker.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.course.CourseAdapter;
import com.android.speaker.course.CourseItem;
import com.android.speaker.course.GetRecommendCourseListRequest;
import com.android.speaker.home.IHomeCallBack;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.FormatUtils;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

public class StudyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = StudyFragment.class.getSimpleName();

    private static final int DEFAULT_MAX_SIZE = 6;
    private View mTitleLayout2;
    private TextView mUserTv2;
    private ImageView mCalendarIv2;
    private TextView mClockTimesTv2;
    private TextView mUserTv;
    private TextView mAiLabelTv;
    private ImageView mCalendarIv;
    private TextView mClockTimesTv;
    private ImageView mEditIv;
    private TextView mCurrTimeTv;
    private TextView mGoalTimeTv;
    private ProgressBar mProgressBar;
    private TextView mTotalTimeTv;
    private GridView mCourseGv;
    private TextView mMoreCourseTv;
    private ScrollView mScrollView;
    private TextView mOpenTitleTv;
    private TextView mOpenTip1Tv;
    private TextView mOpenTip2Tv;
    private TextView mOpenStartTv;
    private ImageView mOpenOssIv;
    private IHomeCallBack mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_study, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initGridview();
        initData();
    }

    private void initView(View view) {
        mTitleLayout2 = view.findViewById(R.id.study_title_layout2);
        mUserTv2 = view.findViewById(R.id.study_user_tv2);
        mCalendarIv2 = view.findViewById(R.id.study_clock_calendar_iv2);
        mClockTimesTv2 = view.findViewById(R.id.study_total_clock_times_tv2);
        mUserTv = view.findViewById(R.id.study_user_tv);
        mAiLabelTv = view.findViewById(R.id.study_ai_label_tv);
        mCalendarIv = view.findViewById(R.id.study_clock_calendar_iv);
        mClockTimesTv = view.findViewById(R.id.study_total_clock_times_tv);
        mEditIv = view.findViewById(R.id.study_edit_iv);
        mCurrTimeTv = view.findViewById(R.id.study_today_curr_time_tv);
        mGoalTimeTv = view.findViewById(R.id.study_today_goal_time_tv);
        mProgressBar = view.findViewById(R.id.study_today_progress_bar);
        mTotalTimeTv = view.findViewById(R.id.study_total_time_tv);
        mCourseGv = view.findViewById(R.id.study_course_gv);
        mMoreCourseTv = view.findViewById(R.id.study_course_more_tv);
        mScrollView = view.findViewById(R.id.study_scrollview);
        mOpenTitleTv = view.findViewById(R.id.study_open_mouth_title_tv);
        mOpenTip1Tv = view.findViewById(R.id.study_open_mouth_tip1_tv);
        mOpenTip2Tv = view.findViewById(R.id.study_open_mouth_tip2_tv);
        mOpenStartTv = view.findViewById(R.id.study_open_mouth_start_tv);
        mOpenOssIv = view.findViewById(R.id.study_open_mouth_oss_iv);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>10) {
                    changeTitleBar(true);
                } else {
                    changeTitleBar(false);
                }
            }

        });

        mCalendarIv2.setOnClickListener(this);
        mCalendarIv.setOnClickListener(this);
        mEditIv.setOnClickListener(this);
        mMoreCourseTv.setOnClickListener(this);
        mOpenTip1Tv.setOnClickListener(this);
        mOpenTip2Tv.setOnClickListener(this);
        mOpenStartTv.setOnClickListener(this);
    }

    private void initGridview() {
        // item之间的间隔
        int itemPadding = ScreenUtil.dip2px(15);
        // item宽度
        int itemWidth = ScreenUtil.getScreenWidth(getActivity())*150/393;
        // 计算GridView宽度
        int gridviewWidth = itemWidth*3 + itemPadding*2;
        int verticalPadding = ScreenUtil.dip2px(24);
        int itemHeight = ScreenUtil.dip2px(190);
        int gridviewHeight = itemHeight*2 + verticalPadding;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mCourseGv.setLayoutParams(params);
        mCourseGv.setColumnWidth(itemWidth);
        mCourseGv.setHorizontalSpacing(itemPadding);
        mCourseGv.setVerticalSpacing(verticalPadding);
        mCourseGv.setStretchMode(GridView.NO_STRETCH);
    }

    private void initData() {
        String name = "";
        UserInfo info = UserInfo.getInstance();
        if(!TextUtils.isEmpty(info.getName())) {
            name = info.getName();
        } else if(!TextUtils.isEmpty(info.getPhone())) {
            name = FormatUtils.formatTelephone(info.getPhone());
        }
        mTitleLayout2.setVisibility(View.GONE);
        String welcomeTip = getString(R.string.welcome_user, name);
        mUserTv.setText(welcomeTip);
        mUserTv2.setText(welcomeTip);

        new GetLearnInfoRequest(getActivity()).schedule(true, new RequestListener<LearnInfo>() {
            @Override
            public void onSuccess(LearnInfo result) {
                setLearnInfo(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
                setLearnInfo(new LearnInfo());
            }
        });

        new GetRecommendCourseListRequest(getActivity()).schedule(false, new RequestListener<List<CourseItem>>() {
            @Override
            public void onSuccess(List<CourseItem> result) {
                if(result.size() > DEFAULT_MAX_SIZE) {
                    result = result.subList(0, DEFAULT_MAX_SIZE);
                    mMoreCourseTv.setVisibility(View.VISIBLE);
                }
                CourseAdapter adapter = new CourseAdapter(getActivity(), result);
                mCourseGv.setAdapter(adapter);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });

        new GetOpenSpeakerListRequest(getActivity(), 1, 1).schedule(false, new RequestListener<List<OpenSpeakerInfo>>() {
            @Override
            public void onSuccess(List<OpenSpeakerInfo> result) {
                if(result != null && result.size() > 0) {
                    setOpenSpeakerInfo(result.get(0));
                }
            }

            @Override
            public void onFailed(Throwable e) {
                LogUtil.e(TAG, e.getMessage());
            }
        });
    }

    private void setLearnInfo(LearnInfo info) {
        if(info == null) {
            return;
        }
        int size = info.punchTimeList == null ? 0 : info.punchTimeList.size();
        String clockTimes = getString(R.string.total_clock_times, size);
        mClockTimesTv.setText(clockTimes);
        mClockTimesTv2.setText(clockTimes);
        mCurrTimeTv.setText(info.currentLearnTime + "");
        mGoalTimeTv.setText(info.targetTime + "");
        mProgressBar.setMax(info.targetTime);
        mProgressBar.setProgress(info.currentLearnTime);
        mTotalTimeTv.setText(info.grantTotal + "");
    }

    private void setOpenSpeakerInfo(OpenSpeakerInfo info) {
        if(!TextUtils.isEmpty(info.title)) {
            mOpenTitleTv.setText(info.title);
        }
        if(!TextUtils.isEmpty(info.ossUrl)) {
            GlideUtil.loadCornerImage(mOpenOssIv, info.ossUrl, null, 10);
            mOpenOssIv.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(info.tips)) {
            String[] tipsArray = info.tips.split(",");
            mOpenTip1Tv.setText(tipsArray[0]);
            mOpenTip1Tv.setVisibility(View.VISIBLE);
            if(tipsArray.length > 1) {
                mOpenTip2Tv.setText(tipsArray[1]);
                mOpenTip2Tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void changeTitleBar(boolean flag) {
        if(flag) {
            mUserTv.setVisibility(View.INVISIBLE);
            mAiLabelTv.setVisibility(View.INVISIBLE);
            mClockTimesTv.setVisibility(View.INVISIBLE);
            mCalendarIv.setVisibility(View.INVISIBLE);
            mTitleLayout2.setVisibility(View.VISIBLE);
            mCallback.callback(IHomeCallBack.TAB_STUDY, "up");
        } else {
            mUserTv.setVisibility(View.VISIBLE);
            mAiLabelTv.setVisibility(View.VISIBLE);
            mClockTimesTv.setVisibility(View.VISIBLE);
            mCalendarIv.setVisibility(View.VISIBLE);
            mTitleLayout2.setVisibility(View.GONE);
            mCallback.callback(IHomeCallBack.TAB_STUDY, "down");
        }
    }

    public void setCallback(IHomeCallBack callback) {
        this.mCallback = callback;
    }
}
