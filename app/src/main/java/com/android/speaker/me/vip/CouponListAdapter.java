package com.android.speaker.me.vip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class CouponListAdapter extends BaseListItemAdapter<CouponInfo> {
    private int type;

    public CouponListAdapter(Context context, List<CouponInfo> list, int type) {
        super(context, list);

        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            if(type == CouponListEntity.TYPE_NOT_USE) {
                convertView = inflater.inflate(R.layout.coupon_list_item, null);
            } else {
                convertView = inflater.inflate(R.layout.coupon_list_item2, null);
            }
            holder.amountTv = convertView.findViewById(R.id.coupon_item_amount_tv);
            holder.nameTv = convertView.findViewById(R.id.coupon_item_name_tv);
            holder.descTv = convertView.findViewById(R.id.coupon_item_desc_tv);
            holder.timeTv = convertView.findViewById(R.id.coupon_item_valid_time_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        CouponInfo info = items.get(position);

        holder.amountTv.setText(info.faceValue + "");
        holder.nameTv.setText(info.name);
        holder.descTv.setText(info.remark);
        holder.timeTv.setText(info.validityBeginTime + " - " + info.validityEndTime);

        return convertView;
    }

    public class ViewHolder {
        public TextView amountTv;
        public TextView nameTv;
        public TextView descTv;
        public TextView timeTv;
    }
}
