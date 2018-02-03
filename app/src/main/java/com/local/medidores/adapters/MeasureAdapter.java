package com.local.medidores.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.local.medidores.R;
import com.local.medidores.models.Measure;
import com.local.medidores.viewholders.MeasureViewHolder;

import java.util.ArrayList;

/**
 * Created by djra23 on 22/01/18.
 */

public class MeasureAdapter extends RecyclerView.Adapter<MeasureViewHolder> {
    private static final int TYPE_EMPTY = 1;

    private ArrayList<Measure> data;
    private View.OnClickListener clickListener;

    public MeasureAdapter(ArrayList<Measure> measures) {
        this.data = measures;
    }

    @Override
    public MeasureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = (viewType == TYPE_EMPTY) ? R.layout.grid_item_empty : R.layout.grid_item_image;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        v.setOnClickListener(this.clickListener);
        return new MeasureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeasureViewHolder holder, int position) {
        holder.setMeasure(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Measure item = this.data.get(position);
        return (item.getImagePath() == null || item.getImagePath().isEmpty()) ? TYPE_EMPTY : 0;
    }

    public void updateDataset(ArrayList<Measure> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Measure getMeasure(int id) {
        return this.data.get(id);
    }

    public void setOnClickEventListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }
}

