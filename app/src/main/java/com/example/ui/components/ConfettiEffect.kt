package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald
import kotlin.random.Random

data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float
)

@Composable
fun ConfettiOverlay(
    triggerCount: Int,
    modifier: Modifier = Modifier
) {
    if (triggerCount == 0) return

    val progress = remember(triggerCount) { Animatable(0f) }

    LaunchedEffect(triggerCount) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800, easing = LinearEasing)
        )
    }

    val colors = remember {
        listOf(VibrantEmerald, SoftMint, CrispWhite, BorderMoss, SurfaceContainerHigh)
    }

    val particles = remember(triggerCount) {
        List(50) {
            ConfettiParticle(
                initialX = 0.5f + (Random.nextFloat() - 0.5f) * 0.4f,
                initialY = 0.35f,
                vx = (Random.nextFloat() - 0.5f) * 500f,
                vy = -Random.nextFloat() * 450f - 150f,
                size = Random.nextFloat() * 12f + 8f,
                color = colors[Random.nextInt(colors.size)],
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val t = progress.value
            val alpha = (1f - t).coerceIn(0f, 1f)

            particles.forEach { p ->
                val x = (p.initialX * size.width) + p.vx * t
                val y = (p.initialY * size.height) + (p.vy * t) + (0.5f * 980f * t * t)
                val currentRotation = p.rotationSpeed * t

                rotate(degrees = currentRotation, pivot = Offset(x, y)) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(x - p.size / 2, y - p.size / 2),
                        size = Size(p.size, p.size * 0.7f)
                    )
                }
            }
        }
    }
}
