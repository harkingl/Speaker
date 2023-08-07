package com.android.speaker.course;

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
 * 精品和专项课程adapter
 */
public class QualityCourseAdapter extends BaseListItemAdapter<CourseItem> {
    public QualityCourseAdapter(Context context, List<CourseItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.quality_course_item, null);
            holder.iv = convertView.findViewById(R.id.course_item_img_iv);
            holder.titleTv = convertView.findViewById(R.id.course_item_title_tv);
            holder.descTv = convertView.findViewById(R.id.course_item_desc_tv);

//            int itemHeight = ScreenUtil.dip2px(190);
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, itemHeight);
//            convertView.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CourseItem info = items.get(position);
        GlideUtil.loadCornerImage(holder.iv, info.homePage, null, 10);
        holder.titleTv.setText(info.title);
        holder.descTv.setText(info.des);

        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView titleTv;
        public TextView descTv;
    }
}
