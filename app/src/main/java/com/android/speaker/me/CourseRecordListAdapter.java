package com.android.speaker.me;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.course.CoursePreviewActivity;
import com.android.speaker.course.CourseUtil;
import com.android.speaker.util.TimeUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 课程学习记录adapter
 */
public class CourseRecordListAdapter extends BaseListItemAdapter<RecordInfo> {
    public CourseRecordListAdapter(Context context, List<RecordInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.course_record_item, null);
            holder.typeTv = convertView.findViewById(R.id.item_type_tv);
            holder.titleTv = convertView.findViewById(R.id.item_title_tv);
            holder.dateTv = convertView.findViewById(R.id.item_date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordInfo info = items.get(position);
        if(!TextUtils.isEmpty(info.type)) {
            holder.typeTv.setText("[" + info.type + "]");
        } else {
            holder.typeTv.setText("");
        }
        holder.titleTv.setText(info.title);
        holder.dateTv.setText(TimeUtil.getStringFromDate(TimeUtil.FORMAT_YYYYMMDDHHMM, info.date));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCoursePreview(info);
            }
        });
        return convertView;
    }

    private void gotoCoursePreview(RecordInfo item) {
        int type = CourseUtil.TYPE_COURSE_PROJECT;
        if("精品课程".equals(item.type)) {
            type = CourseUtil.TYPE_COURSE_CATALOG;
        } else if("专项课程".equals(item.type)) {
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
