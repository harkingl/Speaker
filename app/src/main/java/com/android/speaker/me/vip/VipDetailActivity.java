package com.android.speaker.me.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollGridView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class VipDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "VipDetailActivity";

    private TitleBarLayout titleBarLayout;
    private TextView mValidateTv;
    private NoScrollGridView mDiscountGv;
    private TextView mRenewalTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_detail);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.vip_title_bar);
        mValidateTv = findViewById(R.id.vip_validate_tv);
        mDiscountGv = findViewById(R.id.vip_discount_gv);
        mRenewalTv = findViewById(R.id.vip_renewal_tv);

        mRenewalTv.setOnClickListener(this);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("开口说会员", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        new GetCouponReceiveListRequest(this).schedule(false, new RequestListener<List<CouponInfo>>() {
            @Override
            public void onSuccess(List<CouponInfo> result) {
                initProductGv(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void initProductGv(List<CouponInfo> list) {
        if(list == null || list.size() == 0) {
            return;
        }

        CouponReceiveListAdapter adapter = new CouponReceiveListAdapter(this, list);
        mDiscountGv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mRenewalTv) {
            renewalVip();
        }
    }

    private void renewalVip() {
        startActivity(new Intent(this, VipOpenActivity.class));
    }
}