package com.gk.medidores.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gk.medidores.db.AppDatabase;
import com.gk.medidores.models.Measure;

import java.util.List;

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
