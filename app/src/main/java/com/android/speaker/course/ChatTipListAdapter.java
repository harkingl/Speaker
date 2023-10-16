package com.android.speaker.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 开口说-对话提示
 */
public class ChatTipListAdapter extends BaseListItemAdapter<String> {
    public ChatTipListAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_tip_list_item, null);
            holder.titleTv = convertView.findViewById(R.id.item_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String text = items.get(position);
        holder.titleTv.setText(text);

        return convertView;
    }

    class ViewHolder {
        public TextView titleTv;
    }
}
