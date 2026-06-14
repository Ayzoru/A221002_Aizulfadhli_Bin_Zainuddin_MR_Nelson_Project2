package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    apiHealthTip: String,
    //decoupling
    onStartLogClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    //new buttons for 2 new screens
    onCommunityClick: () -> Unit,
    onTrackerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- APP TITLE HEADER ---
        Text(
            text = "CareGo",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SDG 3: Good Health & Well-being",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        //CORE FEATURES BUTTON GROUP
        //use an inner layout block with unified spacing for a clean menu arrangement
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //1.start Daily Log Button
            Button(
                onClick = onStartLogClick,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨  Start Daily Log", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                }
            }

            //2.view History Button
            OutlinedButton(
                onClick = onViewHistoryClick,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📊  View Health History", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //subtle Section Divider label for academic panel clarity
            Text(
                text = "Connect & Track ✨",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            //3.community Board Button (Firebase Cloud Integration)
            Button(
                onClick = onCommunityClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🤝  Community Board (Cloud)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                }
            }

            //4.Emergency Clinic Tracker Button (GPS Sensor Integration)
            OutlinedButton(
                onClick = onTrackerClick,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍  Emergency Clinic Tracker (GPS)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) //intelligently pushes the tips box to the bottom safely

        //WEB REST API SYSTEM TIP CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "💡 Live Health Tip of the Day",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = apiHealthTip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            apiHealthTip = "An apple a day keeps the doctor away!",
            onStartLogClick = {},
            onViewHistoryClick = {},
            onCommunityClick = {},
            onTrackerClick = {}
        )
    }
}