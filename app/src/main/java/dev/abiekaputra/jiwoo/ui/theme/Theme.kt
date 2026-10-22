package dev.abiekaputra.jiwoo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val JiwooColors =
    lightColorScheme(
        primary = JiwooIndigo,
        onPrimary = androidx.compose.ui.graphics.Color.White,
        primaryContainer = JiwooMist,
        onPrimaryContainer = JiwooIndigoDark,
        secondary = JiwooCoral,
        onSecondary = androidx.compose.ui.graphics.Color.White,
        secondaryContainer = ColorTokens.CoralSoft,
        onSecondaryContainer = JiwooInk,
        background = JiwooSurface,
        onBackground = JiwooInk,
        surface = androidx.compose.ui.graphics.Color.White,
        onSurface = JiwooInk,
        surfaceVariant = JiwooLavender,
        onSurfaceVariant = JiwooMuted,
        outline = JiwooLine,
        error = ColorTokens.Error,
    )

private val JiwooTypography =
    Typography(
        displaySmall =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 41.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp,
                lineHeight = 33.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 21.sp,
            ),
        bodyLarge =
            TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp, lineHeight = 24.sp),
        labelLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            ),
    )

@Composable
fun JiwooTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = JiwooColors, typography = JiwooTypography, content = content)
}

private object ColorTokens {
    val CoralSoft = androidx.compose.ui.graphics.Color(0xFFFFE4E1)
    val Error = androidx.compose.ui.graphics.Color(0xFFB42318)
}
