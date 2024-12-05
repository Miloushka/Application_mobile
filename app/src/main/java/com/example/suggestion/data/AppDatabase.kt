package com.example.suggestion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Depense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // DAO pour accéder aux méthodes d'accès aux données
    abstract fun depenseDao(): DepenseDao

    companion object {
        // Instance singleton de la base de données
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
