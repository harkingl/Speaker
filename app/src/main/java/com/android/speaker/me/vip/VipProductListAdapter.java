package com.android.speaker.me.vip;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.listen.ProgramItem;
import com.android.speaker.util.GlideUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 商品列表
 */
public class VipProductListAdapter extends BaseListItemAdapter<ProductInfo> {
    private int selectIndex = 0;
    public VipProductListAdapter(Context context, List<ProductInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.vip_product_item, null);
            holder.parentLayout = convertView.findViewById(R.id.item_parent_ll);
            holder.nameTv = convertView.findViewById(R.id.item_name_tv);
            holder.priceTv = convertView.findViewById(R.id.item_price_tv);
            holder.originalPriceTv = convertView.findViewById(R.id.item_original_price_tv);
            holder.descTv = convertView.findViewById(R.id.item_desc_tv);
            holder.tagTv = convertView.findViewById(R.id.item_tag_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductInfo info = items.get(position);
        holder.parentLayout.setBackgroundResource(position == selectIndex ? R.drawable.product_select_bg_shape : R.drawable.white_bg_with_border_shape);
        holder.nameTv.setText(info.name);
        holder.priceTv.setText("￥" + info.price);
        holder.originalPriceTv.setText("￥" + info.originPrice);
        holder.originalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.descTv.setText(info.description);
        if(!TextUtils.isEmpty(info.tag)) {
            holder.tagTv.setText(info.tag);
            holder.tagTv.setVisibility(View.VISIBLE);
        } else {
            holder.tagTv.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return selectIndex;
    }

//    public void setData(List<ProductInfo> list) {
//        this.items.clear();
//        if(list != null) {
//            this.items.addAll(list);
//        }
//        selectIndex = 0;
//        notifyDataSetChanged();
//    }

    class ViewHolder {
        public View parentLayout;
        public TextView nameTv;
        public TextView priceTv;
        public TextView originalPriceTv;
        public TextView descTv;
        public TextView tagTv;
    }
}
