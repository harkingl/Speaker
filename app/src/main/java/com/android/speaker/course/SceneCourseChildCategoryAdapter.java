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

public class SceneCourseChildCategoryAdapter extends BaseListItemAdapter<CategoryItem> {
    private int selectIndex;
    public SceneCourseChildCategoryAdapter(Context context, List<CategoryItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_course_child_category_item, null);
            holder.flagView = convertView.findViewById(R.id.scene_course_child_flag_view);
            holder.nameTv = convertView.findViewById(R.id.scene_course_child_category_name_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryItem info = items.get(position);
        holder.nameTv.setText(info.name);
        if(position == selectIndex) {
            if(position == 0) {
                convertView.setBackgroundResource(R.drawable.category_item_white_bg_shape);
            } else {
                convertView.setBackgroundColor(context.getColor(R.color.white));
            }
            holder.flagView.setVisibility(View.VISIBLE);
            holder.nameTv.setTextColor(context.getColor(R.color.common_green_color));
        } else {
            if(position == 0) {
                convertView.setBackgroundResource(R.drawable.category_item_gray_bg_shape);
            } else {
                convertView.setBackgroundColor(context.getColor(R.color.common_line_color));
            }
            holder.flagView.setVisibility(View.GONE);
            holder.nameTv.setTextColor(context.getColor(R.color.text_color_1));
        }

        return convertView;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    public void setData(List<CategoryItem> list) {
        items.clear();
        if(list != null) {
            items.addAll(list);
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        public View flagView;
        public TextView nameTv;
    }
}
