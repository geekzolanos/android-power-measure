package com.gk.medidores;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gk.medidores.adapters.MeasureAdapter;
import com.gk.medidores.models.Measure;
import com.gk.medidores.viewholders.MeasureViewHolder;
import com.gk.medidores.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MeasureAdapter rv_adapter;
    private View emptyView;
    private RecyclerView recyclerView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case NewActivity.MEASURE_CREATED:
                Snackbar.make(findViewById(android.R.id.content), R.string.create_success, Snackbar.LENGTH_LONG).show();
                break;

            case DetailsActivity.MEASURE_DELETED:
                Snackbar.make(findViewById(android.R.id.content), R.string.details_delete_success, Snackbar.LENGTH_LONG).show();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setGridList();
        setFabClick();
        setLiveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_about) {
            try {
                onAbout();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAbout() throws PackageManager.NameNotFoundException {
        int d = this.getResources().getDimensionPixelSize(R.dimen.about_view_margin),
            d2 = d / 2;

        FrameLayout view = new FrameLayout(this);
        TextView text = new TextView(this);

        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(d, d2, d, d2);
        view.addView(text, lp);

        String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        String str = getString(R.string.main_about_msg, version);
        SpannableString s = new SpannableString(str);
        Linkify.addLinks(s, Linkify.WEB_URLS);
        text.setText(s);
        text.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.main_about_title)
                .setView(view)
                .setPositiveButton(R.string.default_btn_ok, null)
                .create();

        dialog.show();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setFabClick(){
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, NewActivity.class), 0);
            }
        });
    }

    private void setLiveData() {
        MainViewModel vm = ViewModelProviders.of(this).get(MainViewModel.class);
        vm.getMeasures().observe(this, new Observer<List<Measure>>() {
            @Override
            public void onChanged(@Nullable List<Measure> measures) {
                setEmptyView(measures == null || measures.isEmpty());
                rv_adapter.updateDataset((ArrayList<Measure>) measures);
            }
        });
    }

    private void setGridList() {
        rv_adapter = new MeasureAdapter(new ArrayList<Measure>());
        rv_adapter.setOnClickEventListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsView(v);
            }
        });
        emptyView = findViewById(R.id.main_empty);
        recyclerView = findViewById(R.id.main_grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(rv_adapter);
    }

    private void openDetailsView(View v) {
        ActivityOptions options = null;

        int pos = recyclerView.getChildAdapterPosition(v);
        Measure measure = rv_adapter.getMeasure(pos);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("measure", measure);

        ImageView image = v.findViewById(R.id.item_image);
        if(image != null) {
            image.setTransitionName("measureImage");
            options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, image, "measureImage");
        } else {
            MeasureViewHolder vh = (MeasureViewHolder) recyclerView.getChildViewHolder(v);
            intent.putExtra("colorScrim", vh.getColorId());
        }

        ActivityCompat.startActivityForResult(this, intent, 0, (options != null) ? options.toBundle() : null);
    }

    private void setEmptyView(boolean val) {
        if (val) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
