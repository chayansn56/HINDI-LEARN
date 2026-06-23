package com.example.hindilearn.ui.gamified

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    var rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiOverlay(modifier: Modifier = Modifier) {
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(16, easing = LinearEasing)),
        label = "time"
    )
    
    // Initialize particles once
    LaunchedEffect(Unit) {
        val colors = listOf(Color(0xFFFFC107), Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF2196F3))
        particles = List(100) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * 0.5f - 0.5f,
                vx = Random.nextFloat() * 0.02f - 0.01f,
                vy = Random.nextFloat() * 0.02f + 0.01f,
                color = colors.random(),
                size = Random.nextFloat() * 20f + 10f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 10f - 5f
            )
        }
    }
    
    // Update logic tied to time state
    LaunchedEffect(time) {
        particles = particles.map {
            it.copy(
                x = it.x + it.vx,
                y = it.y + it.vy,
                rotation = it.rotation + it.rotationSpeed
            )
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val px = p.x * size.width
            val py = p.y * size.height
            if (py < size.height) {
                rotate(degrees = p.rotation, pivot = Offset(px, py)) {
                    drawRect(
                        color = p.color,
                        topLeft = Offset(px, py),
                        size = Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}
