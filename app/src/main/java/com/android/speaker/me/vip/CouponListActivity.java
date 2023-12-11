package com.android.speaker.me.vip;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.base.component.TitleTagView;
import com.android.speaker.listen.IBlogDetailCallBack;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

public class CouponListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CouponListActivity";
    private static final int TAB_INDEX_UNUSED = 0;
    private static final int TAB_INDEX_USED = 1;
    private static final int TAB_INDEX_EXIRE = 2;

    private TitleBarLayout mTitleBarLayout;
    private TitleTagView mUnUsedTitleView;
    private TitleTagView mUsedTitleView;
    private TitleTagView mExireTitleView;
    private FrameLayout mContainerFl;
    private CouponListFragment mUnusedFragment;
    private CouponListFragment mUsedFragment;
    private CouponListFragment mExireFragment;
    private int mCurrentTab = -1;
    private CouponListEntity mEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.coupon_title_bar);
        mUnUsedTitleView = findViewById(R.id.coupon_title_not_use_tv);
        mUsedTitleView = findViewById(R.id.coupon_title_have_use_tv);
        mExireTitleView = findViewById(R.id.coupon_title_exire_use_tv);
        mContainerFl = findViewById(R.id.coupon_container_fl);

        mUnUsedTitleView.setOnClickListener(this);
        mUsedTitleView.setOnClickListener(this);
        mExireTitleView.setOnClickListener(this);
    }

    private void configTitleBar() {
        mTitleBarLayout.setTitle("优惠券", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        new GetCouponListRequest(this).schedule(false, new RequestListener<CouponListEntity>() {
            @Override
            public void onSuccess(CouponListEntity result) {
                mEntity = result;
                changeTag(TAB_INDEX_UNUSED);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void changeTag(int index) {
        if(mEntity == null || mCurrentTab == index) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case TAB_INDEX_UNUSED:
                mUnUsedTitleView.setColorBlockViewVisible(true);
                mUsedTitleView.setColorBlockViewVisible(false);
                mExireTitleView.setColorBlockViewVisible(false);
                if(mUnusedFragment == null) {
                    mUnusedFragment = new CouponListFragment(mEntity.notUseList, CouponListEntity.TYPE_NOT_USE);
                    transaction.add(R.id.coupon_container_fl, mUnusedFragment);
                }
                transaction.show(mUnusedFragment);
                mCurrentTab = TAB_INDEX_UNUSED;
                break;
            case TAB_INDEX_USED:
                mUsedTitleView.setColorBlockViewVisible(true);
                mUnUsedTitleView.setColorBlockViewVisible(false);
                mExireTitleView.setColorBlockViewVisible(false);
                if(mUsedFragment == null) {
                    mUsedFragment = new CouponListFragment(mEntity.havedUseList, CouponListEntity.TYPE_HAVED_USE);
                    transaction.add(R.id.coupon_container_fl, mUsedFragment);
                }
                transaction.show(mUsedFragment);
                mCurrentTab = TAB_INDEX_USED;
                break;
            case TAB_INDEX_EXIRE:
                mExireTitleView.setColorBlockViewVisible(true);
                mUnUsedTitleView.setColorBlockViewVisible(false);
                mUsedTitleView.setColorBlockViewVisible(false);
                if(mExireFragment == null) {
                    mExireFragment = new CouponListFragment(mEntity.exipreUseList, CouponListEntity.TYPE_EXIPRE_USE);
                    transaction.add(R.id.coupon_container_fl, mExireFragment);
                }
                transaction.show(mExireFragment);
                mCurrentTab = TAB_INDEX_EXIRE;
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction tran) {
        if (null != mUnusedFragment) {
            tran.hide(mUnusedFragment);
        }
        if (null != mUsedFragment) {
            tran.hide(mUsedFragment);
        }
        if (null != mExireFragment) {
            tran.hide(mExireFragment);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mUnUsedTitleView) {
            changeTag(TAB_INDEX_UNUSED);
        } else if(v == mUsedTitleView) {
            changeTag(TAB_INDEX_USED);
        } else if(v == mExireTitleView) {
            changeTag(TAB_INDEX_EXIRE);
        }
    }
}
