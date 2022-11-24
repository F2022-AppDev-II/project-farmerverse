package com.example.farmerverse.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.farmerverse.daos.SeedDao;
import com.example.farmerverse.entities.Seed;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Seed.class}, version = 1, exportSchema = false)
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
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                SeedDao seedDao = INSTANCE.seedDao();

                seedDao.deleteAll();

                //Space between in CM, quantity in KG, Weight Per Seed in grams, growth time is average of the range
                Seed watermelon = new Seed("Watermelon", 15, 10.2, 75, 0.25);
                Seed spaghettiSquash = new Seed("Spaghetti Squash", 92, 1.25, 100, 0.25);
                Seed greenBeans = new Seed("Green Beans", 5.1, 5.4, 55, 0.25);
                Seed romaineLettuce = new Seed("Romaine Lettuce", 15.24, 22.2, 70, 0.25);
                Seed corn = new Seed("Corn", 7.5, 17.25, 70, 0.25);

                seedDao.insert(watermelon);
                seedDao.insert(spaghettiSquash);
                seedDao.insert(greenBeans);
                seedDao.insert(romaineLettuce);
                seedDao.insert(corn);
            });
        }
    };
}
