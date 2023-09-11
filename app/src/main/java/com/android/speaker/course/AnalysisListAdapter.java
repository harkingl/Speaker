package com.android.speaker.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class AnalysisListAdapter extends BaseListItemAdapter<CourseLectureDetail.AnalysisItem> {
    // 中文翻译是否打开
    private boolean mIsOpen;

    public AnalysisListAdapter(Context context, List<CourseLectureDetail.AnalysisItem> list, boolean isOpen) {
        super(context, list);

        this.mIsOpen = isOpen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lecture_analysis_item, null);
            holder.titleEnTv = convertView.findViewById(R.id.analysis_item_title_en_tv);
            holder.titleChTv = convertView.findViewById(R.id.analysis_item_title_ch_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CourseLectureDetail.AnalysisItem info = items.get(position);
        holder.titleEnTv.setText(info.sentence);
        if(mIsOpen) {
            holder.titleChTv.setText(info.translation);
            holder.titleChTv.setVisibility(View.VISIBLE);
        } else {
            holder.titleChTv.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }

    public void setIsOpen(boolean isOpen) {
        this.mIsOpen = isOpen;
    }

    class ViewHolder {
        public TextView titleEnTv;
        public TextView titleChTv;
    }
}
