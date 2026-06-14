package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.data.HealthEntry

@Composable
fun HistoryScreen(
    historyList: List<HealthEntry>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .safeDrawingPadding()
        .padding(16.dp)
    ) {

        Text(
            text = "Health History",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (historyList.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("No records yet. Go log your health!")
            }
        } else {
            //only loads the item that are currently visible on screen, better for performance
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(historyList) { entry -> //make new card after every single new record
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = entry.name, style = MaterialTheme.typography.titleLarge)
                                Text(text = entry.date, style = MaterialTheme.typography.labelSmall)
                            }
                            Text(text = "Feeling: ${entry.mood}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)

                            //only show the symptoms section if user actually typed something
                            if (entry.symptoms.isNotBlank()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(text = "Symptom Notes:", style = MaterialTheme.typography.titleSmall)
                                Text(text = entry.symptoms, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Back to Menu")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    //fake list to see how the cards look
    val mockHistory = listOf(
        HealthEntry(1, "Ali", "Rad", "Feeling energetic after exercise", "May 09, 2026"),
        HealthEntry(2, "Siti", "Meh", "Slight headache from studying", "May 08, 2026"),
        HealthEntry(3, "Abu", "Good", "", "May 07, 2026") // Test entry with no symptoms
    )

    AppTheme {
        HistoryScreen(
            historyList = mockHistory,
            onBackClick = {}
        )
    }
}