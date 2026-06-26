package com.example.hindilearn.ui.gamified

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.theme.*

import androidx.compose.foundation.isSystemInDarkTheme

// Premium Background Generator (Animated)
@Composable
fun PremiumBackground(content: @Composable BoxScope.() -> Unit) {
    val isDark = when (com.example.hindilearn.data.UserManager.progress.themeMode) {
        "DARK" -> true
        "LIGHT" -> false
        else -> isSystemInDarkTheme()
    }
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val color1 by infiniteTransition.animateColor(
        initialValue = if (isDark) {
            Color(0xFF0C1D3A) // Deep Brand Blue-Navy
        } else {
            Color(0xFFEBF3FC) // Very soft Aqua Sky Blue
        },
        targetValue = if (isDark) {
            Color(0xFF061022) // Darker Brand Midnight
        } else {
            Color(0xFFF7FAFD) // Pure water white
        },
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "c1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = if (isDark) {
            Color(0xFF132A52) // Dark Navy Blue
        } else {
            Color(0xFFD4E6FA) // Soft watercolor Aqua Blue
        },
        targetValue = if (isDark) {
            Color(0xFF1E3F79) // Lighter blue accent
        } else {
            Color(0xFFFFFBF0) // Subtle gold undertone from tag line
        },
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "c2"
    )

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(color1, color2)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        content = {
            // Optional watermark
            Text(
                "VIETANA",
                color = if (isDark) VietanaBlue.copy(alpha = 0.04f) else VietanaBlue.copy(alpha = 0.06f),
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.align(Alignment.Center)
            )
            content()
        }
    )
}

// Glassmorphism Card with 32dp corners and soft shadow
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    containerColor: Color = Color.Unspecified, // Evaluated inside
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SurfaceTranslucent.copy(alpha = 0.4f),
                        SurfaceTranslucent.copy(alpha = 0.1f)
                    )
                )
            )
            .background(if (isDark) SurfaceDarkTranslucent else SurfaceTranslucent)
    ) {
        content()
    }
}

// Premium Scalable Button
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = DeepSaffron,
    textColor: Color = PureWhite
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "buttonScale")
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(32.dp),
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = color.copy(alpha = 0.3f),
                ambientColor = color.copy(alpha = 0.3f)
            )
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
}

@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = DeepSaffron
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progress"
    )
    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = color,
        trackColor = RoyalBlue.copy(alpha = 0.1f)
    )
}
