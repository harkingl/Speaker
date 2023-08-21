package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class PlayAllListAdapter extends BaseListItemAdapter<ScenePlayDataItem> {
    private int selectIndex = -1;
    public PlayAllListAdapter(Context context, List<ScenePlayDataItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.play_all_list_item, null);
            holder.numTv = convertView.findViewById(R.id.play_item_num_tv);
            holder.titleTv = convertView.findViewById(R.id.play_item_title_tv);
            holder.vipTv = convertView.findViewById(R.id.play_item_vip_tv);
            holder.levelTv = convertView.findViewById(R.id.play_item_level_tv);
            holder.descTv = convertView.findViewById(R.id.play_item_desc_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScenePlayDataItem info = items.get(position);
        holder.numTv.setText((position+1) + "");
        holder.titleTv.setText(info.title);
        holder.vipTv.setVisibility(info.type == 1 ? View.VISIBLE : View.GONE);
        holder.levelTv.setText(info.leveName);
        holder.descTv.setText(info.des);

        if(position == selectIndex) {
            holder.numTv.setTextColor(context.getColor(R.color.common_green_color));
            holder.titleTv.setTextColor(context.getColor(R.color.common_green_color));
            holder.descTv.setTextColor(context.getColor(R.color.common_green_color));
        } else {
            holder.numTv.setTextColor(context.getColor(R.color.text_color_1));
            holder.titleTv.setTextColor(context.getColor(R.color.text_color_1));
            holder.descTv.setTextColor(context.getColor(R.color.text_color_1));
        }

        return convertView;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    class ViewHolder {
        public TextView numTv;
        public TextView titleTv;
        public TextView vipTv;
        public TextView levelTv;
        public TextView descTv;
    }
}
