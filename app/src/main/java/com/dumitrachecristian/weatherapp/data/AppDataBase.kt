package com.dumitrachecristian.weatherapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherEntityDao(): WeatherEntityDao

    companion object {
        //Migrations
    }
}