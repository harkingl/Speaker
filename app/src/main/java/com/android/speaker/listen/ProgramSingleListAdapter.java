package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.util.GlideUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 博客详情-单集列表
 */
public class ProgramSingleListAdapter extends BaseListItemAdapter<BlogItem> {
    public ProgramSingleListAdapter(Context context, List<BlogItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.program_single_item, null);
            holder.titleTv = convertView.findViewById(R.id.program_title_tv);
            holder.desTv = convertView.findViewById(R.id.program_des_tv);
            holder.timeTv = convertView.findViewById(R.id.program_time_tv);
            holder.playIv = convertView.findViewById(R.id.program_play_iv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BlogItem info = items.get(position);
        holder.titleTv.setText(info.titleEn);
        holder.desTv.setText(info.des);
        holder.timeTv.setText(context.getString(R.string.blog_item_time, info.publishTime, info.audioDuration));
        holder.playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView titleTv;
        public TextView desTv;
        public TextView timeTv;
        public ImageView playIv;
    }
}
