package com.android.speaker.me;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.listen.BlogDetailActivity;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 博客学习记录adapter
 */
public class BlogRecordListAdapter extends BaseListItemAdapter<RecordInfo> {
    public BlogRecordListAdapter(Context context, List<RecordInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.blog_record_item, null);
            holder.titleTv = convertView.findViewById(R.id.item_title_tv);
            holder.desTv = convertView.findViewById(R.id.item_des_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordInfo info = items.get(position);
        holder.titleTv.setText(info.title);
        holder.desTv.setText(info.des);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail(info);
            }
        });
        return convertView;
    }

    private void gotoDetail(RecordInfo item) {
        Intent i = new Intent(context, BlogDetailActivity.class);
        i.putExtra("blog_id", item.id);
        context.startActivity(i);
    }

    class ViewHolder {
        public TextView titleTv;
        public TextView desTv;
    }
}
