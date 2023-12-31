package com.android.speaker.course;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

public class SpeakChatAdapter extends BaseListItemAdapter<ChatItem> {
    // 中文翻译是否打开
    private boolean mIsOpen;
    private int selectIndex = -1;
    private ICallBack mCallback;

    public SpeakChatAdapter(Context context, List<ChatItem> list, boolean isOpen) {
        super(context, list);

        this.mIsOpen = isOpen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.speak_chat_item, null);
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
            holder.rightStateIv = convertView.findViewById(R.id.item_right_state_iv);
            holder.rightStateTv = convertView.findViewById(R.id.item_right_state_tv);
            holder.rightArrowIv = convertView.findViewById(R.id.item_right_arrow_iv);
            holder.rightTitleLayout = convertView.findViewById(R.id.item_right_title_ll);
            holder.rightTitleEnTv = convertView.findViewById(R.id.item_right_title_en_tv);
            holder.rightVoiceIv = convertView.findViewById(R.id.item_right_voice_iv);
            holder.rightAnalysisLayout = convertView.findViewById(R.id.item_right_analysis_ll);
            holder.rightStandAnswerTv = convertView.findViewById(R.id.item_right_stand_answer_tv);
            holder.rightAnalysisTv = convertView.findViewById(R.id.item_right_analysis_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        ChatItem info = items.get(position);
        if(!info.isMySelf) {
            holder.leftIconTv.setText(info.name.charAt(0) + "");
            holder.leftNameTv.setText(info.name);
            holder.leftTitleEnTv.setText(info.content);
            if(mIsOpen) {
                holder.leftTitleChTv.setText(info.transfer);
                holder.leftTitleChTv.setVisibility(View.VISIBLE);
            } else {
                holder.leftTitleChTv.setVisibility(View.GONE);
            }
//            if(selectIndex == position) {
//                holder.leftIconTv.setBackgroundResource(R.drawable.green_circle_bg_shape);
//                holder.leftNameTv.setTextColor(context.getColor(R.color.text_color_00B49B));
//                holder.leftContentLayout.setBackgroundResource(R.drawable.chat_item_left_bg_select_shape);
//                holder.leftTitleChTv.setTextColor(context.getColor(R.color.text_color_00B49B));
//                holder.leftVoiceIv.setImageResource(R.drawable.chat_item_voice_select);
//            } else {
//                holder.leftIconTv.setBackgroundResource(R.drawable.purple_circle_bg_shape);
//                holder.leftNameTv.setTextColor(context.getColor(R.color.text_color_2));
//                holder.leftContentLayout.setBackgroundResource(R.drawable.chat_item_left_bg_shape);
//                holder.leftTitleChTv.setTextColor(context.getColor(R.color.text_color_2));
//                holder.leftVoiceIv.setImageResource(R.drawable.chat_item_voice_default);
//            }
            holder.leftVoiceIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallback != null) {
                        mCallback.doPlay(info);
                    }
                }
            });
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
        } else {
            holder.rightIconTv.setText(info.name.charAt(0) + "");
            holder.rightNameTv.setText(info.name);
            holder.rightTitleEnTv.setText(info.content);
//            if(mIsOpen) {
//                holder.rightTitleChTv.setText(info.transfer);
//                holder.rightTitleChTv.setVisibility(View.VISIBLE);
//            } else {
//                holder.rightTitleChTv.setVisibility(View.GONE);
//            }
//            if(selectIndex == position) {
//                holder.rightIconTv.setBackgroundResource(R.drawable.green_circle_bg_shape);
//                holder.rightNameTv.setTextColor(context.getColor(R.color.text_color_00B49B));
//                holder.rightContentLayout.setBackgroundResource(R.drawable.chat_item_right_bg_select_shape);
//                holder.rightTitleChTv.setTextColor(context.getColor(R.color.text_color_00B49B));
//                holder.rightVoiceIv.setImageResource(R.drawable.chat_item_voice_select);
//            } else {
//                holder.rightIconTv.setBackgroundResource(R.drawable.purple_circle_bg_shape);
//                holder.rightNameTv.setTextColor(context.getColor(R.color.text_color_2));
//                holder.rightContentLayout.setBackgroundResource(R.drawable.chat_item_right_bg_shape);
//                holder.rightTitleChTv.setTextColor(context.getColor(R.color.text_color_2));
//                holder.rightVoiceIv.setImageResource(R.drawable.chat_item_voice_default);
//            }
//            holder.rightVoiceIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mCallback != null) {
//                        mCallback.doPlay(info);
//                    }
//                }
//            });

            holder.rightArrowIv.setVisibility(View.GONE);
            holder.rightAnalysisLayout.setVisibility(View.GONE);
            setRightStateView(info, holder);
            holder.rightArrowIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.isAnalysisShow = !info.isAnalysisShow;
                    notifyDataSetChanged();
                }
            });
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void setRightStateView(ChatItem info, ViewHolder holder) {
        if(info.state == ChatItem.STATE_SEND_FAILED) {
            holder.rightStateIv.setImageResource(R.drawable.ic_chat_warning);
            holder.rightStateTv.setText("发送失败");
        } else if(info.state == ChatItem.STATE_ANALYSISING) {
            holder.rightStateIv.setImageResource(R.drawable.ic_chat_sync);
            holder.rightStateTv.setText("分析中");
        } else if(info.state == ChatItem.STATE_FINISH) {
            if(TextUtils.isEmpty(info.analysis)) {
                holder.rightStateIv.setImageResource(R.drawable.ic_chat_check);
                holder.rightStateTv.setText("完美表达");
            } else {
                holder.rightStateIv.setImageResource(R.drawable.ic_chat_bulb);
                holder.rightStateTv.setText("优化提升");
                holder.rightArrowIv.setImageResource(info.isAnalysisShow ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
                holder.rightArrowIv.setVisibility(View.VISIBLE);
                if(info.isAnalysisShow) {
                    if(!TextUtils.isEmpty(info.standarAnswer)) {
                        holder.rightStandAnswerTv.setText(info.standarAnswer);
                        holder.rightStandAnswerTv.setVisibility(View.VISIBLE);
                    } else {
                        holder.rightStandAnswerTv.setVisibility(View.GONE);
                    }
                    holder.rightTitleLayout.setBackgroundColor(context.getColor(R.color.common_green_color));
                    holder.rightAnalysisTv.setText(info.analysis);
                    holder.rightAnalysisLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.rightTitleLayout.setBackgroundResource(R.drawable.speak_chat_item_right_bottom_bg_shape);
                    holder.rightAnalysisLayout.setVisibility(View.GONE);
                }
            }
        }
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
        public ImageView rightStateIv;
        public TextView rightStateTv;
        public ImageView rightArrowIv;
        public View rightTitleLayout;
        public TextView rightTitleEnTv;
        public ImageView rightVoiceIv;
        public View rightAnalysisLayout;
        public TextView rightStandAnswerTv;
        public TextView rightAnalysisTv;
        public int position;
    }

    public void setCallback(ICallBack callback) {
        this.mCallback = callback;
    }

    interface ICallBack {
        void doPlay(ChatItem item);
    }
}
