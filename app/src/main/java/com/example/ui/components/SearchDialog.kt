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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ResourceEntity
import com.example.data.model.TaskEntity
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.VibrantEmerald

@Composable
fun SearchDialog(
    isOpen: Boolean,
    tasks: List<TaskEntity>,
    resources: List<ResourceEntity>,
    onDismiss: () -> Unit,
    onSelectTask: (TaskEntity) -> Unit,
    onSelectResource: (ResourceEntity) -> Unit
) {
    if (!isOpen) return

    var query by remember { mutableStateOf("") }

    val matchedTasks = remember(query, tasks) {
        if (query.isBlank()) emptyList() else tasks.filter {
            it.title.contains(query, ignoreCase = true) || it.courseCode.contains(query, ignoreCase = true)
        }
    }

    val matchedResources = remember(query, resources) {
        if (query.isBlank()) emptyList() else resources.filter {
            it.title.contains(query, ignoreCase = true) || it.folderName.contains(query, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Search StudyHive",
                    color = CrispWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LightSage
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = VibrantEmerald
                    )
                },
                placeholder = { Text("Search tasks, notes, labs, syllabus...", color = LightSage) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (query.isBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Type to search assignments, slide decks or peers",
                        color = LightSage,
                        fontSize = 12.sp
                    )
                }
            } else if (matchedTasks.isEmpty() && matchedResources.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No results found for \"$query\"",
                        color = LightSage,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (matchedTasks.isNotEmpty()) {
                        item {
                            Text(
                                text = "TASKS (${matchedTasks.size})",
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(matchedTasks) { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepPine)
                                    .border(1.dp, BorderMoss, RoundedCornerShape(8.dp))
                                    .clickable {
                                        onSelectTask(task)
                                        onDismiss()
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = VibrantEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = task.title,
                                        color = CrispWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${task.courseCode} • ${task.dueString}",
                                        color = LightSage,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    if (matchedResources.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "VAULT FILES (${matchedResources.size})",
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(matchedResources) { res ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepPine)
                                    .border(1.dp, BorderMoss, RoundedCornerShape(8.dp))
                                    .clickable {
                                        onSelectResource(res)
                                        onDismiss()
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = SoftMint,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = res.title,
                                        color = CrispWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${res.courseCode} • ${res.fileSize}",
                                        color = LightSage,
                                        fontSize = 11.sp
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
