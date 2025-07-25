package com.eyadalalimi.car.obd2.ui.visual;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eyadalalimi.car.obd2.R;

import java.util.List;

public class VisualLibraryAdapter extends BaseAdapter {

    private final Activity context;
    private final List<VisualItem> items;

    public VisualLibraryAdapter(Activity context, List<VisualItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        ImageView imgPart;
        TextView txtPart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_visual_component, parent, false);
            holder = new ViewHolder();
            holder.imgPart = convertView.findViewById(R.id.imgPart);
            holder.txtPart = convertView.findViewById(R.id.txtPart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VisualItem item = items.get(position);
        holder.txtPart.setText(item.name);

        // ✅ تحميل الصور من الإنترنت أو الموارد
        if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.splash) // صورة مؤقتة أثناء التحميل
                    .error(R.drawable.ic_disclaimer)             // في حال فشل التحميل
                    .into(holder.imgPart);
        } else {
            holder.imgPart.setImageResource(item.imageRes); // في حالة موارد محلية
        }

        return convertView;
    }
}
