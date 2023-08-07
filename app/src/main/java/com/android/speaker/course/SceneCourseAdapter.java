package com.android.speaker.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class SceneCourseAdapter extends BaseListItemAdapter<CourseItem> {
    public SceneCourseAdapter(Context context, List<CourseItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_course_item, null);
            holder.iv = convertView.findViewById(R.id.course_item_img_iv);
            holder.freeLabelTv = convertView.findViewById(R.id.course_item_free_label_tv);
            holder.titleTv = convertView.findViewById(R.id.course_item_title_tv);
            holder.aiLabelTv = convertView.findViewById(R.id.course_item_ai_tv);
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
        String labelText = info.type == 0 ? "限时免费" : "VIP专享";
        holder.freeLabelTv.setText(labelText);
        holder.aiLabelTv.setText(info.leveName);
        holder.titleTv.setText(info.title);
        holder.descTv.setText(info.des);

        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView freeLabelTv;
        public TextView titleTv;
        public TextView aiLabelTv;
        public TextView descTv;
    }
}
