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

public class SceneCourseCategoryAdapter extends BaseListItemAdapter<CategoryItem> {
    private int selectIndex;
    public SceneCourseCategoryAdapter(Context context, List<CategoryItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_course_category_item, null);
            holder.imgIv = convertView.findViewById(R.id.scene_course_category_iv);
            holder.nameTv = convertView.findViewById(R.id.scene_course_category_name_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryItem info = items.get(position);
        GlideUtil.loadCircleImage(holder.imgIv, info.imageUrl, null);
        holder.nameTv.setText(info.name);
        if(position == selectIndex) {
            holder.nameTv.setTextColor(context.getColor(R.color.common_purple_color));
            holder.nameTv.setBackgroundResource(R.drawable.white_bg_shape);
        } else {
            holder.nameTv.setTextColor(context.getColor(R.color.text_color_CCFFFFFF));
            holder.nameTv.setBackground(null);
        }

        return convertView;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    class ViewHolder {
        public ImageView imgIv;
        public TextView nameTv;
    }
}
