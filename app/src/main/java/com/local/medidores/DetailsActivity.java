package com.local.medidores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.local.medidores.models.Measure;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
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

    private void setToolbar(int colorScrim) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout ct = findViewById(R.id.main_collapsing_toolbar);
        ct.setExpandedTitleColor(getColor(android.R.color.white));
        ct.setCollapsedTitleTextColor(getColor(android.R.color.white));
        ct.setTitle(measure.getDeviceId());

        if(colorScrim != 0) {
            ct.setBackgroundColor(getColor(colorScrim));
            ct.setContentScrimColor(getColor(colorScrim));
            ct.setStatusBarScrimColor(getColor(colorScrim));
        }
    }

    private void setView() {
        TextView addressView = findViewById(R.id.data_address);
        TextView powerView = findViewById(R.id.data_power);
        TextView createdView = findViewById(R.id.data_createdAt);
        ImageView imageView = findViewById(R.id.data_image);

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

        if(measure.getCoordLat() != null) {
            locationCoords = new LatLng((double) measure.getCoordLat(), (double) measure.getCoordLong());
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
