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

// Premium Background Generator (Animated)
@Composable
fun PremiumBackground(content: @Composable BoxScope.() -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val color1 by infiniteTransition.animateColor(
        initialValue = WarmIvory,
        targetValue = DeepSaffron.copy(alpha = 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "c1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = DeepSaffron.copy(alpha = 0.05f),
        targetValue = RoyalBlue.copy(alpha = 0.05f),
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
                color = DeepSaffron.copy(alpha = 0.05f),
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
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = shape,
                spotColor = RoyalBlue.copy(alpha = 0.05f),
                ambientColor = RoyalBlue.copy(alpha = 0.05f)
            )
            .clip(shape),
        color = PureWhite.copy(alpha = 0.9f),
        shape = shape,
        shadowElevation = 0.dp
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
