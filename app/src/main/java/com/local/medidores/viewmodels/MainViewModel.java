package com.local.medidores.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.local.medidores.db.AppDatabase;
import com.local.medidores.models.Measure;

import java.util.List;

/**
 * Created by djra23 on 22/01/18.
 */

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Measure>> measures;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getDb(application);
        measures = appDatabase.measures().getAll();
    }


    public LiveData<List<Measure>> getMeasures() {
        return measures;
    }
}
