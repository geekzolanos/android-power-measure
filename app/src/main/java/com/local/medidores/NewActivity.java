package com.local.medidores;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.local.medidores.db.AppDatabase;
import com.local.medidores.helpers.LocationHelper;
import com.local.medidores.models.Measure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText idView;
    private EditText powerView;
    private EditText addressView;
    private AppCompatCheckBox locationView;
    private ProgressBar locProgressView;
    private TextView locStatusView;
    private ImageView imageView;
    private ImageView imagePhView;

    private float locationLat;
    private float locationLong;

    private boolean m_imageLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        idView = findViewById(R.id.data_id);
        powerView = findViewById(R.id.data_power);
        addressView = findViewById(R.id.data_address);
        locationView = findViewById(R.id.data_location);
        imageView = findViewById(R.id.data_image);
        imagePhView = findViewById(R.id.data_image_ph);
        locProgressView = findViewById(R.id.location_progress);
        locStatusView = findViewById(R.id.location_status);

        FloatingActionButton fabView = findViewById(R.id.btn_pick_image);
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dispatchTakePictureIntent(); }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1000);
        } else {
            LocationHelper.getLocation(this, locListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LocationHelper.getLocation(this, locListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_save:
                saveMeasure();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(imageBitmap);
            imagePhView.setVisibility(View.GONE);
            m_imageLoaded = true;
        }
    }

    private void saveMeasure() {
        try {
            validateInputData();

            Measure measure = new Measure();
            measure.setDeviceId(idView.getText().toString());
            measure.setAddress(addressView.getText().toString());
            measure.setPower(Float.valueOf(powerView.getText().toString()));
            measure.setCreatedAt(Calendar.getInstance().getTimeInMillis());

            if(m_imageLoaded) {
                String imagePath = saveImageFile();
                measure.setImagePath(imagePath);
            }

            if(locationView.isChecked()) {
                measure.setCoordLat(locationLat);
                measure.setCoordLong(locationLong);
            }

            new InsertTask(AppDatabase.getDb(this), new OnSuccessListener() {
                @Override
                void onSuccess() {
                    Snackbar.make(idView, "Medicion almacenada satisfactoriamente.", Snackbar.LENGTH_LONG).show();
                    onBackPressed();
                }
            }).execute(measure);
        }
        catch(Exception ignored) {
            Snackbar.make(idView, "Por favor, verifique los datos e intente nuevamente.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void validateInputData() throws Exception {
        String idVal = idView.getText().toString();
        String addressVal = addressView.getText().toString();
        String powerVal = powerView.getText().toString();
        if(idVal.isEmpty() || addressVal.isEmpty() || powerVal.isEmpty()) {
            throw new Exception("INVALID_DATA");
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private String saveImageFile() throws IOException {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String absPath = image.getAbsolutePath();

        FileOutputStream out = new FileOutputStream(absPath);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

        return absPath;
    }

    private LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationLat = (float) location.getLatitude();
                locationLong = (float) location.getLongitude();
                locStatusView.setText(getString(R.string.location_status, locationLat, locationLong));
                locStatusView.setVisibility(View.VISIBLE);
                locationView.setEnabled(true);
                locProgressView.setVisibility(View.GONE);
            } else {
                locProgressView.setVisibility(View.GONE);
            }
        }
        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    };
}

class InsertTask extends AsyncTask<Measure, Void, Void> {
    private AppDatabase db;
    private OnSuccessListener listener;

    InsertTask(AppDatabase db, OnSuccessListener listener) {
        this.db = db;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Measure... measures) {
        db.measures().insert(measures[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.listener.onSuccess();
    }
}

abstract class OnSuccessListener {
    abstract void onSuccess();
}