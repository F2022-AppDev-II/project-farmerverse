package com.example.farmerverse.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.farmerverse.daos.SeedDao;
import com.example.farmerverse.database.FarmerverseRoomDatabase;
import com.example.farmerverse.entities.Seed;

import java.util.List;

public class SeedRepository {
    private SeedDao seedDao;
    private LiveData<List<Seed>> allSeeds;

    public SeedRepository(Application application)
    {
        FarmerverseRoomDatabase db = FarmerverseRoomDatabase.getDatabase(application);
        seedDao = db.seedDao();
        allSeeds = seedDao.getAllSeeds();
    }

    public LiveData<List<Seed>> getAllSeeds()
    {
        return allSeeds;
    }

    public Seed getSeed(int id)
    {
        return seedDao.getSeed(id);
    }

    public void updateSeed(Seed s)
    {
        seedDao.updateSeed(s);
    }

    public void insert(Seed seed)
    {
        FarmerverseRoomDatabase.databaseWriteExecutor.execute(() -> {
            seedDao.insert(seed);
        });
    }
}
