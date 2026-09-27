package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.StudyHiveAssets
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.VibrantEmerald

@Composable
fun StudyHiveTopBar(
    subtitle: String,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(OpaqueMoss)
            .border(width = 1.dp, color = BorderMoss)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Subtitle
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onProfileClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepPine)
                        .border(1.dp, BorderMoss, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = StudyHiveAssets.LOGO,
                        contentDescription = "StudyHive Logo",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit,
                        onError = {
                            // Handled by icon fallback below if load fails
                        }
                    )
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "StudyHive",
                            color = VibrantEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = (-0.02).sp
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // CMRK Campus Badge
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BorderMoss)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(VibrantEmerald.copy(alpha = pulseAlpha))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CMRK",
                                color = SoftMint,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = subtitle,
                        color = LightSage,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Search Icon Button
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("topbar_search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = LightSage
                )
            }

            // Notification Button with live indicator
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("topbar_notification_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = LightSage
                    )
                }
                // Ping dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // User Profile Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(2.dp, VibrantEmerald, CircleShape)
                    .clickable { onProfileClick() }
                    .testTag("topbar_avatar_button")
            ) {
                AsyncImage(
                    model = StudyHiveAssets.ALEX_AVATAR,
                    contentDescription = "Alex Chen Profile",
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
