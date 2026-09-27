package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.Classmate
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald

@Composable
fun ClassmateDirectoryDialog(
    isOpen: Boolean,
    classmates: List<Classmate>,
    onDismiss: () -> Unit,
    onToggleConnect: (String) -> Unit,
    onStartDirectMessage: (classmate: Classmate) -> Unit
) {
    if (!isOpen) return

    var searchQuery by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("All") }

    val sections = listOf("All", "Section CSD-A", "Section CSD-B", "Section AIR-A", "Section MC-B", "Section SE-A")

    val filteredList = remember(searchQuery, selectedSection, classmates) {
        classmates.filter { peer ->
            val matchesSearch = searchQuery.isBlank() ||
                peer.name.contains(searchQuery, ignoreCase = true) ||
                peer.rollNumber.contains(searchQuery, ignoreCase = true) ||
                peer.department.contains(searchQuery, ignoreCase = true)

            val matchesSection = selectedSection == "All" || peer.classSection.contains(selectedSection, ignoreCase = true)

            matchesSearch && matchesSection
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BorderMoss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Find Classmates",
                            color = CrispWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Official CMRK Campus Directory",
                            color = LightSage,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LightSage
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = LightSage,
                        modifier = Modifier.size(18.dp)
                    )
                },
                placeholder = { Text("Search by name, roll no, or major...", color = LightSage, fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite,
                    focusedContainerColor = DeepPine,
                    unfocusedContainerColor = DeepPine
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Section filter pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .androidx.compose.foundation.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sections.forEach { sec ->
                    val isSelected = selectedSection == sec
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) VibrantEmerald else DeepPine)
                            .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, CircleShape)
                            .clickable { selectedSection = sec }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = sec,
                            color = if (isSelected) DarkGreenText else LightSage,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Classmates List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No classmates found matching criteria",
                        color = LightSage,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { peer ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepPine)
                                .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
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
                                    text = peer.mutualCourses,
                                    color = SoftMint,
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Action buttons: Connect + Message
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onToggleConnect(peer.id) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (peer.isConnected) VibrantEmerald else BorderMoss)
                                ) {
                                    Icon(
                                        imageVector = if (peer.isConnected) Icons.Default.Check else Icons.Default.PersonAdd,
                                        contentDescription = "Connect",
                                        tint = if (peer.isConnected) DarkGreenText else SoftMint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                IconButton(
                                    onClick = {
                                        onStartDirectMessage(peer)
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(BorderMoss)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = "Message",
                                        tint = CrispWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
