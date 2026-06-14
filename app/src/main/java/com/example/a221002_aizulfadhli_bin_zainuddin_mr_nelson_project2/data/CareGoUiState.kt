package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.CommunityPost


@Entity(tableName = "health_entries")
data class HealthEntry( //single saved record , one single log
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val symptoms: String,
    val mood: String,
    val date: String
)

data class CareGoUiState( //manage overall live state
    val userName: String = "",
    val selectedMood: String = "Select a mood",
    val symptomNotes: String = "", //field for input
    //list to store multiple items
    val healthHistory: List<HealthEntry> = emptyList(),
    val isExpanded: Boolean = false,

    //for dynamic web api web data
    val apiHealthTip: String = "Loading your dynamic tip...",
    val isApiLoading: Boolean = true,
    val latestSavedEntry: HealthEntry? = null,

    //3 fields for firebase community feature
    val communityPosts: List<CommunityPost> = emptyList(),
    val newPostText: String = "",
    val isFirebaseLoading: Boolean = false,

    //2 fields for gps sensor APIs
    val gpsCoordinates: String = "Location Not Detected Yet",
    val isGpsLoading: Boolean = false
)