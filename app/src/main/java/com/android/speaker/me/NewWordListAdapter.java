package com.android.speaker.me;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.android.speaker.course.CoursePreviewActivity;
import com.android.speaker.course.CourseUtil;
import com.android.speaker.course.WordInfo;
import com.android.speaker.favorite.FavoriteItem;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 生词adapter
 */
public class NewWordListAdapter extends BaseListItemAdapter<WordInfo> {
    private boolean isEdit = false;
    private ICallBack mCallback;
    public NewWordListAdapter(Context context, List<WordInfo> list, ICallBack callback) {
        super(context, list);

        this.mCallback = callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.new_word_item, null);
            holder.nameTv = convertView.findViewById(R.id.item_word_name_tv);
            holder.pronunciationTv = convertView.findViewById(R.id.item_word_pronunciation_tv);
            holder.voiceIv = convertView.findViewById(R.id.item_word_voice_iv);
            holder.checkIv = convertView.findViewById(R.id.item_word_check_iv);
            holder.rectView = convertView.findViewById(R.id.item_rect_view);
            holder.explainLayout = convertView.findViewById(R.id.item_word_explain_ll);
            holder.featureTv = convertView.findViewById(R.id.item_word_feature_tv);
            holder.explainTv = convertView.findViewById(R.id.item_word_explain_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WordInfo info = items.get(position);
        holder.nameTv.setText(info.word);
        holder.pronunciationTv.setText(info.mark);
        if(isEdit) {
            holder.checkIv.setImageResource(info.isChecked ? R.drawable.ic_edit_checked : R.drawable.ic_edit_uncheck);
            holder.voiceIv.setVisibility(View.GONE);
            holder.checkIv.setVisibility(View.VISIBLE);
        } else {
            holder.voiceIv.setImageResource(info.isPlaying ? R.drawable.ic_question_voice_select : R.drawable.ic_question_voice_default);
            holder.voiceIv.setVisibility(View.VISIBLE);
            holder.checkIv.setVisibility(View.GONE);
        }
        if(info.isPlaying || isEdit) {
//            holder.featureTv.setText("");
//            holder.explainTv.setText("");
            holder.rectView.setVisibility(View.GONE);
            holder.explainLayout.setVisibility(View.VISIBLE);
        } else {
            holder.rectView.setVisibility(View.VISIBLE);
            holder.explainLayout.setVisibility(View.GONE);
        }

        holder.voiceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.isPlaying) {
                    return;
                }

                if(mCallback != null) {
                    mCallback.doPlay(info);
                }
            }
        });

        holder.checkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView nameTv;
        public TextView pronunciationTv;
        public ImageView voiceIv;
        public ImageView checkIv;
        public View rectView;
        public View explainLayout;
        public TextView featureTv;
        public TextView explainTv;
    }

    public List<WordInfo> getSelectItems() {
        List<WordInfo> list = new ArrayList<>();
        for(WordInfo info : items) {
            if(info.isChecked) {
                list.add(info);
            }
        }

        return list;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public interface ICallBack {
        void doPlay(WordInfo info);
    }
}
