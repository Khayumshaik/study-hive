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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald

data class HiveNotification(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val notifications = listOf(
        HiveNotification(
            id = "n1",
            icon = Icons.Default.Alarm,
            title = "Deadline in 3 hours",
            description = "Data Science Lab 4: Neural Nets is due tonight at 11:59 PM.",
            time = "10m ago",
            isUnread = true
        ),
        HiveNotification(
            id = "n2",
            icon = Icons.Default.Verified,
            title = "Task Completed • +50 XP",
            description = "Submission logged for Database Schema Design.",
            time = "Yesterday",
            isUnread = false
        ),
        HiveNotification(
            id = "n3",
            icon = Icons.Default.People,
            title = "Sophia Lin mentioned you",
            description = "\"Did you finish question 4 of the problem set?\"",
            time = "2:15 PM",
            isUnread = true
        ),
        HiveNotification(
            id = "n4",
            icon = Icons.Default.Grade,
            title = "New Lab Grade Posted",
            description = "C Lab: Pointers & Structs — Grade: 98/100 (A+)",
            time = "2 days ago",
            isUnread = false
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = OpaqueMoss,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 48.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(BorderMoss)
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Campus Notifications",
                        color = CrispWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BorderMoss)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LightSage,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                items(notifications, key = { it.id }) { notif ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (notif.isUnread) SurfaceContainerHigh else DeepPine)
                            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BorderMoss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = notif.icon,
                                contentDescription = null,
                                tint = SoftMint,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = notif.title,
                                    color = CrispWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = notif.time,
                                    color = LightSage,
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = notif.description,
                                color = LightSage,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }

                        if (notif.isUnread) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(VibrantEmerald)
                            )
                        }
                    }
                }
            }
        }
    }
}
