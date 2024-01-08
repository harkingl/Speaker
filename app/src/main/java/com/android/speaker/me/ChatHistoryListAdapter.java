package com.android.speaker.me;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 对话历史adapter
 */
public class ChatHistoryListAdapter extends BaseListItemAdapter<ChatReportInfo> {
    public ChatHistoryListAdapter(Context context, List<ChatReportInfo> list) {
        super(context, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_history_item, null);
            holder.titleTv = convertView.findViewById(R.id.item_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatReportInfo info = items.get(position);
        holder.titleTv.setText(info.id);

        return convertView;
    }

    class ViewHolder {
        public TextView titleTv;
    }

}
