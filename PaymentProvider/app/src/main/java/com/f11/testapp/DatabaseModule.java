package com.f11.testapp;

import android.content.Context;

import com.f11.testapp.data.AdProviderDao;
import com.f11.testapp.data.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context);
    }

    @Provides
    @Singleton
    public AdProviderDao provideAdProviderDao(AppDatabase database) {
        return database.adProviderDao();  // Ensure this method name is correct as per your AppDatabase class
    }
}

