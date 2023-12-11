package com.android.speaker.me.vip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.base.component.NoScrollListView;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class CouponListFragment extends BaseFragment {
    private NoScrollListView mListView;
    private ImageView mEmptyIv;
    private CouponListAdapter mAdapter;
    private List<CouponInfo> mList;
    private int mType;

    public CouponListFragment(List<CouponInfo> list, int type) {
        this.mList = list;
        this.mType = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_list, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        initData();
    }

    private void initView(View view) {
        mListView = view.findViewById(R.id.coupon_lv);
        mEmptyIv = view.findViewById(R.id.coupon_empty_iv);
    }

    private void initData() {
        if(mList == null || mList.size() == 0) {
            mEmptyIv.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mAdapter = new CouponListAdapter(getActivity(), mList, mType);
            mListView.setAdapter(mAdapter);
            mEmptyIv.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }
}
