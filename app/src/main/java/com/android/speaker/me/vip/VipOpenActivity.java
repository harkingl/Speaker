package com.android.speaker.me.vip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.speaker.base.Constants;
import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollGridView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ThreadUtils;
import com.android.speaker.util.ToastUtil;
import com.android.speaker.web.WebActivity;
import com.chinsion.SpeakEnglish.R;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

public class VipOpenActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "VipOpenActivity";

    private static final int PAY_TYPE_ALI = 0;
    private static final int PAY_TYPE_WX = 1;
    private TitleBarLayout titleBarLayout;
    private View mValidateLayout;
    private TextView mTimeHourTv;
    private TextView mTimeMinuteTv;
    private TextView mTimeSecondTv;
    private TextView mPreferentialPriceTv;
    private TextView mOpenAgreeTv;
    private TextView mRenewalProtocolTv;
    private TextView mServiceProtocolTv;
    private NoScrollGridView mProductGv;
    private ImageView mAlipayCheckIv;
    private ImageView mWxCheckIv;
    private VipInfo mVipInfo;
    private VipProductListAdapter mAdapter;
    private int mPayType = PAY_TYPE_ALI;
    private ProductInfo mProductInfo;
    private CouponInfo mCouponInfo;
    private IWXAPI mWxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_vip);

        initView();
        configTitleBar();
        initData();
        registerToWx();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.open_title_bar);
        mValidateLayout = findViewById(R.id.open_validate_ll);
        mTimeHourTv = findViewById(R.id.open_time_hour_tv);
        mTimeMinuteTv = findViewById(R.id.open_time_minute_tv);
        mTimeSecondTv = findViewById(R.id.open_time_second_tv);
        mPreferentialPriceTv = findViewById(R.id.open_preferential_price_tv);
        mOpenAgreeTv = findViewById(R.id.open_agree_btn_tv);
        mRenewalProtocolTv = findViewById(R.id.open_renewal_protocol_tv);
        mServiceProtocolTv = findViewById(R.id.open_service_protocol_tv);
        mProductGv = findViewById(R.id.open_product_gv);
        mAlipayCheckIv = findViewById(R.id.open_alipay_check_iv);
        mWxCheckIv = findViewById(R.id.open_wx_check_iv);

        mOpenAgreeTv.setOnClickListener(this);
        mRenewalProtocolTv.setOnClickListener(this);
        mServiceProtocolTv.setOnClickListener(this);
        mAlipayCheckIv.setOnClickListener(this);
        mWxCheckIv.setOnClickListener(this);
    }
    private void configTitleBar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("开口说会员", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        setPayType(PAY_TYPE_ALI);

        new GetVipInfoRequest(this).schedule(false, new RequestListener<VipInfo>() {
            @Override
            public void onSuccess(VipInfo result) {
                mVipInfo = result;
                initProductGv(result.aliProductList);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void registerToWx() {
        mWxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        mWxApi.registerApp(Constants.WX_APP_ID);
    }

    private void wxPay(PayInfo info) {
        PayReq request = new PayReq();

        request.appId = info.appId;

        request.partnerId = info.partnerId;

        request.prepayId= info.prepayId;

        request.packageValue = info.packageVal;

        request.nonceStr= info.nonceStr;

        request.timeStamp= info.timestamp;

        request.sign= info.sign;

        mWxApi.sendReq(request);
    }

    private void setPayType(int type) {
        if(type == PAY_TYPE_WX) {
            mWxCheckIv.setImageResource(R.drawable.ic_circle_checked);
            mAlipayCheckIv.setImageResource(R.drawable.ic_circle_uncheck);
            if(mVipInfo != null) {
                initProductGv(mVipInfo.wxProductList);
//                mAdapter.setData(mVipInfo.wxProductList);
            }
        } else {
            mWxCheckIv.setImageResource(R.drawable.ic_circle_uncheck);
            mAlipayCheckIv.setImageResource(R.drawable.ic_circle_checked);
            if(mVipInfo != null) {
                initProductGv(mVipInfo.aliProductList);
//                mAdapter.setData(mVipInfo.aliProductList);
            }
        }
        this.mPayType = type;
    }

    private void initProductGv(List<ProductInfo> list) {
        if(list == null || list.size() == 0) {
            return;
        }
        // item之间的间隔
        int itemPadding = ScreenUtil.dip2px(10);
        int itemWidth = ScreenUtil.dip2px(122);

        int sceneGridviewWidth = itemWidth*list.size() + itemPadding*(list.size()-1);
        LinearLayout.LayoutParams sceneParams = new LinearLayout.LayoutParams(
                sceneGridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mProductGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSelectItem(position);
            }
        });
        mProductGv.setLayoutParams(sceneParams);
        mProductGv.setNumColumns(list.size());
        mProductGv.setColumnWidth(itemWidth);
        mProductGv.setHorizontalSpacing(itemPadding);
        mProductGv.setVerticalSpacing(itemPadding);
        mProductGv.setStretchMode(GridView.NO_STRETCH);
        mAdapter = new VipProductListAdapter(this, list);
        mProductGv.setAdapter(mAdapter);

        onSelectItem(0);
//        mProductGv.post(new Runnable() {
//            @Override
//            public void run() {
//                mProductGv.smoothScrollToPosition(0);
//            }
//        });
    }

    private void onSelectItem(int position) {
        if(position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        mProductInfo = (ProductInfo) mAdapter.getItem(position);
        mCouponInfo = null;
        if(mVipInfo.couponList != null) {
            for(CouponInfo item : mVipInfo.couponList) {
                if(item.products != null && item.products.contains(mProductInfo.id)) {
                    mCouponInfo = item;
                    break;
                }
            }
        }
        if(mCouponInfo == null) {
            mValidateLayout.setVisibility(View.GONE);
        } else {
            // TODO
            mValidateLayout.setVisibility(View.VISIBLE);
        }
        mAdapter.setSelectIndex(position);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mOpenAgreeTv) {
            openVip();
        } else if(v == mRenewalProtocolTv) {
            gotoWebPage(Constants.RENEWAL_PROTOCOL);
        } else if(v == mServiceProtocolTv) {
            gotoWebPage(Constants.VIP_SERVICE_PROTOCOL);
        } else if(v == mAlipayCheckIv) {
            if(mPayType != PAY_TYPE_ALI) {
                setPayType(PAY_TYPE_ALI);
            }
        } else if(v == mWxCheckIv) {
            if(mPayType != PAY_TYPE_WX) {
                setPayType(PAY_TYPE_WX);
            }
        }
    }

    /***
     * 同意并开通
     */
    private void openVip() {
        if(mProductInfo == null) {
            ToastUtil.toastLongMessage("请先选择商品");
            return;
        }
        String couponId = mCouponInfo == null ? "" : mCouponInfo.id;
        new PayOrderRequest(this, mPayType, mProductInfo.id, couponId).schedule(true, new RequestListener<PayInfo>() {
            @Override
            public void onSuccess(PayInfo result) {
                if(mPayType == PAY_TYPE_WX) {
                    wxPay(result);
                } else {
                    aliPay(result.orderInfo);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void aliPay(String info) {
        final String orderInfo = info;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipOpenActivity.this);
                Map<String,String> result = alipay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = WHAT_SDK_PAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        ThreadUtils.execute(payRunnable);
    }

    private void gotoWebPage(String url) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    private static final int WHAT_SDK_PAY = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SDK_PAY:
                    Object obj = msg.obj;
                    break;
            }
        };
    };
}