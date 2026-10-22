package dev.abiekaputra.jiwoo.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.abiekaputra.jiwoo.JiwooViewModel
import dev.abiekaputra.jiwoo.ui.screens.CheckInScreen
import dev.abiekaputra.jiwoo.ui.screens.InsightsScreen
import dev.abiekaputra.jiwoo.ui.screens.JournalScreen
import dev.abiekaputra.jiwoo.ui.screens.OnboardingScreen
import dev.abiekaputra.jiwoo.ui.screens.SettingsScreen
import dev.abiekaputra.jiwoo.ui.screens.TodayScreen

private enum class Destination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    TODAY("today", "Today", Icons.Outlined.Home),
    JOURNAL("journal", "Journal", Icons.Outlined.Book),
    INSIGHTS("insights", "Insights", Icons.Outlined.AutoGraph),
    SETTINGS("settings", "Settings", Icons.Outlined.Settings),
}

@Composable
fun JiwooApp(viewModel: JiwooViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when {
        state.loading -> LoadingScreen()
        !state.settings.onboardingComplete ->
            OnboardingScreen(onContinue = viewModel::completeOnboarding)
        else -> MainExperience(viewModel)
    }
}

@Composable
private fun MainExperience(viewModel: JiwooViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.setReminder(granted)
        }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbar.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        bottomBar = {
            if (currentRoute != "checkin") {
                NavigationBar {
                    Destination.entries.forEach { destination ->
                        NavigationBarItem(
                            modifier = Modifier.testTag("nav-${destination.route}"),
                            selected = currentRoute == destination.route,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(destination.icon, contentDescription = destination.label)
                            },
                            label = { Text(destination.label) },
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbar) { data ->
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Text(
                        data.visuals.message,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.TODAY.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Destination.TODAY.route) {
                TodayScreen(
                    entries = state.entries,
                    onNewReflection = { navController.navigate("checkin") },
                    onOpenJournal = { navController.navigate(Destination.JOURNAL.route) },
                )
            }
            composable("checkin") {
                CheckInScreen(
                    onBack = { navController.popBackStack() },
                    onSave = viewModel::save,
                )
            }
            composable(Destination.JOURNAL.route) {
                JournalScreen(entries = state.entries, onDelete = viewModel::delete)
            }
            composable(Destination.INSIGHTS.route) { InsightsScreen(insights = state.insights) }
            composable(Destination.SETTINGS.route) {
                SettingsScreen(
                    reminderEnabled = state.settings.reminderEnabled,
                    onReminderChange = { enabled ->
                        if (!enabled || Build.VERSION.SDK_INT < 33) {
                            viewModel.setReminder(enabled)
                        } else if (
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS,
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.setReminder(true)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}
