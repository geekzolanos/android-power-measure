package com.local.medidores.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.local.medidores.dao.MeasureDao;
import com.local.medidores.models.Measure;

/**
 * Created by djra23 on 22/01/18.
 */

@Database(entities = {Measure.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;

    public static AppDatabase getDb(Context ctx) {
        if(db == null) {
            db = Room.databaseBuilder(ctx.getApplicationContext(),
                    AppDatabase.class, "measure_db").build();
        }

        return db;
    }

    public abstract MeasureDao measures();
}
