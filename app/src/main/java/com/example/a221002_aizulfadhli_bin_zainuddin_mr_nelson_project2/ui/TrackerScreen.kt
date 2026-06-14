package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.CareGoViewModel

@Composable
fun TrackerScreen(
    viewModel: CareGoViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current //grab the context required for hardware checks

    //automated OS popup launcher to securely ask the user for GPS permission tokens
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            //if the user accepts the system prompt, fire the sensor client immediately
            viewModel.fetchCurrentGpsLocation(context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //title header
        Text(
            text = "Clinic Tracker",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Hardware GPS Sensor Integration",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //section 1: hardware coordinates interface card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Spatial Position:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(12.dp))

                //render either a spinning progress bar or the calculated coordinate strings
                if (uiState.isGpsLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    Text(
                        text = uiState.gpsCoordinates,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This screen interfaces directly with the device's FusedLocationProviderClient to stream hardware metadata securely.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )

                //google maps feature
                if (uiState.gpsCoordinates.contains("Lat:")) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            // 1. Extract the raw lat and lng values from your state string
                            // Example string format: "Lat: 2.9211 | Lng: 101.7724"
                            val cleanCoordinates = uiState.gpsCoordinates
                                .replace("Lat: ", "")
                                .replace("Lng: ", "")
                                .replace(" | ", ",") // Turns into "2.9211,101.7724"

                            // 2. 🆕 Create a search query URI that centers on your coordinates and automatically searches for "Klinik"
                            val mapUri = Uri.parse("geo:$cleanCoordinates?q=Klinik")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
                                setPackage("com.google.android.apps.maps") //forces it to open the real Google Maps app
                            }
                            context.startActivity(mapIntent)
                        }
                    ) {
                        Text("Open in Google Maps 🗺️")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        //section 2: trigger sensor action button
        Button(
            onClick = {
                //request the OS system permissions prompt container
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("TRIGGER GPS SENSOR", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        //navigation return
        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Back to Dashboard", fontWeight = FontWeight.SemiBold)
        }
    }
}