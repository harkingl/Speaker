package com.android.speaker.me;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.text.DecimalFormat;
import java.util.List;

/***
 * 订单adapter
 */
public class OrderListAdapter extends BaseListItemAdapter<OrderInfo> {
    private static final DecimalFormat MY_FORMAT = new DecimalFormat("0.00");

    public OrderListAdapter(Context context, List<OrderInfo> list) {
        super(context, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_item, null);
            holder.orderNoTv = convertView.findViewById(R.id.item_order_no_tv);
            holder.dateTv = convertView.findViewById(R.id.item_order_date_tv);
            holder.typeIv = convertView.findViewById(R.id.item_order_type_iv);
            holder.nameTv = convertView.findViewById(R.id.item_order_name_tv);
            holder.originalPriceTv = convertView.findViewById(R.id.item_order_original_price_tv);
            holder.detailTv = convertView.findViewById(R.id.item_order_detail_tv);
            holder.countTv = convertView.findViewById(R.id.item_order_count_tv);
            holder.actualPriceTv = convertView.findViewById(R.id.item_order_actual_price_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderInfo info = items.get(position);
        holder.orderNoTv.setText(info.orderNumber);
        holder.dateTv.setText(info.orderDate);
        if("0".equals(info.productType)) {
            holder.typeIv.setImageResource(R.drawable.ic_order_type_vip);
        } else {
            holder.typeIv.setImageResource(R.drawable.ic_order_type_course);
        }
        holder.nameTv.setText(info.product);
        holder.originalPriceTv.setText("¥" + MY_FORMAT.format(info.originalPrice));
        holder.detailTv.setText(info.productDetails);
        holder.countTv.setText("×" + info.count);
        holder.actualPriceTv.setText("实付款 ¥" + MY_FORMAT.format(info.actualPayment));

        return convertView;
    }

    class ViewHolder {
        public TextView orderNoTv;
        public TextView dateTv;
        public ImageView typeIv;
        public TextView nameTv;
        public TextView originalPriceTv;
        public TextView detailTv;
        public TextView countTv;
        public TextView actualPriceTv;
    }

}
