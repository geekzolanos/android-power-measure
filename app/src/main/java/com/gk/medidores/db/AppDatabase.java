package com.gk.medidores.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.gk.medidores.dao.MeasureDao;
import com.gk.medidores.models.Measure;

@Database(entities = {Measure.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "measure_db";

    private static AppDatabase db;

    public static AppDatabase getDb(Context ctx) {
        if(db == null) {
            db = Room.databaseBuilder(ctx.getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();
        }

        return db;
    }

    public abstract MeasureDao measures();
}
