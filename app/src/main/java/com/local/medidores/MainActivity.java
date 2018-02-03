package com.local.medidores;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.local.medidores.adapters.MeasureAdapter;
import com.local.medidores.models.Measure;
import com.local.medidores.viewholders.MeasureViewHolder;
import com.local.medidores.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MeasureAdapter rv_adapter;
    private View emptyView;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setGridList();
        setFabClick();
        setLiveData();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setFabClick(){
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
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

        startActivity(intent, (options != null) ? options.toBundle() : null);
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
