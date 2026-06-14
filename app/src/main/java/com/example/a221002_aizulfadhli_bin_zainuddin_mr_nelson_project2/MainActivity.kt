package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.CommunityScreen
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.TrackerScreen
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme.AppTheme
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme.HistoryScreen
import com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2.ui.theme.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //Add theme from Theme.kt
            AppTheme {
                //Initialize the Controller and ViewModel
                val navController = rememberNavController()
                val viewModel: CareGoViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //The NavHost defines the "Map"
                    NavHost(
                        navController = navController,
                        startDestination = CareGoScreen.Home.name
                    ) {


                        // SCREEN 1: HOME (DASHBOARD)
                        composable(route = CareGoScreen.Home.name) {
                            //collect the UI state right here so Home can watch the network variables
                            val uiState by viewModel.uiState.collectAsState()
                            HomeScreen(
                                //pass the real text from the Web API down into the screen parameters
                                apiHealthTip = uiState.apiHealthTip,
                                onStartLogClick = {
                                    navController.navigate(CareGoScreen.Start.name)
                                },
                                onViewHistoryClick = {
                                    navController.navigate(CareGoScreen.History.name)
                                },
                                // 🆕 ADD THESE TWO NEW CLICK ACTIONS TO HOME
                                onCommunityClick = {
                                    navController.navigate(CareGoScreen.Community.name)
                                },
                                onTrackerClick = {
                                    navController.navigate(CareGoScreen.Tracker.name)
                                }

                            )
                        }


                        //SCREEN 2: START (SDG INFO)
                        composable(route = CareGoScreen.Start.name) {
                            StartScreen(
                                onNextButtonClicked = {
                                    navController.navigate(CareGoScreen.Entry.name)
                                }
                            )
                        }

                        //SCREEN 3: ENTRY (FORM)
                        composable(route = CareGoScreen.Entry.name) {
                            EntryScreen(
                                viewModel = viewModel,
                                onNextButtonClicked = {
                                    viewModel.saveEntry() //call viewmodel here so the data move to history
                                    navController.navigate(CareGoScreen.Summary.name)
                                }
                            )
                        }

                        // SCREEN 4: SUMMARY (REPORT)
                        composable(route = CareGoScreen.Summary.name) {
                            SummaryScreen(
                                viewModel = viewModel,
                                onRestartButtonClicked = {
                                    //Go back to home
                                    navController.popBackStack(CareGoScreen.Home.name, inclusive = false)
                                }
                            )
                        }

                        // SCREEN 5: HISTORY (THE LIST)
                        composable(route = CareGoScreen.History.name) {
                            val uiState by viewModel.uiState.collectAsState()

                            HistoryScreen(
                                historyList = uiState.healthHistory,
                                onBackClick = { //inclusive false , go back home but don't close home
                                    navController.popBackStack(CareGoScreen.Home.name, inclusive = false)
                                } //use popback to close all pages in between and return to start
                            )
                        }

                        // 🆕 SCREEN 6: COMMUNITY MOTIVATION FEED (FIREBASE CLOUD)
                        composable(route = CareGoScreen.Community.name) {
                            CommunityScreen(
                                viewModel = viewModel, //pass the viewmodel
                                onBackClick = {
                                    navController.popBackStack(CareGoScreen.Home.name, inclusive = false)
                                }
                            )
                        }

                        // 🆕 SCREEN 7: EMERGENCY CLINIC TRACKER (GPS SENSOR)
                        composable(route = CareGoScreen.Tracker.name) {
                            TrackerScreen(
                                viewModel = viewModel,
                                onBackClick = {
                                    navController.popBackStack(CareGoScreen.Home.name, inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
