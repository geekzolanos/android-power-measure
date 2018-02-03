package com.local.medidores.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.local.medidores.R;
import com.local.medidores.models.Measure;

/**
 * Created by djra23 on 23/01/18.
 */

public class MeasureViewHolder extends RecyclerView.ViewHolder {
    private static final String[] COLORS = new String[] {"indigo", "teal", "purple", "orange"};

    private ImageView v_picture;
    private TextView v_id;
    private TextView v_power;

    private int colorId;

    public MeasureViewHolder(View itemView) {
        super(itemView);

        this.v_picture = itemView.findViewById(R.id.item_image);
        this.v_id = itemView.findViewById(R.id.item_id);
        this.v_power = itemView.findViewById(R.id.item_power);
    }

    public void setMeasure(Measure measure) {
        this.v_id.setText(measure.getDeviceId());
        this.v_power.setText(Float.toString(measure.getPower()) + " Watts");

        if(this.v_picture == null) {
            this.setBackgroundColor();
        } else {
            this.setBackgroundImage(measure.getImagePath());
        }
    }

    private void setBackgroundColor() {
        Context ctx = this.itemView.getContext();
        int idx = (int) Math.floor(Math.random() * (COLORS.length));
        colorId = this.itemView.getResources().getIdentifier(COLORS[idx], "color", ctx.getPackageName());
        this.itemView.setBackgroundColor(ctx.getColor(colorId));
    }

    private void setBackgroundImage(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(path, bmOptions);
        v_picture.setImageBitmap(bmp);
    }

    public int getColorId() {
        return colorId;
    }
}
