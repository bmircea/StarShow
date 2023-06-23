package com.example.starshow

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Observation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun observationDao() : ObservationDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: AppDatabase) {
            val obsDao = db.observationDao()
            obsDao.insert(Observation(1, "Example 1", "This is an example!"))
            //noteDao.insert(Observation("title 2", "desc 2", 2))
            //noteDao.insert(Observation("title 3", "desc 3", 3))

        }
    }
}