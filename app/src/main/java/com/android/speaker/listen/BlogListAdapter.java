package com.android.speaker.listen;

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

/***
 * 博客adapter
 */
public class BlogListAdapter extends BaseListItemAdapter<BlogItem> {
    public BlogListAdapter(Context context, List<BlogItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.blog_item, null);
            holder.iv = convertView.findViewById(R.id.blog_item_img_iv);
            holder.titleTv = convertView.findViewById(R.id.blog_item_title_tv);
            holder.timeTv = convertView.findViewById(R.id.blog_item_time_tv);

//            int itemHeight = ScreenUtil.dip2px(190);
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, itemHeight);
//            convertView.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BlogItem info = items.get(position);
        GlideUtil.loadCornerImage(holder.iv, info.iconUrl, null, 10);
        holder.titleTv.setText(info.titleZh);
        holder.timeTv.setText(context.getString(R.string.blog_item_time, info.publishTime, info.audioDuration));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBlogDetailPage(info);
            }
        });
        return convertView;
    }

    private void gotoBlogDetailPage(BlogItem item) {
        Intent i = new Intent(context, BlogDetailActivity.class);
        i.putExtra("blog_id", item.id);
        context.startActivity(i);
    }

    class ViewHolder {
        public ImageView iv;
        public TextView titleTv;
        public TextView timeTv;
    }
}
