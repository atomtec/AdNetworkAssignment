package com.f11.testapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AdProviderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AdProvider provider);

    @Update
    void update(AdProvider provider);

    @Delete
    void delete(AdProvider provider);

    @Query("SELECT * FROM ad_providers ORDER BY name ASC")
    List<AdProvider> getAllProviders();
}