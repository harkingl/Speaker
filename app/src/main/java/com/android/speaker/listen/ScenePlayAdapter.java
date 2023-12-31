package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.course.CourseItem;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 场景连播
 */
public class ScenePlayAdapter extends BaseListItemAdapter<ScenePlayItem> {
    public ScenePlayAdapter(Context context, List<ScenePlayItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_play_item, null);
            holder.iv = convertView.findViewById(R.id.scene_play_item_img_iv);
            holder.titleTv = convertView.findViewById(R.id.scene_play_item_title_tv);
            holder.countTv = convertView.findViewById(R.id.scene_play_item_count_tv);

            int itemHeight = ScreenUtil.dip2px(108);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight);
            holder.iv.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScenePlayItem info = items.get(position);
        GlideUtil.loadCornerImage(holder.iv, info.iconUrl, null, 10);
        holder.countTv.setText(context.getString(R.string.scene_play_count, info.count));
        holder.titleTv.setText(info.title);

        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView titleTv;
        public TextView countTv;
    }
}
