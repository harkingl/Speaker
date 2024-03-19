package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class DialogPlayListAdapter extends BaseListItemAdapter<ScenePlayDataItem> {
    private ICallback mCallback;
    private int selectIndex = -1;
    public DialogPlayListAdapter(Context context, List<ScenePlayDataItem> list, ICallback callback) {
        super(context, list);

        this.mCallback = callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_play_list_item, null);
            holder.titleTv = convertView.findViewById(R.id.dialog_play_item_title_tv);
            holder.descTv = convertView.findViewById(R.id.dialog_play_item_desc_tv);
            holder.deleteIv = convertView.findViewById(R.id.dialog_play_item_delete_iv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScenePlayDataItem info = items.get(position);
        holder.titleTv.setText(info.title);
        holder.descTv.setText(info.des);
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null) {
                    mCallback.doDelete(position);
                }
            }
        });

        if(position == selectIndex) {
            holder.titleTv.setTextColor(context.getColor(R.color.common_green_color));
            holder.descTv.setTextColor(context.getColor(R.color.common_green_color));
        } else {
            holder.titleTv.setTextColor(context.getColor(R.color.text_color_1));
            holder.descTv.setTextColor(context.getColor(R.color.text_color_1));
        }

        return convertView;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView titleTv;
        public TextView descTv;
        public ImageView deleteIv;
    }

    interface ICallback {
        void doDelete(int position);
    }
}
