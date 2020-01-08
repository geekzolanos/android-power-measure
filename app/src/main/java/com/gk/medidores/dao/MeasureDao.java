package com.gk.medidores.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gk.medidores.models.Measure;

import java.util.List;

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
