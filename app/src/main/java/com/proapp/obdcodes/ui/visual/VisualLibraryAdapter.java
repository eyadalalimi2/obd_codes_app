package com.proapp.obdcodes.ui.visual;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.proapp.obdcodes.R;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_visual_component, viewGroup, false);
        ImageView img = view.findViewById(R.id.imgPart);
        TextView txt = view.findViewById(R.id.txtPart);

        img.setImageResource(items.get(i).imageRes);
        txt.setText(items.get(i).name);

        return view;
    }
}
