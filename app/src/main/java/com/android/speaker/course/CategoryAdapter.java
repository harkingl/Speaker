package com.android.speaker.course;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class CategoryAdapter extends BaseListItemAdapter<CategoryItem> {
    private int selectIndex;
    public CategoryAdapter(Context context, List<CategoryItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_type_item, null);
            holder.nameTv = convertView.findViewById(R.id.course_scene_type_name_tv);
            holder.sliderView = convertView.findViewById(R.id.course_scene_type_slider);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryItem info = items.get(position);
        holder.nameTv.setText(info.name);
        if(position == selectIndex) {
            holder.nameTv.setTextSize(18);
            holder.nameTv.setTextColor(context.getColor(R.color.text_color_1));
            holder.nameTv.setTypeface(Typeface.DEFAULT_BOLD);
            holder.sliderView.setVisibility(View.VISIBLE);
        } else {
            holder.nameTv.setTextSize(16);
            holder.nameTv.setTextColor(context.getColor(R.color.text_color_48496E));
            holder.nameTv.setTypeface(Typeface.DEFAULT);
            holder.sliderView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    class ViewHolder {
        public TextView nameTv;
        public View sliderView;
    }
}
