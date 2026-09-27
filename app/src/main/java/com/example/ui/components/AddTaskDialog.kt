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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.VibrantEmerald

@Composable
fun AddTaskDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onAddTask: (title: String, courseCode: String, dueDate: String, isUrgent: Boolean) -> Unit
) {
    if (!isOpen) return

    var taskTitle by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("CS302") }
    var dueDate by remember { mutableStateOf("Tonight, 11:59 PM") }
    var isUrgent by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
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
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "New Academic Task",
                        color = CrispWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Assignment or Lab Title *", color = LightSage, fontSize = 12.sp) },
                placeholder = { Text("e.g. Distributed Consensus Lab 5", color = LightSage.copy(alpha = 0.5f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_task_title_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Course / Subject:", color = LightSage, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("CS302", "CS201", "MATH240", "CS305").forEach { code ->
                    val isSelected = courseCode == code
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) VibrantEmerald else DeepPine)
                            .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, RoundedCornerShape(8.dp))
                            .clickable { courseCode = code }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = code,
                            color = if (isSelected) DarkGreenText else LightSage,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Due Date / Time", color = LightSage, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Mark as Urgent", color = CrispWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Highlights card and shows pulsating badge", color = LightSage, fontSize = 11.sp)
                }

                Switch(
                    checked = isUrgent,
                    onCheckedChange = { isUrgent = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DarkGreenText,
                        checkedTrackColor = VibrantEmerald,
                        uncheckedThumbColor = LightSage,
                        uncheckedTrackColor = DeepPine
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onAddTask(taskTitle, courseCode, dueDate, isUrgent)
                    }
                },
                enabled = taskTitle.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VibrantEmerald,
                    disabledContainerColor = BorderMoss
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("add_task_submit_button")
            ) {
                Text(
                    text = "Add to Tracker (+50 XP)",
                    color = if (taskTitle.isNotBlank()) DarkGreenText else LightSage,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
