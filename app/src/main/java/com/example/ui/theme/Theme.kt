package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val BarberColorScheme = darkColorScheme(
    primary = BarberGold,
    onPrimary = MidnightBlack,
    primaryContainer = LightGraySlate,
    onPrimaryContainer = BarberGoldLight,
    secondary = BarberGoldLight,
    onSecondary = MidnightBlack,
    tertiary = EmeraldLive,
    onTertiary = MidnightBlack,
    background = MidnightBlack,
    onBackground = CleanWhite,
    surface = DeepGraphite,
    onSurface = CleanWhite,
    surfaceVariant = LightGraySlate,
    onSurfaceVariant = CleanWhite,
    outline = LightGraySlate,
    error = RubyRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // We default to Dark Mode always for the Clean Minimalism Indigo & Slate aesthetic
    dynamicColor: Boolean = false, // Disable dynamic colors keying to force our brand identity
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = BarberColorScheme,
        typography = Typography,
        content = content
    )
}
