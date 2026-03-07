package com.example.demo103.ui.theme.log_workout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo103.data.entity.ExerciseEntity


private object AppColors {
    val Background = Color.Black
    val Surface = Color.DarkGray
    val SurfaceDimmed = Color.DarkGray.copy(alpha = 0.5f)
    val Primary = Color(0xFF6200EE)
    val Today = Color(0xFF311B92)
    val TextPrimary = Color.White
    val TextSecondary = Color.Gray
}

@Composable
fun LogWorkoutScreen(
    exercise: ExerciseEntity,
    logWorkoutViewModel: LogWorkoutViewModel = viewModel(),
    onBack: () -> Unit,
    ) {

   LaunchedEffect(exercise) {
       logWorkoutViewModel.onEvent(LogWorkoutEvent.SetExercise(exercise))
   }


    val state by logWorkoutViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        logWorkoutViewModel.uiEvent.collect { event ->
            when (event) {
                is LogWorkoutUiEvent.NavBackToHome -> {
                    onBack()
                }

                is LogWorkoutUiEvent.SendSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

            }

        }
    }
    Scaffold(
        containerColor = AppColors.Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            SaveBttn(
                onClick = {
                    logWorkoutViewModel.onEvent(LogWorkoutEvent.SaveWorkout)
                }
            )
        }
    )
    { paddingValues ->
        LogWorkoutContent(
            state = state,
            onEvent = { event -> logWorkoutViewModel.onEvent(event) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun LogWorkoutContent(
    state: LogWorkoutState,
    onEvent: (LogWorkoutEvent) -> Unit,
    modifier: Modifier
) {


}

@Composable
private fun SaveBttn(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = AppColors.Primary
    ) {
        Text("+", fontSize = 24.sp, color = AppColors.TextPrimary)
    }
}