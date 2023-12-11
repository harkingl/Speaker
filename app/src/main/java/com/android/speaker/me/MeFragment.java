package com.android.speaker.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.favorite.FavoriteListActivity;
import com.android.speaker.me.setting.SettingActivity;
import com.android.speaker.me.vip.CouponListActivity;
import com.android.speaker.me.vip.VipOpenActivity;
import com.chinsion.SpeakEnglish.R;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mSettingIv;
    private ImageView mUserHeadIv;
    private TextView mUserNameTv;
    private TextView mVipValidateTv;
    private TextView mCourseSeriesTv;
    private TextView mChatFreelyTv;
    private TextView mCouponDetailTv;
    private TextView mVipRightsDetailTv;
    private TextView mVipRenewalTv;
    private ImageView mFunctionNewWordIv;
    private ImageView mFunctionLaptopIv;
    private TextView mFunctionRecordTv;
    private TextView mFunctionCollectTv;
    private TextView mFunctionTestTv;
    private TextView mFunctionMyOrderTv;
    private TextView mFunctionCouponTv;
    private TextView mFunctionScanTv;
    private TextView mFunctionClockTv;
    private TextView mFunctionHelpTv;
    private ImageView mVipDetailIv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mSettingIv = view.findViewById(R.id.me_setting_iv);
        mUserHeadIv = view.findViewById(R.id.me_user_head_iv);
        mUserNameTv = view.findViewById(R.id.me_user_name_tv);
        mVipValidateTv = view.findViewById(R.id.me_vip_validate_tv);
        mCourseSeriesTv = view.findViewById(R.id.me_course_series_tv);
        mChatFreelyTv = view.findViewById(R.id.me_ai_chat_freely_tv);
        mCouponDetailTv = view.findViewById(R.id.me_vip_coupon_detail_tv);
        mVipRightsDetailTv = view.findViewById(R.id.me_vip_rights_detail_tv);
        mVipRenewalTv = view.findViewById(R.id.me_vip_renewal_tv);
        mFunctionNewWordIv = view.findViewById(R.id.me_function_new_word_iv);
        mFunctionLaptopIv = view.findViewById(R.id.me_function_laptop_iv);
        mFunctionRecordTv = view.findViewById(R.id.me_function_record_tv);
        mFunctionCollectTv = view.findViewById(R.id.me_function_collect_tv);
        mFunctionTestTv = view.findViewById(R.id.me_function_test_tv);
        mFunctionMyOrderTv = view.findViewById(R.id.me_function_order_tv);
        mFunctionCouponTv = view.findViewById(R.id.me_function_coupon_tv);
        mFunctionScanTv = view.findViewById(R.id.me_function_scan_tv);
        mFunctionClockTv = view.findViewById(R.id.me_function_clock_tv);
        mFunctionHelpTv = view.findViewById(R.id.me_function_help_tv);
        mVipDetailIv = view.findViewById(R.id.me_vip_detail_iv);

        mSettingIv.setOnClickListener(this);
        mCouponDetailTv.setOnClickListener(this);
        mVipRightsDetailTv.setOnClickListener(this);
        mVipRenewalTv.setOnClickListener(this);
        mFunctionNewWordIv.setOnClickListener(this);
        mFunctionLaptopIv.setOnClickListener(this);
        mFunctionRecordTv.setOnClickListener(this);
        mFunctionCollectTv.setOnClickListener(this);
        mFunctionTestTv.setOnClickListener(this);
        mFunctionMyOrderTv.setOnClickListener(this);
        mFunctionCouponTv.setOnClickListener(this);
        mFunctionScanTv.setOnClickListener(this);
        mFunctionClockTv.setOnClickListener(this);
        mFunctionHelpTv.setOnClickListener(this);
        mVipDetailIv.setOnClickListener(this);
    }

    private void initData() {
//        String name = "";
//        UserInfo info = UserInfo.getInstance();
//        if(!TextUtils.isEmpty(info.getToken())) {
//            name = info.getName();
//        }
        mUserNameTv.setText("185****7028");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.me_setting_iv:
                gotoSetting();
                break;
            case R.id.me_function_collect_tv:
                gotoFavorite();
                break;
            case R.id.me_function_new_word_iv:
                gotoNewWord();
                break;
            case R.id.me_vip_detail_iv:
                gotoVipDetail();
                break;
            case R.id.me_function_coupon_tv:
                gotoCouponListPage();
                break;
        }
    }

    private void gotoVipDetail() {
        startActivity(new Intent(getActivity(), VipOpenActivity.class));
    }

    private void gotoCouponListPage() {
        startActivity(new Intent(getActivity(), CouponListActivity.class));
    }

    private void gotoSetting() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    private void gotoFavorite() {
        startActivity(new Intent(getActivity(), FavoriteListActivity.class));
    }

    private void gotoNewWord() {
        startActivity(new Intent(getActivity(), NewWordListActivity.class));
    }
}
