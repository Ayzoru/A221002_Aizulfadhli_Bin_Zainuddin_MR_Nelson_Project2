package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme.AppTheme

@Composable
fun SummaryScreen(
    viewModel: CareGoViewModel,
    onRestartButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    //read from new explicit snapshot holder instead of filtering the history list
    val lastEntry = uiState.latestSavedEntry
    //get the most recent entry from history list
    //if the list is empty, we use a placeholder


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Health Report Summary",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //use lastEntry data instead of uiState.userName
                Text(
                    text = "Hello, ${lastEntry?.name ?: "User"}!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Today you're feeling:",
                    style = MaterialTheme.typography.bodyMedium
                )

                //use lastEntry mood
                Text(
                    text = lastEntry?.mood ?: "No Status",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your logs have been saved securely. Keep up the consistent tracking!",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedButton(
            onClick = onRestartButtonClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("FINISH & GO HOME", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SummaryScreenPreview() {
    AppTheme {
        val mockViewModel: CareGoViewModel = viewModel()
        SummaryScreen(
            viewModel = mockViewModel,
            onRestartButtonClicked = {}
        )
    }
}