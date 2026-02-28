package com.example.demo103.ui.theme.exercise_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.di.Demo103App
import com.example.demo103.ui.theme.home.HomeViewModel
import kotlin.collections.List

private object AppColors {
    val Background = Color.Black
    val Surface = Color.DarkGray
    val Primary = Color(0xFF6200EE)
    val TextPrimary = Color.White
    val TextSecondary = Color.Gray
    val ChipSelected = Color(0xFF6200EE)
    val ChipUnselected = Color.DarkGray
}
@Composable
fun ExerciseSelectionScreen(
    viewModel: ExerciseViewModel = viewModel(),
    homeViewModel: HomeViewModel,
    navController:() -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            event ->
            when(event){
                is ExerciseUiEvent.AddExercise->{
                    homeViewModel.addExerciseToWorkout(exercise=event.exercise)
                    navController()
                }
                ExerciseUiEvent.NavigateBack -> {
                    navController()
                }
            }
        }
    }

    ExerciseSelectionContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
@Composable
fun ExerciseSelectionContent(
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
) {
    Search(
        query = state.searchQuery,
        selectedCategory = state.selectedCategory,
        isSearching = state.isSearching,
        exercises = state.exercises,
        onQueryChange = {
            onEvent(ExerciseEvent.OnSearchQueryChange(it))
        },
        onCategorySelected = {
            onEvent(ExerciseEvent.OnSelectCategory(it))
        },
        onCategoryCleared = {
            onEvent(ExerciseEvent.OnClearCategory)
        },
        onAddExercise= {
            onEvent(ExerciseEvent.OnAddExercise(it))
        }
    )
}
@Composable
fun Search(
    query: String,
    selectedCategory: String?,
    isSearching: Boolean,
    exercises: List<ExerciseEntity>,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onCategoryCleared: () -> Unit,
    onAddExercise: (ExerciseEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(top = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search exercises") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        when {
            isSearching -> LoadingIndicator()
            exercises.isEmpty() -> EmptyMessage()
            else -> ExerciseList(
                exercises = exercises,
                onClickExercise = onAddExercise
            )
        }
    }
}

@Composable
fun ExerciseList(
    exercises: List<ExerciseEntity>,
    onClickExercise: (ExerciseEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = exercises,
            key = { it.exerciseId }
        ) { exercise ->
            ExerciseItem(
                exercise = exercise,
                onClickExercise = {
                    onClickExercise(exercise)
                }
            )
        }
    }
}
@Composable
private fun ExerciseItem(
    exercise: ExerciseEntity,
    onClickExercise: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: color bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppColors.Primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Middle: name + category
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.exerciseName,
                    color = AppColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.category,
                    color = AppColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
            // Right: add button
            IconButton(onClick = onClickExercise){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add exercise",
                    tint = AppColors.Primary
                )
            }
        }
    }
}


@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AppColors.Primary)
    }
}
@Composable
private fun EmptyMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No workouts logged for this day",
            color =AppColors.TextSecondary,
            fontSize = 16.sp
        )
    }
}
@Composable
private fun ErrorMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 16.sp
        )
    }
}