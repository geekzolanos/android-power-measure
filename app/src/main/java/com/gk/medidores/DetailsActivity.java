package com.gk.medidores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gk.medidores.db.AppDatabase;
import com.gk.medidores.models.Measure;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int MEASURE_DELETED = 2;

    private Measure measure;
    private LatLng locationCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        measure = getIntent().getExtras().getParcelable("measure");
        int colorScrim = getIntent().getExtras().getInt("colorScrim");
        setToolbar(colorScrim);
        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_delete) {
            onDeleteMeasure();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    private void onDeleteMeasure() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.detail_delete_title)
                .setMessage(getString(R.string.detail_delete_msg, measure.getDeviceId().toUpperCase()))
                .setPositiveButton(R.string.default_btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMeasure();
                    }
                })
                .setNeutralButton(R.string.default_btn_no, null)
                .create();

        dialog.show();
    }

    private void deleteMeasure() {
        new DeleteTask(AppDatabase.getDb(this), new OnSuccessListener() {
            @Override
            void onSuccess() {
                setResult(MEASURE_DELETED);
                finish();
            }
        }).execute(measure);
    }

    private void setToolbar(int colorScrim) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout ct = findViewById(R.id.main_collapsing_toolbar);
        ct.setExpandedTitleColor(ActivityCompat.getColor(this, android.R.color.white));
        ct.setCollapsedTitleTextColor(ActivityCompat.getColor(this, android.R.color.white));
        ct.setTitle(measure.getDeviceId());

        if(colorScrim != 0) {
            ct.setBackgroundColor(ActivityCompat.getColor(this, colorScrim));
            ct.setContentScrimColor(ActivityCompat.getColor(this, colorScrim));
            ct.setStatusBarScrimColor(ActivityCompat.getColor(this, colorScrim));
        }
    }

    private void setView() {
        TextView addressView = findViewById(R.id.data_address);
        TextView powerView = findViewById(R.id.data_power);
        TextView createdView = findViewById(R.id.data_createdAt);
        ImageView imageView = findViewById(R.id.data_image);
        View frameMap = findViewById(R.id.frame_map);

        addressView.setText(measure.getAddress());
        powerView.setText(String.valueOf(measure.getPower()));

        Date date = new Date();
        date.setTime(measure.getCreatedAt());
        String c_date = new SimpleDateFormat("dd/MM/yy hh:mm:ss a").format(date);
        createdView.setText(c_date);

        if(measure.getImagePath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(measure.getImagePath());
            imageView.setImageBitmap(bitmap);
        }

        if(measure.getCoordLat() > 0.0f) {
            locationCoords = new LatLng((double) measure.getCoordLat(), (double) measure.getCoordLong());
            frameMap.setVisibility(View.VISIBLE);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.data_map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(locationCoords));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoords, 15));
    }
}

class DeleteTask extends AsyncTask<Measure, Void, Void> {
    private AppDatabase db;
    private OnSuccessListener listener;

    DeleteTask(AppDatabase db, OnSuccessListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Measure... measures) {
        db.measures().delete(measures[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.listener.onSuccess();
    }
}
