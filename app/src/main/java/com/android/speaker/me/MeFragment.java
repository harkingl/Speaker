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

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.favorite.FavoriteListActivity;
import com.android.speaker.me.setting.HelpActivity;
import com.android.speaker.me.setting.SettingActivity;
import com.android.speaker.me.vip.CouponListActivity;
import com.android.speaker.me.vip.RedemptionCenterActivity;
import com.android.speaker.me.vip.VipDetailActivity;
import com.android.speaker.me.vip.VipOpenActivity;
import com.chinsion.SpeakEnglish.R;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mSettingIv;
    private ImageView mUserHeadIv;
    private TextView mUserNameTv;
    private View mVipParentLayout;
    private TextView mVipValidateTv;
    private TextView mCourseSeriesTv;
    private TextView mChatFreelyTv;
    private TextView mCouponDetailTv;
    private TextView mVipRightsDetailTv;
    private TextView mVipRenewalTv;
    private View mVipRenewalLayout;

    // 非会员
    private View mUnVipParentLayout;

    private View mFunctionNewWordLl;
    private View mFunctionLaptopLl;
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
        mVipParentLayout = view.findViewById(R.id.me_vip_parent_ll);
        mVipValidateTv = view.findViewById(R.id.me_vip_validate_tv);
        mCourseSeriesTv = view.findViewById(R.id.me_course_series_tv);
        mChatFreelyTv = view.findViewById(R.id.me_ai_chat_freely_tv);
        mCouponDetailTv = view.findViewById(R.id.me_vip_coupon_detail_tv);
        mVipRightsDetailTv = view.findViewById(R.id.me_vip_rights_detail_tv);
        mVipRenewalTv = view.findViewById(R.id.me_vip_renewal_tv);
        mVipRenewalLayout = view.findViewById(R.id.me_vip_renewal_ll);
        mUnVipParentLayout = view.findViewById(R.id.me_unvip_parent_ll);
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
        mFunctionNewWordLl = view.findViewById(R.id.me_function_new_word_ll);
        mFunctionLaptopLl = view.findViewById(R.id.me_function_laptop_ll);

        mSettingIv.setOnClickListener(this);
        mVipRightsDetailTv.setOnClickListener(this);
        mVipRenewalTv.setOnClickListener(this);
        mFunctionNewWordLl.setOnClickListener(this);
        mFunctionLaptopLl.setOnClickListener(this);
        mFunctionRecordTv.setOnClickListener(this);
        mFunctionCollectTv.setOnClickListener(this);
        mFunctionTestTv.setOnClickListener(this);
        mFunctionMyOrderTv.setOnClickListener(this);
        mFunctionCouponTv.setOnClickListener(this);
        mFunctionScanTv.setOnClickListener(this);
        mFunctionClockTv.setOnClickListener(this);
        mFunctionHelpTv.setOnClickListener(this);
        mVipDetailIv.setOnClickListener(this);
        mVipParentLayout.setOnClickListener(this);
        mUnVipParentLayout.setOnClickListener(this);
        mVipRenewalLayout.setOnClickListener(this);
    }

    private void initData() {
//        String name = "";
//        UserInfo info = UserInfo.getInstance();
//        if(!TextUtils.isEmpty(info.getToken())) {
//            name = info.getName();
//        }
        UserInfo info = UserInfo.getInstance();
        mUserNameTv.setText(info.getName());

        if(info.isVip()) {
            mUnVipParentLayout.setVisibility(View.GONE);
            mVipParentLayout.setVisibility(View.VISIBLE);
        } else {
            mUnVipParentLayout.setVisibility(View.VISIBLE);
            mVipParentLayout.setVisibility(View.GONE);
        }
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
            case R.id.me_function_new_word_ll:
                gotoNewWord();
                break;
            case R.id.me_function_coupon_tv:
                gotoCouponListPage();
                break;
            case R.id.me_function_laptop_ll:
                gotoNotePage();
                break;
            case R.id.me_function_order_tv:
                gotoOrderPage();
                break;
            case R.id.me_function_record_tv:
                gotoRecordPage();
                break;
            case R.id.me_function_test_tv:
                gotoLevelTestPage();
                break;
            case R.id.me_function_help_tv:
                gotoHelpPage();
                break;
            case R.id.me_unvip_parent_ll:
            case R.id.me_vip_renewal_ll:
                gotoVipOpenPage();
                break;
            case R.id.me_vip_parent_ll:
                gotoVipDetailPage();
                break;
            case R.id.me_function_scan_tv:
                gotoRedemptionPage();
                break;
        }
    }

    private void gotoVipOpenPage() {
        startActivity(new Intent(getActivity(), VipOpenActivity.class));
    }

    private void gotoVipDetailPage() {
        startActivity(new Intent(getActivity(), VipDetailActivity.class));
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

    private void gotoNotePage() {
        startActivity(new Intent(getActivity(), NoteListActivity.class));
    }

    private void gotoOrderPage() {
        startActivity(new Intent(getActivity(), OrderListActivity.class));
    }

    private void gotoRecordPage() {
        startActivity(new Intent(getActivity(), RecordListActivity.class));
    }

    private void gotoLevelTestPage() {
        startActivity(new Intent(getActivity(), LevelTestActivity.class));
    }

    private void gotoHelpPage() {
        startActivity(new Intent(getActivity(), HelpActivity.class));
    }

    private void gotoRedemptionPage() {
        startActivity(new Intent(getActivity(), RedemptionCenterActivity.class));
    }
}
