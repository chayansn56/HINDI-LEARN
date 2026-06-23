package com.example.hindilearn

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object PathRoute : NavKey
@Serializable data class LessonRoute(val nodeId: String, val nodeType: String) : NavKey
@Serializable data object AITutorRoute : NavKey
@Serializable data object LanguageSelectionRoute : NavKey
@Serializable data object SettingsRoute : NavKey
@Serializable data class ModuleOverviewRoute(val seasonId: String) : NavKey
@Serializable data class ComingSoonRoute(val title: String, val description: String) : NavKey
@Serializable data object AlphabetRoute : NavKey
@Serializable data object StoriesRoute : NavKey
@Serializable data object RevisionRoute : NavKey
@Serializable data object DailyChallengeRoute : NavKey
@Serializable data object AchievementsRoute : NavKey


