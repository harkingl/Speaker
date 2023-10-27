package com.android.speaker.favorite;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.course.CoursePreviewActivity;
import com.android.speaker.course.CourseUtil;
import com.android.speaker.listen.BlogDetailActivity;
import com.android.speaker.listen.ScenePlayListActivity;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 收藏adapter
 */
public class FavoriteListAdapter extends BaseListItemAdapter<FavoriteItem> {
    public FavoriteListAdapter(Context context, List<FavoriteItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.favorite_item, null);
            holder.typeTv = convertView.findViewById(R.id.item_type_tv);
            holder.titleTv = convertView.findViewById(R.id.item_title_tv);
            holder.dateTv = convertView.findViewById(R.id.item_date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FavoriteItem info = items.get(position);
        holder.typeTv.setText(getTypeName(info.type));
        holder.titleTv.setText(info.name);
        holder.dateTv.setText(info.date);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFavoriteDetailPage(info);
            }
        });
        return convertView;
    }

    private String getTypeName(int type) {
        String text = "";
        switch (type) {
            case FavoriteItem.TYPE_STREAM:
                text = "[场景连播]";
                break;
            case FavoriteItem.TYPE_BLOG:
                text = "[英语博客]";
                break;
            case FavoriteItem.TYPE_QUALITY:
                text = "[精品课程]";
                break;
            case FavoriteItem.TYPE_SPECIAL:
                text = "[专项课程]";
                break;
            default:
                text = "[场景课程]";
                break;
        }

        return text;
    }

    private void gotoFavoriteDetailPage(FavoriteItem item) {
        switch (item.type) {
            case FavoriteItem.TYPE_BLOG:
                Intent i = new Intent(context, BlogDetailActivity.class);
                i.putExtra("blog_id", item.id);
                context.startActivity(i);
                break;
            case FavoriteItem.TYPE_STREAM:
                Intent intent = new Intent(context, ScenePlayListActivity.class);
                intent.putExtra("scene_play_id", item.id);
                intent.putExtra("scene_play_title", item.name);
                context.startActivity(intent);
                break;
            case FavoriteItem.TYPE_SCENE:
            case FavoriteItem.TYPE_SPECIAL:
            case FavoriteItem.TYPE_QUALITY:
                gotoCoursePreview(item);
                break;
        }
    }

    private void gotoCoursePreview(FavoriteItem item) {
        int type = CourseUtil.TYPE_COURSE_PROJECT;
        if(item.type == FavoriteItem.TYPE_QUALITY) {
            type = CourseUtil.TYPE_COURSE_CATALOG;
        } else if(item.type == FavoriteItem.TYPE_SPECIAL) {
            type = CourseUtil.TYPE_COURSE_SPECIAL;
        }
        Intent i = new Intent(context, CoursePreviewActivity.class);
        i.putExtra(CourseUtil.KEY_COURSE_ID, item.id);
        i.putExtra(CourseUtil.KEY_COURSE_TYPE, type);
        context.startActivity(i);
    }

    class ViewHolder {
        public TextView typeTv;
        public TextView titleTv;
        public TextView dateTv;
    }
}
