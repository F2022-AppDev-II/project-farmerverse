package com.example.farmerverse.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farmerverse.entities.Seed;

import java.util.List;

@Dao
public interface SeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Seed seed);

    @Query("DELETE FROM seed_table")
    void deleteAll();

    @Query("SELECT * FROM seed_table")
    LiveData<List<Seed>> getAllSeeds();

    @Query("SELECT * FROM seed_table WHERE id = :id")
    Seed getSeed(int id);

    @Query("DELETE FROM seed_table WHERE id = :id")
    void deleteSeed(int id);

    @Update
    void updateSeed(Seed seed);

    @Query("UPDATE seed_table SET quantity = quantity-:amount WHERE id=:id")
    void removeSeeds(double amount, int id);

    @Query("UPDATE seed_table SET quantity = quantity+:amount WHERE id=:id")
    void addSeeds(double amount, int id);

    @Query("SELECT * FROM seed_table")
    List<Seed> getAllSeedsList();
}
