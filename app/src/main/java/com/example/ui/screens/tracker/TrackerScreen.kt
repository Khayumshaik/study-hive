package com.example.ui.screens.tracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassRepository
import com.example.data.model.ResourceEntity
import com.example.data.model.TaskEntity
import com.example.ui.components.CircularProgressMeter
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.StudyHiveViewModel
import com.example.ui.viewmodel.TaskFilter
import com.example.ui.viewmodel.TrackerSubTab

@Composable
fun TrackerScreen(
    viewModel: StudyHiveViewModel,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.trackerSubTab.collectAsState()
    val tasks by viewModel.allTasks.collectAsState()
    val filteredTasks by viewModel.filteredTasks.collectAsState()
    val currentFilter by viewModel.taskFilter.collectAsState()
    val resources by viewModel.allResources.collectAsState()
    val vaultQuery by viewModel.vaultSearchQuery.collectAsState()

    val completedTasksCount = tasks.count { it.isCompleted }
    val totalTasksCount = tasks.size
    val progressRatio = if (totalTasksCount > 0) completedTasksCount.toFloat() / totalTasksCount else 0.5f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPine)
            .padding(horizontal = 12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 4.dp, bottom = 24.dp)
    ) {
        // Top Tab Sub-navigation Bar: Deadlines (X) vs Resource Vault (89+)
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Deadlines
                val isDeadlines = subTab == TrackerSubTab.DEADLINES
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDeadlines) VibrantEmerald else OpaqueMoss)
                        .clickable { viewModel.setTrackerSubTab(TrackerSubTab.DEADLINES) }
                        .padding(vertical = 10.dp)
                        .testTag("tab_deadlines_btn"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Deadlines",
                        tint = if (isDeadlines) DarkGreenText else LightSage,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Deadlines",
                        color = if (isDeadlines) DarkGreenText else LightSage,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isDeadlines) DeepPine else BorderMoss)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "${tasks.count { !it.isCompleted }}",
                            color = SoftMint,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tab 2: Resource Vault
                val isVault = subTab == TrackerSubTab.VAULT
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isVault) VibrantEmerald else OpaqueMoss)
                        .clickable { viewModel.setTrackerSubTab(TrackerSubTab.VAULT) }
                        .padding(vertical = 10.dp)
                        .testTag("tab_vault_btn"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderSpecial,
                        contentDescription = "Resource Vault",
                        tint = if (isVault) DarkGreenText else LightSage,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Resource Vault",
                        color = if (isVault) DarkGreenText else LightSage,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isVault) DeepPine else BorderMoss)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "89+",
                            color = SoftMint,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (subTab == TrackerSubTab.DEADLINES) {
            // ==================== TAB 1: DEADLINES VIEW ====================

            // Horizontal Filter Pills
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .androidx.compose.foundation.horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterPill(
                            label = "All (${tasks.size})",
                            isSelected = currentFilter == TaskFilter.ALL,
                            onClick = { viewModel.setTaskFilter(TaskFilter.ALL) }
                        )
                        FilterPill(
                            label = "Overdue (${tasks.count { it.isUrgent && !it.isCompleted }})",
                            isSelected = currentFilter == TaskFilter.OVERDUE,
                            onClick = { viewModel.setTaskFilter(TaskFilter.OVERDUE) }
                        )
                        FilterPill(
                            label = "This Week (${tasks.count { !it.isCompleted && !it.isUrgent }})",
                            isSelected = currentFilter == TaskFilter.UPCOMING,
                            onClick = { viewModel.setTaskFilter(TaskFilter.UPCOMING) }
                        )
                        FilterPill(
                            label = "Completed (${tasks.count { it.isCompleted }})",
                            isSelected = currentFilter == TaskFilter.COMPLETED,
                            onClick = { viewModel.setTaskFilter(TaskFilter.COMPLETED) }
                        )
                    }

                    // Add Task Action
                    IconButton(
                        onClick = { viewModel.setAddTaskDialogOpen(true) },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(BorderMoss)
                            .testTag("add_task_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = VibrantEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Weekly Sprints Productivity Meter Banner
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = VibrantEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "WEEKLY SPRINTS",
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "$completedTasksCount of $totalTasksCount Submissions In",
                            color = CrispWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Keep current pace to protect your 3.9 GPA",
                            color = LightSage,
                            fontSize = 12.sp
                        )
                    }

                    CircularProgressMeter(
                        progress = progressRatio,
                        size = 56.dp
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Task List
            items(filteredTasks, key = { it.id }) { task ->
                TaskCardItem(
                    task = task,
                    onToggleComplete = { viewModel.toggleTask(task) },
                    onShareStory = { viewModel.shareTaskStory(task.title) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Add New Assignment Button
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setAddTaskDialogOpen(true) }
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Academic Task / Assignment",
                        color = CrispWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        } else {
            // ==================== TAB 2: RESOURCE VAULT VIEW ====================

            // Vault Search Bar
            item {
                OutlinedTextField(
                    value = vaultQuery,
                    onValueChange = { viewModel.setVaultSearchQuery(it) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = LightSage,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.showToast("Filtered by relevance") }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Tune",
                                tint = VibrantEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search class notes, PYQs, slides...",
                            color = LightSage,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vault_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibrantEmerald,
                        unfocusedBorderColor = BorderMoss,
                        focusedTextColor = CrispWhite,
                        unfocusedTextColor = CrispWhite,
                        focusedContainerColor = OpaqueMoss,
                        unfocusedContainerColor = OpaqueMoss
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Quick Stats & Repositories Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Class Repositories",
                        color = CrispWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Synced 2m ago",
                            color = SoftMint,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Grid of Class Folder Cards (2x2)
            item {
                val repos = viewModel.classRepositories
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ClassRepoCard(
                            repo = repos[0],
                            onClick = { viewModel.showToast("Opened Data Science (CS302 Core)") },
                            modifier = Modifier.weight(1f)
                        )
                        ClassRepoCard(
                            repo = repos[1],
                            onClick = { viewModel.showToast("Opened C Lab Repo (CS201 Core)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ClassRepoCard(
                            repo = repos[2],
                            onClick = { viewModel.showToast("Opened Eng. Math III (MATH240)") },
                            modifier = Modifier.weight(1f)
                        )
                        ClassRepoCard(
                            repo = repos[3],
                            onClick = { viewModel.showToast("Opened Algorithm Design (CS305)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Trending Files in Hive (Verified Notes) Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(VibrantEmerald)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Trending Files in Hive",
                            color = CrispWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Verified Notes",
                        color = LightSage,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Vault Material Items
            items(resources, key = { it.id }) { item ->
                ResourceItemCard(
                    item = item,
                    onUpvote = { viewModel.toggleUpvote(item) },
                    onDownload = { viewModel.downloadResource(item) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Upload Document CTA
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.openCreateModal("document") },
                    colors = ButtonDefaults.buttonColors(containerColor = OpaqueMoss),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BorderMoss)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Upload PDF or Slide Deck to Vault",
                        color = CrispWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(if (isSelected) VibrantEmerald else OpaqueMoss)
            .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, RoundedCornerShape(9999.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) DarkGreenText else LightSage,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun TaskCardItem(
    task: TaskEntity,
    onToggleComplete: () -> Unit,
    onShareStory: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) {
                // Custom Checkbox Button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (task.isCompleted) VibrantEmerald else BorderMoss)
                        .border(1.dp, if (task.isCompleted) VibrantEmerald else BorderMoss, RoundedCornerShape(6.dp))
                        .clickable { onToggleComplete() }
                        .testTag("task_checkbox_${task.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = DarkGreenText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = task.title,
                        color = if (task.isCompleted) LightSage else CrispWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BorderMoss)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.courseCode,
                                color = LightSage,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.DoneAll else if (task.isUrgent) Icons.Default.Schedule else Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = if (task.isCompleted) VibrantEmerald else if (task.isUrgent) SoftMint else LightSage,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = task.dueString,
                            color = LightSage,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Status Tag
            val tagText = when {
                task.isCompleted -> "Done"
                task.isUrgent -> "Urgent"
                else -> "Upcoming"
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(BorderMoss)
                    .border(1.dp, BorderMoss, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.isUrgent && !task.isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(VibrantEmerald)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = tagText,
                        color = SoftMint,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = { if (task.isCompleted) 1f else (task.progressPercent / 100f) },
            color = VibrantEmerald,
            trackColor = BorderMoss,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
        )

        // Celebratory Action Bar when task is completed
        AnimatedVisibility(
            visible = task.isCompleted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerHigh)
                        .border(1.dp, BorderMoss, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Submission Logged",
                            color = SoftMint,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(VibrantEmerald)
                            .clickable { onShareStory() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = DarkGreenText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Brag on Story",
                            color = DarkGreenText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassRepoCard(
    repo: ClassRepository,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BorderMoss),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (repo.iconName) {
                    "terminal" -> Icons.Default.Terminal
                    "calculate" -> Icons.Default.Calculate
                    "account_tree" -> Icons.Default.AccountTree
                    else -> Icons.Default.Folder
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VibrantEmerald,
                    modifier = Modifier.size(22.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Likes",
                    tint = SoftMint,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = repo.upvotes,
                    color = SoftMint,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = repo.title,
            color = CrispWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = repo.curatedFilesCount,
            color = LightSage,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = repo.courseCode,
                color = LightSage,
                fontSize = 10.sp
            )

            Text(
                text = "Open →",
                color = VibrantEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ResourceItemCard(
    item: ResourceEntity,
    onUpvote: () -> Unit,
    onDownload: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BorderMoss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.fileType == "code") Icons.Default.Code else Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = SoftMint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.title,
                    color = CrispWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.fileSize,
                        color = VibrantEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = " • ", color = LightSage, fontSize = 10.sp)
                    Text(
                        text = item.author,
                        color = LightSage,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action Buttons: Upvote & Download
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (item.isUpvoted) VibrantEmerald else BorderMoss)
                    .clickable { onUpvote() }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = "Upvote",
                    tint = if (item.isUpvoted) DarkGreenText else VibrantEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = item.upvotes.toString(),
                    color = if (item.isUpvoted) DarkGreenText else CrispWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(VibrantEmerald)
                    .clickable { onDownload() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = DarkGreenText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
