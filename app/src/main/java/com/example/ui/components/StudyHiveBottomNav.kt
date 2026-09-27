package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.MainNavigationTab

@Composable
fun StudyHiveBottomNav(
    currentTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit,
    onFabClick: () -> Unit,
    unreadChatCount: Int = 4,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(OpaqueMoss)
            .border(width = 1.dp, color = BorderMoss)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home Feed
            NavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentTab == MainNavigationTab.HOME,
                onClick = { onTabSelected(MainNavigationTab.HOME) },
                testTag = "nav_item_home",
                modifier = Modifier.weight(1f)
            )

            // 2. Academic Tracker
            NavItem(
                icon = Icons.Default.EventNote,
                label = "Tracker",
                isSelected = currentTab == MainNavigationTab.TRACKER,
                onClick = { onTabSelected(MainNavigationTab.TRACKER) },
                testTag = "nav_item_tracker",
                modifier = Modifier.weight(1f)
            )

            // 3. Central '+' Create Button (Integrated within dock touch bounds - No clipping overlay)
            Box(
                modifier = Modifier
                    .weight(1.1f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                        .border(width = 2.dp, color = BorderMoss, shape = CircleShape)
                        .clickable { onFabClick() }
                        .testTag("nav_central_fab"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Post or Assignment",
                        tint = DarkGreenText,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // 4. Study Circles Chat
            NavItem(
                icon = Icons.Default.ChatBubble,
                label = "Chat",
                isSelected = currentTab == MainNavigationTab.CHAT,
                onClick = { onTabSelected(MainNavigationTab.CHAT) },
                badgeCount = unreadChatCount,
                testTag = "nav_item_chat",
                modifier = Modifier.weight(1f)
            )

            // 5. Student Profile
            NavItem(
                icon = Icons.Default.AccountCircle,
                label = "Profile",
                isSelected = currentTab == MainNavigationTab.PROFILE,
                onClick = { onTabSelected(MainNavigationTab.PROFILE) },
                testTag = "nav_item_profile",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int? = null,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val tint = if (isSelected) VibrantEmerald else LightSage
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )

            if (badgeCount != null && badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                        .border(1.dp, DeepPine, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = DarkGreenText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = label,
            color = tint,
            fontSize = 11.sp,
            fontWeight = fontWeight,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
