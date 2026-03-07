package com.example.demo103.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object HomeScreen: Route , NavKey

    @Serializable
    data object ExerciseScreen : Route , NavKey


}