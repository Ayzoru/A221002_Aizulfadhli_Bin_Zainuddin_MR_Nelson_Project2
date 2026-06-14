package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {

    //get all saved data, sorted by the latest entry first
    //room automatically updates this Flow whenever data changes
    @Query("SELECT * FROM health_entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<HealthEntry>>

    //insert a new entry. if an ID matches, ignore/replace it safely
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: HealthEntry)
}