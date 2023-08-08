package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 节目精选
 */
public class ProgramListAdapter extends BaseListItemAdapter<ProgramItem> {
    private int imgSize;
    public ProgramListAdapter(Context context, List<ProgramItem> list, int imgSize) {
        super(context, list);
        this.imgSize = imgSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.program_list_item, null);
            holder.iv = convertView.findViewById(R.id.program_iv);
            holder.titleTv = convertView.findViewById(R.id.program_title_tv);
            holder.typeTv = convertView.findViewById(R.id.program_type_tv);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgSize, imgSize);
            holder.iv.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramItem info = items.get(position);
        GlideUtil.loadCornerImage(holder.iv, info.iconUrl, null, 10, imgSize, imgSize);
        holder.typeTv.setText(info.des);
        holder.titleTv.setText(info.title);

        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView titleTv;
        public TextView typeTv;
    }
}
