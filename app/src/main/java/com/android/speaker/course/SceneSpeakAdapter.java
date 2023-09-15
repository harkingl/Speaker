package com.android.speaker.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class SceneSpeakAdapter extends BaseListItemAdapter<SceneSpeakDetail.SpeakItem> {
    // 中文翻译是否打开
    private boolean mIsOpen;
    private int selectIndex = -1;
    private String mLeftName;

    public SceneSpeakAdapter(Context context, List<SceneSpeakDetail.SpeakItem> list, boolean isOpen) {
        super(context, list);

        this.mIsOpen = isOpen;
        if(list != null && list.size() > 0) {
            mLeftName = list.get(0).name;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scene_speak_item, null);
            holder.leftLayout = convertView.findViewById(R.id.item_left_parent_rl);
            holder.leftIconTv = convertView.findViewById(R.id.item_left_icon_tv);
            holder.leftNameTv = convertView.findViewById(R.id.item_left_name_tv);
            holder.leftContentLayout = convertView.findViewById(R.id.item_left_content_ll);
            holder.leftTitleEnTv = convertView.findViewById(R.id.item_left_title_en_tv);
            holder.leftTitleChTv = convertView.findViewById(R.id.item_left_title_ch_tv);
            holder.leftVoiceIv = convertView.findViewById(R.id.item_left_voice_iv);
            holder.rightLayout = convertView.findViewById(R.id.item_right_parent_rl);
            holder.rightIconTv = convertView.findViewById(R.id.item_right_icon_tv);
            holder.rightNameTv = convertView.findViewById(R.id.item_right_name_tv);
            holder.rightContentLayout = convertView.findViewById(R.id.item_right_content_ll);
            holder.rightTitleEnTv = convertView.findViewById(R.id.item_right_title_en_tv);
            holder.rightTitleChTv = convertView.findViewById(R.id.item_right_title_ch_tv);
            holder.rightVoiceIv = convertView.findViewById(R.id.item_right_voice_iv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        SceneSpeakDetail.SpeakItem info = items.get(position);
        if(info.name.equals(mLeftName)) {
            holder.leftIconTv.setText(info.name.charAt(0) + "");
            holder.leftNameTv.setText(info.name);
            holder.leftTitleEnTv.setText(info.content);
            if(mIsOpen) {
                holder.leftTitleChTv.setText(info.transfer);
                holder.leftTitleChTv.setVisibility(View.VISIBLE);
            } else {
                holder.leftTitleChTv.setVisibility(View.GONE);
            }
            if(selectIndex == position) {
                holder.leftIconTv.setBackgroundResource(R.drawable.green_circle_bg_shape);
                holder.leftNameTv.setTextColor(context.getColor(R.color.text_color_00B49B));
                holder.leftContentLayout.setBackgroundResource(R.drawable.chat_item_left_bg_select_shape);
                holder.leftTitleChTv.setTextColor(context.getColor(R.color.text_color_00B49B));
                holder.leftVoiceIv.setImageResource(R.drawable.chat_item_voice_select);
            } else {
                holder.leftIconTv.setBackgroundResource(R.drawable.purple_circle_bg_shape);
                holder.leftNameTv.setTextColor(context.getColor(R.color.text_color_2));
                holder.leftContentLayout.setBackgroundResource(R.drawable.chat_item_left_bg_shape);
                holder.leftTitleChTv.setTextColor(context.getColor(R.color.text_color_2));
                holder.leftVoiceIv.setImageResource(R.drawable.chat_item_voice_default);
            }
            holder.leftVoiceIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
        } else {
            holder.rightIconTv.setText(info.name.charAt(0) + "");
            holder.rightNameTv.setText(info.name);
            holder.rightTitleEnTv.setText(info.content);
            if(mIsOpen) {
                holder.rightTitleChTv.setText(info.transfer);
                holder.rightTitleChTv.setVisibility(View.VISIBLE);
            } else {
                holder.rightTitleChTv.setVisibility(View.GONE);
            }
            if(selectIndex == position) {
                holder.rightIconTv.setBackgroundResource(R.drawable.green_circle_bg_shape);
                holder.rightNameTv.setTextColor(context.getColor(R.color.text_color_00B49B));
                holder.rightContentLayout.setBackgroundResource(R.drawable.chat_item_right_bg_select_shape);
                holder.rightTitleChTv.setTextColor(context.getColor(R.color.text_color_00B49B));
                holder.rightVoiceIv.setImageResource(R.drawable.chat_item_voice_select);
            } else {
                holder.rightIconTv.setBackgroundResource(R.drawable.purple_circle_bg_shape);
                holder.rightNameTv.setTextColor(context.getColor(R.color.text_color_2));
                holder.rightContentLayout.setBackgroundResource(R.drawable.chat_item_right_bg_shape);
                holder.rightTitleChTv.setTextColor(context.getColor(R.color.text_color_2));
                holder.rightVoiceIv.setImageResource(R.drawable.chat_item_voice_default);
            }
            holder.rightVoiceIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setIsOpen(boolean isOpen) {
        this.mIsOpen = isOpen;
    }

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    class ViewHolder {
        public View leftLayout;
        public TextView leftIconTv;
        public TextView leftNameTv;
        public View leftContentLayout;
        public TextView leftTitleEnTv;
        public TextView leftTitleChTv;
        public ImageView leftVoiceIv;
        public View rightLayout;
        public TextView rightIconTv;
        public TextView rightNameTv;
        public View rightContentLayout;
        public TextView rightTitleEnTv;
        public TextView rightTitleChTv;
        public ImageView rightVoiceIv;
        public int position;
    }
}
