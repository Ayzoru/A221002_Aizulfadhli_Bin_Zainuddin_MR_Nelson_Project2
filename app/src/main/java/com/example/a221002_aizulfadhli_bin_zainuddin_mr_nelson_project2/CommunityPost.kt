package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2


data class CommunityPost(
    val id: String = "",         //document ID from Firestore
    val username: String = "",
    val message: String = "",
    val timestamp: Long = 0L     //used to sort posts chronologically
)