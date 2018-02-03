package com.local.medidores.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.local.medidores.models.Measure;

import java.util.List;

/**
 * Created by djra23 on 22/01/18.
 */

@Dao
public interface MeasureDao {
    @Query("SELECT * FROM measures")
    LiveData<List<Measure>> getAll();

    @Update
    void update(Measure measure);

    @Insert
    void insert(Measure measure);

    @Delete
    void delete(Measure measure);
}
