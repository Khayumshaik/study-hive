package com.example.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Classmate
import com.example.data.model.MessageEntity
import com.example.data.model.StudyGroup
import com.example.data.model.StudyHiveAssets
import com.example.data.model.StudyInvite
import com.example.ui.components.ClassmateDirectoryDialog
import com.example.ui.components.CreateGroupDialog
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.ActivePeerChat
import com.example.ui.viewmodel.ChatSubTab
import com.example.ui.viewmodel.StudyHiveViewModel

@Composable
fun StudyCirclesChatScreen(
    viewModel: StudyHiveViewModel,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.chatSubTab.collectAsState()
    val searchQuery by viewModel.chatSearchQuery.collectAsState()
    val activePeerChat by viewModel.activePeerChat.collectAsState()
    val studyInvites by viewModel.studyInvites.collectAsState()
    val classmates by viewModel.classmates.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()
    val pomodoroSeconds by viewModel.pomodoroSecondsLeft.collectAsState()

    val showDirectory by viewModel.showClassmateDirectoryDialog.collectAsState()
    val showCreateGroup by viewModel.showCreateGroupDialog.collectAsState()

    val aiMessages by viewModel.repository.getMessages("ai_tutor").collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPine)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top Search Bar & Classmate Finder Shortcut
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setChatSearchQuery(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = LightSage,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = {
                    Text(
                        text = "Search classmates, groups, or ask AI...",
                        color = LightSage,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_search_bar"),
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

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { viewModel.setClassmateDirectoryOpen(true) },
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                    .testTag("open_directory_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Find Classmates",
                    tint = VibrantEmerald,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Pills Bar: Hive AI, Direct, Class Groups, Requests
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTabButton(
                label = "Hive AI",
                hasIcon = true,
                isSelected = subTab == ChatSubTab.HIVE_AI,
                onClick = { viewModel.setChatSubTab(ChatSubTab.HIVE_AI) },
                modifier = Modifier.weight(1.1f),
                testTag = "chat_tab_ai"
            )

            ChatTabButton(
                label = "Direct",
                hasDot = true,
                isSelected = subTab == ChatSubTab.DIRECT,
                onClick = { viewModel.setChatSubTab(ChatSubTab.DIRECT) },
                modifier = Modifier.weight(0.9f),
                testTag = "chat_tab_direct"
            )

            ChatTabButton(
                label = "Class Groups",
                isSelected = subTab == ChatSubTab.GROUPS,
                onClick = { viewModel.setChatSubTab(ChatSubTab.GROUPS) },
                modifier = Modifier.weight(1.2f),
                testTag = "chat_tab_groups"
            )

            ChatTabButton(
                label = "Requests",
                isSelected = subTab == ChatSubTab.REQUESTS,
                onClick = { viewModel.setChatSubTab(ChatSubTab.REQUESTS) },
                badgeCount = if (studyInvites.isNotEmpty()) studyInvites.size else null,
                modifier = Modifier.weight(1f),
                testTag = "chat_tab_requests"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Content Panels
        Box(modifier = Modifier.weight(1f)) {
            when (subTab) {
                ChatSubTab.HIVE_AI -> {
                    HiveAiTutorPanel(
                        messages = aiMessages,
                        onSendMessage = { viewModel.sendAiTutorMessage(it) }
                    )
                }

                ChatSubTab.DIRECT -> {
                    DirectMessagesPanel(
                        classmates = classmates,
                        searchQuery = searchQuery,
                        onOpenPeer = { id, name, sub, avatar, online ->
                            viewModel.openPeerChat(id, name, sub, avatar, online)
                        },
                        onOpenDirectory = { viewModel.setClassmateDirectoryOpen(true) },
                        onToggleConnect = { viewModel.toggleConnectClassmate(it) }
                    )
                }

                ChatSubTab.GROUPS -> {
                    ClassGroupsPanel(
                        groups = studyGroups,
                        pomodoroSeconds = pomodoroSeconds,
                        onOpenGroup = { id, name, sub ->
                            viewModel.openPeerChat(id, name, sub, StudyHiveAssets.CAMPUS_BANNER, true)
                        },
                        onStartGroup = { viewModel.setCreateGroupDialogOpen(true) }
                    )
                }

                ChatSubTab.REQUESTS -> {
                    RequestsPanel(
                        invites = studyInvites,
                        onAccept = { viewModel.handleStudyInvite(it, true) },
                        onDecline = { viewModel.handleStudyInvite(it, false) }
                    )
                }
            }
        }
    }

    // Classmate Directory Dialog
    if (showDirectory) {
        ClassmateDirectoryDialog(
            isOpen = true,
            classmates = classmates,
            onDismiss = { viewModel.setClassmateDirectoryOpen(false) },
            onToggleConnect = { viewModel.toggleConnectClassmate(it) },
            onStartDirectMessage = { peer ->
                viewModel.openPeerChat(peer.id, peer.name, "${peer.classSection} • ${if (peer.isOnline) "Active now" else "Offline"}", peer.avatarUrl, peer.isOnline)
            }
        )
    }

    // Create Study Circle Dialog
    if (showCreateGroup) {
        CreateGroupDialog(
            isOpen = true,
            onDismiss = { viewModel.setCreateGroupDialogOpen(false) },
            onCreateGroup = { name, courseCode, section, room ->
                viewModel.createStudyGroup(name, courseCode, section, room)
            }
        )
    }

    // Active Chat Thread Drawer (Bottom Sheet)
    activePeerChat?.let { peer ->
        ActiveChatBottomSheet(
            peer = peer,
            onDismiss = { viewModel.closePeerChat() },
            viewModel = viewModel
        )
    }
}

@Composable
private fun ChatTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    hasIcon: Boolean = false,
    hasDot: Boolean = false,
    badgeCount: Int? = null,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) VibrantEmerald else OpaqueMoss)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasIcon) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (isSelected) DarkGreenText else LightSage,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = label,
                color = if (isSelected) DarkGreenText else LightSage,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (hasDot) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) DarkGreenText else SoftMint)
                )
            }

            if (badgeCount != null && badgeCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) DeepPine else SoftMint)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = if (isSelected) SoftMint else DarkGreenText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun HiveAiTutorPanel(
    messages: List<MessageEntity>,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // Assistant Header Meta
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BorderMoss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = SoftMint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hive AI Tutor",
                            color = CrispWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BorderMoss)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "INDEXED CSD",
                                color = SoftMint,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Real-time Courseware Companion",
                        color = LightSage,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Options",
                    tint = LightSage
                )
            }
        }

        // Messages Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                if (msg.isFromUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(VibrantEmerald)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = CrispWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.92f),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(BorderMoss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = null,
                                tint = SoftMint,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(DeepPine)
                                .border(1.dp, BorderMoss, RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = msg.text,
                                    color = CrispWhite,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(SoftMint)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "CS101, DS202 Live • ${msg.timestamp}",
                                        color = LightSage,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Suggestion Prompt Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val chips = listOf(
                "📝 Summarize Lecture 6",
                "⚡ 3-Step Exam Plan",
                "🧪 Practice Quiz",
                "🔍 Debug C Code"
            )
            items(chips) { chip ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                        .clickable { onSendMessage(chip) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = chip,
                        color = CrispWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Composer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DeepPine)
                .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onSendMessage("Can you quiz me on binary search trees?") },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice",
                    tint = LightSage,
                    modifier = Modifier.size(20.dp)
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = "Ask Hive AI anything about your courses...",
                        color = LightSage,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_tutor_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                maxLines = 2
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val text = inputText
                        inputText = ""
                        onSendMessage(text)
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(VibrantEmerald)
                    .testTag("ai_tutor_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = CrispWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Insight badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = SoftMint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Weekly Hive Memory Synced",
                        color = CrispWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "42 PDF handouts • 6 Labs indexed",
                        color = LightSage,
                        fontSize = 10.sp
                    )
                }
            }

            Text(
                text = "98% ACCURACY",
                color = SoftMint,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DirectMessagesPanel(
    classmates: List<Classmate>,
    searchQuery: String,
    onOpenPeer: (id: String, name: String, sub: String, avatar: String, isOnline: Boolean) -> Unit,
    onOpenDirectory: () -> Unit,
    onToggleConnect: (String) -> Unit
) {
    val connectedList = remember(classmates, searchQuery) {
        if (searchQuery.isBlank()) {
            classmates.filter { it.isConnected }
        } else {
            classmates.filter {
                it.isConnected && (
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.classSection.contains(searchQuery, ignoreCase = true) ||
                    it.rollNumber.contains(searchQuery, ignoreCase = true)
                )
            }
        }
    }

    val discoverList = remember(classmates, searchQuery) {
        if (searchQuery.isBlank()) {
            classmates.filter { !it.isConnected }
        } else {
            classmates.filter {
                !it.isConnected && (
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.classSection.contains(searchQuery, ignoreCase = true) ||
                    it.rollNumber.contains(searchQuery, ignoreCase = true) ||
                    it.department.contains(searchQuery, ignoreCase = true)
                )
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Section 1: Connected Study Peers
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STUDY CIRCLE CONVERSATIONS",
                    color = LightSage,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${connectedList.size} Active",
                    color = SoftMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (connectedList.isEmpty() && searchQuery.isNotBlank()) {
            item {
                Text(
                    text = "No connected peers match \"$searchQuery\"",
                    color = LightSage,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        items(connectedList, key = { it.id }) { peer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
                    .clickable {
                        onOpenPeer(peer.id, peer.name, "${peer.classSection} • ${if (peer.isOnline) "Active now" else "Offline"}", peer.avatarUrl, peer.isOnline)
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    AsyncImage(
                        model = peer.avatarUrl,
                        contentDescription = peer.name,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (peer.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(SoftMint)
                                .border(2.dp, OpaqueMoss, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = peer.name,
                                color = CrispWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "(${peer.pronouns})",
                                color = SoftMint,
                                fontSize = 10.sp
                            )
                        }

                        Text(
                            text = if (peer.isOnline) "Active now" else "Offline",
                            color = if (peer.isOnline) SoftMint else LightSage,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "${peer.classSection} • ${peer.mutualCourses}",
                        color = LightSage,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Chat Action Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(BorderMoss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Message",
                        tint = VibrantEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Section 2: Discover Classmates to Connect & Chat With
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DISCOVER CLASSMATES IN YOUR SECTION",
                    color = LightSage,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${discoverList.size} Found",
                    color = SoftMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(discoverList, key = { "discover_${it.id}" }) { peer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    AsyncImage(
                        model = peer.avatarUrl,
                        contentDescription = peer.name,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (peer.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(SoftMint)
                                .border(1.5.dp, DeepPine, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = peer.name,
                            color = CrispWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${peer.pronouns})",
                            color = SoftMint,
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = "${peer.classSection} • ${peer.rollNumber}",
                        color = LightSage,
                        fontSize = 11.sp
                    )

                    Text(
                        text = peer.department,
                        color = LightSage.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Connect Button
                IconButton(
                    onClick = { onToggleConnect(peer.id) },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BorderMoss)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add to Circle",
                        tint = VibrantEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Message Button
                IconButton(
                    onClick = {
                        onOpenPeer(peer.id, peer.name, "${peer.classSection} • ${if (peer.isOnline) "Active now" else "Offline"}", peer.avatarUrl, peer.isOnline)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Message",
                        tint = DarkGreenText,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onOpenDirectory,
                colors = ButtonDefaults.buttonColors(containerColor = OpaqueMoss),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BorderMoss)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = SoftMint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open Full Official Campus Directory",
                    color = CrispWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ClassGroupsPanel(
    groups: List<StudyGroup>,
    pomodoroSeconds: Int,
    onOpenGroup: (id: String, name: String, sub: String) -> Unit,
    onStartGroup: () -> Unit
) {
    val minutes = pomodoroSeconds / 60
    val seconds = pomodoroSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Button(
                onClick = onStartGroup,
                colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.GroupAdd,
                    contentDescription = null,
                    tint = DarkGreenText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+ Start Official Study Circle",
                    color = DarkGreenText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYNCED CAMPUS COHORTS",
                    color = LightSage,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${groups.size} Active Circles",
                    color = SoftMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(groups, key = { it.id }) { group ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
                    .clickable {
                        onOpenGroup(group.id, group.name, "${group.section} • ${group.roomOrHall}")
                    }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BorderMoss),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = group.courseCode.take(4),
                                color = SoftMint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = group.name,
                                    color = CrispWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (group.isOfficial) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = SoftMint,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${group.section} • ${group.memberCount} members",
                                color = LightSage,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BorderMoss)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = group.roomOrHall,
                            color = SoftMint,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerHigh)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${group.lastMessageSender}: ${group.lastMessage}",
                            color = CrispWhite,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (group.hasActivePomodoro) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = SoftMint, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = timeFormatted, color = SoftMint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                text = group.lastMessageTime,
                                color = LightSage,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestsPanel(
    invites: List<StudyInvite>,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit
) {
    if (invites.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(16.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MarkChatRead,
                contentDescription = null,
                tint = LightSage,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Inbox Zero",
                color = CrispWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No pending invitations or peer requests right now.",
                color = LightSage,
                fontSize = 12.sp
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PENDING STUDY INVITES (${invites.size})",
                        color = LightSage,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Clear all",
                        color = SoftMint,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(invites, key = { it.id }) { invite ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = invite.avatarUrl,
                            contentDescription = invite.studentName,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = invite.studentName,
                                        color = CrispWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${invite.pronouns})",
                                        color = SoftMint,
                                        fontSize = 10.sp
                                    )
                                }

                                Text(
                                    text = invite.timestamp,
                                    color = LightSage,
                                    fontSize = 10.sp
                                )
                            }

                            Text(
                                text = invite.mutualContext,
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = invite.message,
                                color = LightSage,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onAccept(invite.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (invite.isTeamInvite) "Join Team" else "Accept",
                                color = DarkGreenText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { onDecline(invite.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderMoss),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Decline",
                                color = LightSage,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActiveChatBottomSheet(
    peer: ActivePeerChat,
    onDismiss: () -> Unit,
    viewModel: StudyHiveViewModel
) {
    val messages by viewModel.repository.getMessages(peer.threadId).collectAsState(initial = emptyList())
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = OpaqueMoss,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(BorderMoss)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CrispWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box {
                        AsyncImage(
                            model = peer.avatarUrl,
                            contentDescription = peer.name,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        if (peer.isOnline) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(SoftMint)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = peer.name,
                            color = CrispWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = peer.subtitle,
                            color = SoftMint,
                            fontSize = 11.sp
                        )
                    }
                }

                Row {
                    IconButton(onClick = { viewModel.showToast("Video call requested with ${peer.name}") }) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = LightSage)
                    }
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = LightSage)
                    }
                }
            }

            // Message Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(DeepPine)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(OpaqueMoss)
                                .border(1.dp, BorderMoss, RoundedCornerShape(9999.dp))
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Today, Campus Node 4",
                                color = LightSage,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                items(messages, key = { it.id }) { msg ->
                    if (msg.isFromUser) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 4.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                                    .background(VibrantEmerald)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    color = CrispWhite,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = msg.timestamp, color = SoftMint, fontSize = 9.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = null,
                                    tint = SoftMint,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                                    .background(OpaqueMoss)
                                    .border(1.dp, BorderMoss, RoundedCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    color = CrispWhite,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = msg.timestamp, color = LightSage, fontSize = 9.sp)
                        }
                    }
                }
            }

            // Input Composer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.showToast("Attachment picker") }, modifier = Modifier.size(34.dp)) {
                    Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Attach", tint = LightSage)
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Message ${peer.name.split(" ")[0]}...", color = LightSage, fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("peer_chat_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BorderMoss,
                        unfocusedBorderColor = BorderMoss,
                        focusedTextColor = CrispWhite,
                        unfocusedTextColor = CrispWhite,
                        focusedContainerColor = DeepPine,
                        unfocusedContainerColor = DeepPine
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val text = inputText
                            inputText = ""
                            viewModel.sendPeerMessage(peer.threadId, text)
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                        .testTag("peer_chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = DarkGreenText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
