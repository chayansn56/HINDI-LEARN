package com.example.hindilearn.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DeepSaffron,
    secondary = PremiumGold,
    tertiary = RoyalBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = TextLight,
    onSecondary = TextDark,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight,
)

private val LightColorScheme = lightColorScheme(
    primary = DeepSaffron,
    secondary = PremiumGold,
    tertiary = RoyalBlue,
    background = WarmIvory,
    surface = PureWhite,
    onPrimary = TextLight,
    onSecondary = TextDark,
    onTertiary = TextLight,
    onBackground = TextDark,
    onSurface = TextDark,
)

@Composable
fun HindiLearnTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
  }
  
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
