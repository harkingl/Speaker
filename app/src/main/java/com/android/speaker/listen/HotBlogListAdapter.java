package com.android.speaker.listen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.util.GlideUtil;
import com.android.speaker.util.ScreenUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 热门博客adapter
 */
public class HotBlogListAdapter extends BaseListItemAdapter<BlogItem> {
    private int mColumnCount = 3;
    private int mRowCount = 3;
    public HotBlogListAdapter(Context context, List<BlogItem> list, int columnCount) {
        super(context, list);

        this.mColumnCount = columnCount;
    }

    @Override
    public int getCount() {
        return items.size() % mRowCount == 1 ? items.size()+1 : items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.hot_blog_item, null);
            holder.iv = convertView.findViewById(R.id.blog_item_img_iv);
            holder.numTv = convertView.findViewById(R.id.blog_item_num_tv);
            holder.titleTv = convertView.findViewById(R.id.blog_item_title_tv);
            holder.timeTv = convertView.findViewById(R.id.blog_item_time_tv);

//            int itemHeight = ScreenUtil.dip2px(190);
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, itemHeight);
//            convertView.setLayoutParams(params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(items.size() % mRowCount == 1 && position == mColumnCount*2-1) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            int actualPos = (position/mColumnCount) + (position%mColumnCount)*mRowCount;
            BlogItem info = items.get(actualPos);
            int itemSize = ScreenUtil.dip2px(60);
            GlideUtil.loadCornerImage(holder.iv, info.iconUrl, null, 10, itemSize, itemSize);
            holder.numTv.setText((actualPos+1) + "");
            holder.titleTv.setText(info.titleZh);
            holder.timeTv.setText(context.getString(R.string.blog_item_time, info.publishTime, info.audioDuration));
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView numTv;
        public TextView titleTv;
        public TextView timeTv;
    }
}
