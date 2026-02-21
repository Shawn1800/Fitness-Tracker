package com.example.demo103

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demo103.di.Demo103App
import com.example.demo103.ui.theme.exercise_selection.ExerciseSelectionScreen
import com.example.demo103.ui.theme.exercise_selection.ExerciseViewModel
import com.example.demo103.ui.theme.exercise_selection.ExerciseViewModelFactory
import com.example.demo103.ui.theme.home.HomeScreen
import com.example.demo103.ui.theme.home.HomeViewModel
import com.example.demo103.ui.theme.home.HomeViewModelFactory
import com.example.demo103.ui.theme.theme.Demo103Theme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as Demo103App


        enableEdgeToEdge()

        setContent {
            Demo103Theme {

                val navController = rememberNavController() // ✅ CORRECT PLACE

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {

                    composable("home") {
                        val homeViewModel: HomeViewModel = viewModel(
                            factory = HomeViewModelFactory(
                                app.appContainer.workoutRepository
                            )
                        )

                        HomeScreen(
                            homeViewModel = homeViewModel,
                            onNavigateToExerciseSelection = {
                                navController.navigate("exercise_selection")
                            }
                        )
                    }

                    composable("exercise_selection") {
                        val app = LocalContext.current.applicationContext as Demo103App

                        val viewModel: ExerciseViewModel = viewModel(
                            factory = ExerciseViewModelFactory(
                                app.appContainer.exerciseRepository
                            )
                        )

                        ExerciseSelectionScreen(viewModel = viewModel)

                    }
                }

            }
        }
    }}

