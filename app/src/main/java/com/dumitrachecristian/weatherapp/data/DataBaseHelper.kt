package com.dumitrachecristian.weatherapp.data

import android.content.Context
import androidx.room.Room

object DatabaseHelper {
    private lateinit var database: AppDatabase

    private const val DB_NAME = "WEATHER_DB"

    fun initDB(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DB_NAME
        )
            .build()
    }
}