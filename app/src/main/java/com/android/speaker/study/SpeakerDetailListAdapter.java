package com.android.speaker.study;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 开口说详情列表
 */
public class SpeakerDetailListAdapter extends BaseListItemAdapter<ExampleInfo> {
    public SpeakerDetailListAdapter(Context context, List<ExampleInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.speaker_detail_list_item, null);
            holder.titleZhTv = convertView.findViewById(R.id.item_title_zh_tv);
            holder.titleEnTv = convertView.findViewById(R.id.item_title_en_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExampleInfo info = items.get(position);
        holder.titleZhTv.setText(info.titleZh);
        holder.titleEnTv.setText(info.titleEn);

        return convertView;
    }

    class ViewHolder {
        public TextView titleZhTv;
        public TextView titleEnTv;
    }
}
