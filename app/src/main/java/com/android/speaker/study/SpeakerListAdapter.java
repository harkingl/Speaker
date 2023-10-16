package com.android.speaker.study;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.util.GlideUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class SpeakerListAdapter extends BaseListItemAdapter<OpenSpeakerInfo> {
    public SpeakerListAdapter(Context context, List<OpenSpeakerInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.speaker_list_item, null);
            holder.iv = convertView.findViewById(R.id.speaker_item_img_iv);
            holder.titleTv = convertView.findViewById(R.id.speaker_item_title_tv);
            holder.descTv = convertView.findViewById(R.id.speaker_item_desc_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OpenSpeakerInfo info = items.get(position);
        GlideUtil.loadCornerImage(holder.iv, info.ossUrl, null, 10);
        holder.titleTv.setText(info.title);
        holder.descTv.setText(info.des);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SpeakerDetailActivity.class);
                i.putExtra("open_speak_id", info.id);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView titleTv;
        public TextView descTv;
    }
}
