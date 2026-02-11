package com.example.demo103

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.ui.exercise.ExerciseViewModel

class HomeScreen {
}
@Composable
fun ExerciseScreen(
    viewModel: ExerciseViewModel,
    onNavigateToDetail: (ExerciseEntity) -> Unit
) {
    val exercises by viewModel.exercises.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    // Listen for navigation events
    LaunchedEffect(Unit) {
        viewModel.navigateToDetail.collect { exercise ->
            onNavigateToDetail(exercise)
        }
    }

    Column {
        SearchBar(
            onQueryChange = { viewModel.updateSearchQuery(it) }
        )

        CategoryFilter(
            onCategorySelected = { viewModel.selectCategory(it) }
        )

        if (isSearching) {
            CircularProgressIndicator()
        }

        LazyColumn {
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onClick = {
                        viewModel.onExerciseSelected(exercise)  // ✅ Handle click
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }  // ✅ Make it clickable
            .padding(8.dp)


    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise image
            Image(
                painter = rememberImagePainter(exercise.imageUrl),
                contentDescription = exercise.name,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = exercise.category,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


