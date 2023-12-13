package com.android.speaker.me;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.speaker.base.component.BaseListItemAdapter;
import com.chinsion.SpeakEnglish.R;

import java.util.List;

/***
 * 笔记adapter
 */
public class NoteListAdapter extends BaseListItemAdapter<NoteInfo> {
    public NoteListAdapter(Context context, List<NoteInfo> list) {
        super(context, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.note_item, null);
            holder.nameTv = convertView.findViewById(R.id.item_note_name_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NoteInfo info = items.get(position);
        holder.nameTv.setText(info.title);

        return convertView;
    }

    class ViewHolder {
        public TextView nameTv;
    }

}
