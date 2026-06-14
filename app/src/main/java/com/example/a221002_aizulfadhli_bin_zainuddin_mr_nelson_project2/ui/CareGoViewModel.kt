package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data.CareGoDatabase
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data.CareGoUiState
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data.HealthEntry
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//changed from ViewModel to AndroidViewModel to safely pass the context
class CareGoViewModel(application: Application) : AndroidViewModel(application) { //the brain
    //the private "internal" state, only viewmodel can change
    private val _uiState = MutableStateFlow(CareGoUiState())
    //the public "read-only" state the UI watches
    val uiState: StateFlow<CareGoUiState> = _uiState.asStateFlow()
    //initialize your local Room Dao handler
    private val healthDao = CareGoDatabase.getDatabase(application).healthDao()

    //initialize the Firebase Firestore Instance
    private val firestore = FirebaseFirestore.getInstance()

    //trigger the web request immediately on initialization
    init {
        fetchDailyHealthTip()
        observeLocalDatabase() //start watching the Room database changes live
        observeCloudCommunity() //start listening to cloud updates instantly on boot
    }

    //1. listen to firebase firestore real-time updates
    private fun observeCloudCommunity() {
        _uiState.update { it.copy(isFirebaseLoading = true) }

        //target a database collection called "community_posts"
        firestore.collection("community_posts")
            .orderBy("timestamp", Query.Direction.DESCENDING) //newest messages at top
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    _uiState.update { it.copy(isFirebaseLoading = false) }
                    return@addSnapshotListener
                }

                //map incoming cloud documents safely back to our CommunityPost Kotlin object list
                val posts = snapshot.documents.mapNotNull { doc ->
                    val post = doc.toObject(CommunityPost::class.java)
                    post?.copy(id = doc.id) //attach document ID
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        communityPosts = posts,
                        isFirebaseLoading = false
                    )
                }
            }
    }

    //2. update typing text value from the input field
    fun updateNewPostText(text: String) {
        _uiState.update { it.copy(newPostText = text) }
    }

    //3. push a new message up to the firebase cloud
    fun sendCommunityPost() {
        val messageText = _uiState.value.newPostText.trim()
        if (messageText.isEmpty()) return //do not upload blank messages

        //can check if the user entered a name in their daily log, otherwise use Anonymous
        val senderName = _uiState.value.latestSavedEntry?.name.orEmpty()
            .ifEmpty { "CareGo User" }

        val newPost = CommunityPost(
            username = senderName,
            message = messageText,
            timestamp = System.currentTimeMillis()
        )

        //asynchronously push to the remote Firestore server collection
        firestore.collection("community_posts")
            .add(newPost)
            .addOnSuccessListener {
                //clear the text box input field when the post successfully uploads
                _uiState.update { it.copy(newPostText = "") }
            }
    }

    //automatically updates the history list whenever the local DB changes
    private fun observeLocalDatabase() {
        viewModelScope.launch {
            healthDao.getAllEntries().collect { savedEntries ->
                _uiState.update { currentState ->
                    currentState.copy(healthHistory = savedEntries)
                }
            }
        }
    }

    //call retrofit asynchronously in a background coroutine
    fun fetchDailyHealthTip() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isApiLoading = true) }

                //make the network call out to the internet
                val response = RetrofitClient.apiService.getRandomHealthTip()

                _uiState.update { currentState ->
                    currentState.copy(
                        apiHealthTip = response.slip.advice, //extract string content
                        isApiLoading = false
                    )
                }
            } catch (e: Exception) {
                //safe fallback string if device is completely offline or server is down
                _uiState.update { currentState ->
                    currentState.copy(
                        apiHealthTip = "Stay hydrated and get plenty of rest today! (Offline Mode)",
                        isApiLoading = false
                    )
                }
            }
        }
    }

    //function to update the name
    fun updateName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(userName = newName)
        }
    }

    //function to update the mood
    fun updateMood(newMood: String) {
        _uiState.update { it.copy(selectedMood = newMood) }
    }

    fun updateSymptoms(newSymptoms: String) {
        _uiState.update { it.copy(symptomNotes = newSymptoms) }
    }

    //function to ADD an item to the list : project requirement
    fun saveEntry() {
        //date formatter to show when it was logged
        val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        val newEntry = HealthEntry(
            name = _uiState.value.userName,
            mood = _uiState.value.selectedMood,
            symptoms = _uiState.value.symptomNotes, //save notes
            date = currentDate //Dynamic Date
        )
        viewModelScope.launch {
            //write to the phone's internal storage
            healthDao.insertEntry(newEntry)

            _uiState.update { currentState ->
                currentState.copy(
                    //cache this exact entry snapshot before resetting the text fields below
                    latestSavedEntry = newEntry,
                    //Reset inputs for the next entry
                    userName = "",
                    selectedMood = "Select a mood",
                    symptomNotes = "",
                    isExpanded = false
                )
            }
        }
    }

    //to handle the expandable card animation
    fun toggleExpansion() {
        _uiState.update { currentState ->
            currentState.copy(isExpanded = !currentState.isExpanded)
        }
    }

    //GPS part
    //fetch real-time gps coordinates from the hardware antenna
    @SuppressLint("MissingPermission") // We check permissions dynamically in the code below
    fun fetchCurrentGpsLocation(context: Context) {
        //1.double check that the user actually granted permission before waking up the hardware
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            _uiState.update { it.copy(gpsCoordinates = "Permission Denied! Allow location access in app settings.") }
            return
        }

        //2.set loading state while the hardware communicates with the satellites
        _uiState.update { it.copy(isGpsLoading = true) }

        //3.request the absolute latest location coordinates from the device client
        val locationClient = LocationServices.getFusedLocationProviderClient(context)

        locationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    //pull the raw mathematical Latitude and Longitude variables out of the hardware
                    val lat = location.latitude
                    val lng = location.longitude
                    _uiState.update { currentState ->
                        currentState.copy(
                            gpsCoordinates = "Lat: ${String.format("%.4f", lat)} | Lng: ${String.format("%.4f", lng)}",
                            isGpsLoading = false
                        )
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            gpsCoordinates = "GPS Sensor Idle. Try opening Google Maps first to activate antenna.",
                            isGpsLoading = false
                        )
                    }
                }
            }
            .addOnFailureListener { exception ->
                _uiState.update { currentState ->
                    currentState.copy(
                        gpsCoordinates = "Hardware Error: ${exception.localizedMessage}",
                        isGpsLoading = false
                    )
                }
            }
    }
}