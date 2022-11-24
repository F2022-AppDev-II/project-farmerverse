package com.example.farmerverse.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.farmerverse.daos.SeedDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class FarmerverseRoomDatabase extends RoomDatabase {
    public abstract SeedDao seedDao();

    private static volatile FarmerverseRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FarmerverseRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null) {
            synchronized (FarmerverseRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FarmerverseRoomDatabase.class, "farmerverse_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
