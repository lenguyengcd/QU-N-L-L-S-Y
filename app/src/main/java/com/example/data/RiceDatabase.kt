package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RiceCellEntity::class], version = 2, exportSchema = false)
@TypeConverters(RiceConverters::class)
abstract class RiceDatabase : RoomDatabase() {
    abstract fun riceCellDao(): RiceCellDao

    companion object {
        @Volatile
        private var INSTANCE: RiceDatabase? = null

        fun getDatabase(context: Context): RiceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RiceDatabase::class.java,
                    "rice_cells_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
