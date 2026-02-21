package com.example.demo103.ui.theme.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.demo103.ui.theme.exercise_selection.ExerciseEvent
import com.example.demo103.ui.theme.exercise_selection.ExerciseViewModel

// ─── Theme Constants ──────────────────────────────────────────────────────────
private object AppColors {
    val Background = Color.Black
    val Surface = Color.DarkGray
    val SurfaceDimmed = Color.DarkGray.copy(alpha = 0.5f)
    val Primary = Color(0xFF6200EE)
    val Today = Color(0xFF03DAC5)
    val TextPrimary = Color.White
    val TextSecondary = Color.Gray
}

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    onNavigateToExerciseSelection: () -> Unit,

) {
    val state by homeViewModel.state.collectAsState()


    // Collect one-shot navigation events
    LaunchedEffect(Unit) {
        homeViewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToExerciseSelection -> {
                    onNavigateToExerciseSelection()
                }
            }
        }
    }

    Scaffold(
        containerColor = AppColors.Background,
        floatingActionButton = {
            AddWorkoutFab(
                onClick = {
                    homeViewModel.onEvent(HomeEvent.OnAddWorkoutClick) }
            )
        }
    ) { paddingValues ->
        HomeContent(
            state = state,
            paddingValues = paddingValues,
            onDateSelected = { date -> homeViewModel.onEvent(HomeEvent.OnDateSelected(date)) }
        )
    }
}

// ─── Content ──────────────────────────────────────────────────────────────────

@Composable
private fun HomeContent(
    state: HomeState,
    paddingValues: PaddingValues,
    onDateSelected: (LocalDate) -> Unit
) {
    val currentDate = LocalDate.now()
    val monthName = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(paddingValues)
    ) {
        MonthHeader(monthName = monthName)

        Spacer(modifier = Modifier.height(16.dp))

        WeeklyCalendar(
            selectedDate = currentDate,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> LoadingIndicator()
            state.errorMessage != null -> ErrorMessage(message = state.errorMessage)
            state.workouts.isEmpty() -> EmptyWorkoutsMessage()
            else -> WorkoutList(workouts = state.workouts)
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@Composable
private fun MonthHeader(monthName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = monthName,
            color = AppColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}
// ─── Calendar ─────────────────────────────────────────────────────────────────

@Composable
private fun WeeklyCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val totalWeeks = 500
    val pagerState = rememberPagerState(
        pageCount = { totalWeeks },
        initialPage = totalWeeks - 1  // Start at current week; past weeks to the left
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) { page ->
        val weeksAgo = (totalWeeks - 1) - page
        val mondayOfWeek = LocalDate.now()
            .minusWeeks(weeksAgo.toLong())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        WeekRow(
            startDate = mondayOfWeek,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun WeekRow(
    startDate: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            val date = startDate.plusDays(i.toLong())
            DateItem(
                date = date,
                isToday = date == LocalDate.now(),
                isSelected = date == selectedDate,
                isFuture = date.isAfter(LocalDate.now()),
                onDateSelected = onDateSelected
            )
        }
    }
}
@Composable
private fun DateItem(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    isFuture: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayNumber = date.dayOfMonth.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .alpha(if (isFuture) 0.3f else 1f)
            .clickable(enabled = !isFuture) { onDateSelected(date) }
    ) {
        Text(
            text = dayName,
            color = AppColors.TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.size(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isSelected -> AppColors.Primary        // Purple  = selected by user
                    isToday -> AppColors.Today             // Teal    = today (not selected)
                    isFuture -> AppColors.SurfaceDimmed   // Dimmed  = future
                    else -> AppColors.Surface             // Grey    = past
                }
            )
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = dayNumber,
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─── FAB ──────────────────────────────────────────────────────────────────────

@Composable
private fun AddWorkoutFab(onClick: ()->Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = AppColors.Primary
    ) {
        Text("+", fontSize = 24.sp, color = AppColors.TextPrimary)
    }
}

// ─── Workout List ─────────────────────────────────────────────────────────────

@Composable
private fun WorkoutList(workouts: List<com.example.demo103.data.entity.WorkoutEntryEntity>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        workouts.forEach { workout ->
            WorkoutItem(workout = workout)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WorkoutItem(workout: com.example.demo103.data.entity.WorkoutEntryEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface)
    ) {
        Text(
            text = workout.toString(), // Replace with your actual fields
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// ─── Empty / Loading / Error States ──────────────────────────────────────────

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AppColors.Primary)
    }
}

@Composable
private fun EmptyWorkoutsMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No workouts logged for this day",
            color = AppColors.TextSecondary,
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