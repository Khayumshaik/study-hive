package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EvergreenColorScheme = darkColorScheme(
    primary = VibrantEmerald,
    onPrimary = DarkGreenText,
    primaryContainer = VibrantEmerald,
    onPrimaryContainer = DarkGreenText,
    secondary = SoftMint,
    onSecondary = DarkGreenText,
    secondaryContainer = BorderMoss,
    onSecondaryContainer = SoftMint,
    tertiary = PaleSpearmint,
    onTertiary = DarkGreenText,
    background = DeepPine,
    onBackground = CrispWhite,
    surface = OpaqueMoss,
    onSurface = CrispWhite,
    surfaceVariant = BorderMoss,
    onSurfaceVariant = LightSage,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = OpaqueMoss,
    surfaceContainer = OpaqueMoss,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = BorderMoss,
    outlineVariant = BorderMoss
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EvergreenColorScheme,
        typography = Typography,
        content = content
    )
}
