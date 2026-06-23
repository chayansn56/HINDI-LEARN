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
    background = WarmIvory,
    surface = PureWhite,
    onPrimary = TextLight,
    onSecondary = TextDark,
    onTertiary = TextLight,
    onBackground = TextDark,
    onSurface = TextDark,
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
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
