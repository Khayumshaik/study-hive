package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddTaskDialog
import com.example.ui.components.ConfettiOverlay
import com.example.ui.components.CreatePostBottomSheet
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.SearchDialog
import com.example.ui.components.StudyHiveBottomNav
import com.example.ui.components.StudyHiveTopBar
import com.example.ui.screens.chat.StudyCirclesChatScreen
import com.example.ui.screens.feed.CampusFeedScreen
import com.example.ui.screens.login.LoginScreen
import com.example.ui.screens.profile.StudentProfileScreen
import com.example.ui.screens.tracker.TrackerScreen
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DeepPine
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.MainNavigationTab
import com.example.ui.viewmodel.StudyHiveViewModel
import com.example.ui.viewmodel.TrackerSubTab
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                StudyHiveApp()
            }
        }
    }
}

@Composable
fun StudyHiveApp(
    viewModel: StudyHiveViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val showCreateSheet by viewModel.showCreateSheet.collectAsState()
    val createFormType by viewModel.createFormType.collectAsState()
    val showSettingsDrawer by viewModel.showSettingsDrawer.collectAsState()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsState()
    val showSearchDialog by viewModel.showSearchDialog.collectAsState()
    val showAddTaskDialog by viewModel.showAddTaskDialog.collectAsState()
    val confettiCount by viewModel.confettiCount.collectAsState()

    val tasks by viewModel.allTasks.collectAsState()
    val resources by viewModel.allResources.collectAsState()
    val studentProfile by viewModel.studentProfile.collectAsState()

    // Floating Custom Toast State
    var floatingToastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.repository.toastEvent.collect { msg ->
            floatingToastMessage = msg
            delay(2600)
            floatingToastMessage = null
        }
    }

    // Top Bar Subtitle
    val topBarSubtitle = when (currentTab) {
        MainNavigationTab.HOME -> "Campus Feed"
        MainNavigationTab.TRACKER -> "Academic Tracker"
        MainNavigationTab.CHAT -> "Study Circles Chat"
        MainNavigationTab.PROFILE -> "Student Profile"
    }

    // Back handling
    BackHandler(enabled = isLoggedIn && currentTab != MainNavigationTab.TRACKER) {
        viewModel.setNavigationTab(MainNavigationTab.TRACKER)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepPine)
    ) {
        if (!isLoggedIn) {
            // Login Screen View
            LoginScreen(
                onLoginSuccess = { viewModel.logIn() },
                onRegisterSuccess = { name, pronouns, email, roll, dept, sec, bio ->
                    viewModel.registerNewAccount(name, pronouns, email, roll, dept, sec, bio)
                }
            )
        } else {
            // Main Authenticated Campus App View
            Scaffold(
                topBar = {
                    StudyHiveTopBar(
                        subtitle = topBarSubtitle,
                        onSearchClick = { viewModel.setSearchDialogOpen(true) },
                        onNotificationClick = { viewModel.setNotificationsSheetOpen(true) },
                        onProfileClick = { viewModel.setNavigationTab(MainNavigationTab.PROFILE) }
                    )
                },
                bottomBar = {
                    StudyHiveBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.setNavigationTab(it) },
                        onFabClick = { viewModel.openCreateModal() },
                        unreadChatCount = 4
                    )
                },
                containerColor = DeepPine
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        MainNavigationTab.HOME -> CampusFeedScreen(viewModel = viewModel)
                        MainNavigationTab.TRACKER -> TrackerScreen(viewModel = viewModel)
                        MainNavigationTab.CHAT -> StudyCirclesChatScreen(viewModel = viewModel)
                        MainNavigationTab.PROFILE -> StudentProfileScreen(viewModel = viewModel)
                    }
                }
            }
        }

        // Global Confetti Explosion Overlay
        ConfettiOverlay(
            triggerCount = confettiCount,
            modifier = Modifier.fillMaxSize()
        )

        // Floating Toast Notification Pill
        AnimatedVisibility(
            visible = floatingToastMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp)
        ) {
            floatingToastMessage?.let { msg ->
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, CircleShape)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = msg,
                        color = CrispWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // ==================== OVERLAYS & SHEETS ====================

        // Central '+' Create Post Bottom Sheet
        CreatePostBottomSheet(
            isOpen = showCreateSheet,
            currentFormType = createFormType,
            studentProfile = studentProfile,
            onDismiss = { viewModel.closeCreateModal() },
            onSelectType = { viewModel.selectCreateOption(it) },
            onBackToMenu = { viewModel.backToCreateMenu() },
            onPublish = { type, title, content, tags, image, doc ->
                viewModel.publishPost(type, title, content, tags, image, doc)
            }
        )

        // Notifications Sheet
        NotificationsSheet(
            isOpen = showNotificationsSheet,
            onDismiss = { viewModel.setNotificationsSheetOpen(false) }
        )

        // Global Search Dialog
        SearchDialog(
            isOpen = showSearchDialog,
            tasks = tasks,
            resources = resources,
            onDismiss = { viewModel.setSearchDialogOpen(false) },
            onSelectTask = { task ->
                viewModel.setNavigationTab(MainNavigationTab.TRACKER)
                viewModel.setTrackerSubTab(TrackerSubTab.DEADLINES)
            },
            onSelectResource = { res ->
                viewModel.setNavigationTab(MainNavigationTab.TRACKER)
                viewModel.setTrackerSubTab(TrackerSubTab.VAULT)
            }
        )

        // Add Task Dialog
        AddTaskDialog(
            isOpen = showAddTaskDialog,
            onDismiss = { viewModel.setAddTaskDialogOpen(false) },
            onAddTask = { title, course, dueDate, isUrgent ->
                viewModel.addTask(title, course, dueDate, isUrgent)
            }
        )
    }
}
