package com.android.speaker.me.vip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class CouponReceiveListAdapter extends BaseListItemAdapter<CouponInfo> {

    public CouponReceiveListAdapter(Context context, List<CouponInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.coupon_receive_list_item, null);
            holder.nameTv = convertView.findViewById(R.id.coupon_item_name_tv);
            holder.valueTv = convertView.findViewById(R.id.coupon_item_value_tv);
            holder.remarkTv = convertView.findViewById(R.id.coupon_item_remark_tv);
            holder.receivedTv = convertView.findViewById(R.id.coupon_item_received_tv);
            holder.tobeReceiveTv = convertView.findViewById(R.id.coupon_item_tobe_receive_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        CouponInfo info = items.get(position);
        holder.nameTv.setText(info.name);
        holder.valueTv.setText(info.faceValue + "");
        holder.remarkTv.setText(info.remark);
        if(info.isLQ) {
            holder.tobeReceiveTv.setVisibility(View.GONE);
            holder.receivedTv.setVisibility(View.VISIBLE);
        } else {
            holder.receivedTv.setVisibility(View.GONE);
            holder.tobeReceiveTv.setVisibility(View.VISIBLE);
            holder.tobeReceiveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doReceive(info);
                }
            });
        }

        return convertView;
    }

    private void doReceive(CouponInfo info) {
        new GetCouponByIdRequest(context, info.id).schedule(false, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                ToastUtil.toastLongMessage("领取成功");
                info.isLQ = true;
                notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    public class ViewHolder {
        public TextView nameTv;
        public TextView valueTv;
        public TextView remarkTv;
        public TextView receivedTv;
        public TextView tobeReceiveTv;
    }
}
