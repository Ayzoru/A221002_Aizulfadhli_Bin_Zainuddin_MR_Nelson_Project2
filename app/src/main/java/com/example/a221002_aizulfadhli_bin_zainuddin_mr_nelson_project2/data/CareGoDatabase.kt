package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HealthEntry::class], version = 1, exportSchema = false)
abstract class CareGoDatabase : RoomDatabase() {

    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile
        private var Instance: CareGoDatabase? = null

        //a thread-safe Singleton to make sure only ONE database instance exists at a time
        fun getDatabase(context: Context): CareGoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CareGoDatabase::class.java,
                    "carego_database" //the physical file name on the phone
                )
                    .fallbackToDestructiveMigration() //safely handles structure updates during dev
                    .build()
                    .also { Instance = it }
            }
        }
    }
}