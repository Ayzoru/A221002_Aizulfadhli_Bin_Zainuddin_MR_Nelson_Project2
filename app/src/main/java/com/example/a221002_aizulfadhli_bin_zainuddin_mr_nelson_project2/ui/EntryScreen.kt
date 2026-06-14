package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme.AppTheme

@Composable
fun EntryScreen(
    viewModel: CareGoViewModel,
    onNextButtonClicked: () -> Unit
) {
    //collecting state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        //Header (Reusing Lab 3 Logo)
        Image(
            painter = painterResource(id = R.drawable.carego),
            contentDescription = "Logo",
            modifier = Modifier.height(130.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Daily Log",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        //Card for input - CONNECTED TO VIEWMODEL
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.userName, // show what is currently in the viewmodel
                    onValueChange = { viewModel.updateName(it) },  // update to follow new changes
                    label = { Text("Enter Name") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Mood Section
        Text("How are you feeling?", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //Updated to be clickable and save to ViewModel
            MoodItem("😃", "Rad") { viewModel.updateMood("Rad") }
            MoodItem("🙂", "Good") { viewModel.updateMood("Good") }
            MoodItem("😐", "Meh") { viewModel.updateMood("Meh") }
            MoodItem("😟", "Bad") { viewModel.updateMood("Bad") }
        }

        //Animated Expandable Card - CONNECTED TO VIEWMODEL
        ExpandableSymptomCard(
            isExpanded = uiState.isExpanded,
            symptomNotes = uiState.symptomNotes,
            onToggle = { viewModel.toggleExpansion() },
            onSymptomsChanged = { viewModel.updateSymptoms(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Button to go to Screen 3
        Button(
            onClick = onNextButtonClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            enabled = uiState.userName.isNotBlank() // Only allow next if name is typed!
        ) {
            Text("GENERATE REPORT", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MoodItem(emoji: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(text = emoji, fontSize = 32.sp)
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun ExpandableSymptomCard(isExpanded: Boolean,
                          symptomNotes: String, //needs the current notes
                          onToggle: () -> Unit,
                          onSymptomsChanged: (String) -> Unit //callback when user types
                          ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onToggle() }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Specify Symptoms Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                //icon hint
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                //functional input box
                OutlinedTextField(
                    value = symptomNotes,
                    onValueChange = onSymptomsChanged,
                    label = { Text("List symptoms (e.g., headache, cough)") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                    ),
                    maxLines = 3 //prevents card from being too tall
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EntryScreenPreview() {
    AppTheme {
        val mockViewModel: CareGoViewModel = viewModel()
        EntryScreen(
            viewModel = mockViewModel,
            onNextButtonClicked = {}
        )
    }
}