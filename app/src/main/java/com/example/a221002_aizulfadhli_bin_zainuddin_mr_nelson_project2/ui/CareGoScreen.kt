package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2

//enum prevents typos from main
enum class CareGoScreen(val title: String) { //enumeration fixed list of constants, val = property
    Home(title = "Dashboard"), //constants, unique id for pages , pass to navcontroller
    Start(title = "CareGo Home"),
    Entry(title = "Log Entry"),
    Summary(title = "Health Report"),
    History(title = "Health History"),

    //new screens
    Community(title = "Community Feed"),     //for the Firebase Firestore Cloud Feature
    Tracker(title = "Emergency Clinic")      //for gps sensor feature
}