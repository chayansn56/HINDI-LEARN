package com.example.hindilearn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.hindilearn.ui.gamified.LessonScreen
import com.example.hindilearn.ui.gamified.AcademyDashboard
import com.example.hindilearn.ui.gamified.SplashScreen
import com.example.hindilearn.ui.gamified.AlphabetScreen
import com.example.hindilearn.ui.gamified.StoriesScreen
import com.example.hindilearn.ui.gamified.RevisionScreen
import com.example.hindilearn.ui.gamified.DailyChallengeScreen
import androidx.compose.animation.Crossfade

import androidx.navigation3.runtime.NavKey

@kotlinx.serialization.Serializable
data object SplashRoute : NavKey

@kotlinx.serialization.Serializable
data object MemoryRoute : NavKey

@kotlinx.serialization.Serializable
data object LeaderboardRoute : NavKey

@kotlinx.serialization.Serializable
data object RoleplayRoute : NavKey

@kotlinx.serialization.Serializable
data object PronunciationLabRoute : NavKey

@android.annotation.SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun MainNavigation() {
  val startRoute = SplashRoute
  
  val backStack = rememberNavBackStack(startRoute)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<SplashRoute> {
            SplashScreen(onSplashFinished = {
                backStack.removeLastOrNull()
                if (com.example.hindilearn.data.UserManager.progress.selectedLanguage == null) {
                    backStack.add(LanguageSelectionRoute)
                } else {
                    backStack.add(PathRoute)
                }
            })
        }
        entry<LanguageSelectionRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.LanguageSelectionScreen(
                    onLanguageSelected = { 
                        backStack.removeLastOrNull() // Clear selection screen
                        backStack.add(PathRoute) 
                    },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<PathRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
              AcademyDashboard(
                  onNodeSelected = { node -> 
                      val isComingSoon = node.id.startsWith("episode_") && 
                              !node.id.startsWith("episode_0_") && 
                              node.id.substringAfter("episode_").all { it.isDigit() }
                      if (node.type == "AI_TUTOR") {
                          backStack.add(AITutorRoute)
                      } else if (isComingSoon) {
                          backStack.add(ComingSoonRoute(node.title, "Season stories are coming soon!"))
                      } else {
                          backStack.add(LessonRoute(node.id, node.type)) 
                      }
                  },
                  onModuleSelected = { seasonId ->
                      backStack.add(ModuleOverviewRoute(seasonId))
                  },
                  onSettingsSelected = {
                      backStack.add(SettingsRoute)
                  },
                  onAlphabetSelected = {
                      backStack.add(AlphabetRoute)
                  },
                  onStoriesSelected = {
                      backStack.add(StoriesRoute)
                  },
                  onRevisionSelected = {
                      backStack.add(RevisionRoute)
                  },
                  onDailyChallengeSelected = {
                      backStack.add(DailyChallengeRoute)
                  },
                  onAchievementsSelected = {
                      backStack.add(AchievementsRoute)
                  },
                  onLeaderboardSelected = {
                      backStack.add(LeaderboardRoute)
                  },
                  onRoleplaySelected = {
                      backStack.add(RoleplayRoute)
                  },
                  onPronunciationLabSelected = {
                      backStack.add(PronunciationLabRoute)
                  },
                  modifier = Modifier.safeDrawingPadding()
              )
            }
        }
        entry<LessonRoute> { key ->
            Crossfade(targetState = true, label = "fade") { _ ->
                LessonScreen(
                    nodeId = key.nodeId,
                    nodeType = key.nodeType,
                    onFinish = { backStack.removeLastOrNull() },
                    onClose = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<AITutorRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.tutor.AITutorScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<SettingsRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.main.SettingsScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onCourseSwitched = {
                        backStack.clear()
                        backStack.add(SplashRoute)
                    },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<MemoryRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.MemoryScreen()
            }
        }
        entry<ComingSoonRoute> { key ->
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.ComingSoonScreen(
                    title = key.title,
                    description = key.description,
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<ModuleOverviewRoute> { key ->
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.ModuleOverviewScreen(
                    seasonId = key.seasonId,
                    onEpisodeSelected = { episode ->
                        val isComingSoon = episode.id.startsWith("episode_") && 
                                !episode.id.startsWith("episode_0_") && 
                                episode.id.substringAfter("episode_").all { it.isDigit() }
                        if (episode.type == "AI_TUTOR") {
                            backStack.add(AITutorRoute)
                        } else if (isComingSoon) {
                            backStack.add(ComingSoonRoute(episode.title, "Season stories are coming soon!"))
                        } else {
                            backStack.add(LessonRoute(episode.id, episode.type))
                        }
                    },
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<AlphabetRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                AlphabetScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<StoriesRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                StoriesScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<RevisionRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                RevisionScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<DailyChallengeRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                DailyChallengeScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<AchievementsRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.AchievementsScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<LeaderboardRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.LeaderboardScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<RoleplayRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.RoleplayScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
        entry<PronunciationLabRoute> {
            Crossfade(targetState = true, label = "fade") { _ ->
                com.example.hindilearn.ui.gamified.PronunciationLabScreen(
                    onBack = { backStack.removeLastOrNull() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
      },
  )
}
