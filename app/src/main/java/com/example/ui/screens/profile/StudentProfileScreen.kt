package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.AcademicBadge
import com.example.data.model.PortfolioItem
import com.example.data.model.StudentProfile
import com.example.ui.components.EditProfileDialog
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.ProfileSubTab
import com.example.ui.viewmodel.StudyHiveViewModel

@Composable
fun StudentProfileScreen(
    viewModel: StudyHiveViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.studentProfile.collectAsState()
    val subTab by viewModel.profileSubTab.collectAsState()
    val portfolioItems = viewModel.portfolioItems
    val badges = viewModel.academicBadges
    val selectedItem by viewModel.selectedPortfolioItem.collectAsState()
    val showSettings by viewModel.showSettingsDrawer.collectAsState()
    val showEditProfile by viewModel.showEditProfileDialog.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPine),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Banner & Overlapping Avatar Header Container
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Background Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    AsyncImage(
                        model = profile.bannerUrl,
                        contentDescription = "Campus Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, OpaqueMoss.copy(alpha = 0.6f), DeepPine)
                                )
                            )
                    )

                    // Active on Campus Status Tag
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp, end = 16.dp)
                            .clip(CircleShape)
                            .background(OpaqueMoss)
                            .border(1.dp, BorderMoss, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(VibrantEmerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Active on Campus",
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Overlapping Avatar at Bottom Left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(VibrantEmerald)
                            .padding(3.dp)
                    ) {
                        AsyncImage(
                            model = profile.avatarUrl,
                            contentDescription = profile.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Verified Scholar Badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(VibrantEmerald)
                            .border(2.dp, DeepPine, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Scholar",
                            tint = CrispWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Action Buttons at Bottom Right
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.setEditProfileDialogOpen(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("profile_edit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = DarkGreenText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Edit Profile",
                            color = DarkGreenText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setSettingsDrawerOpen(true) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(OpaqueMoss)
                            .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
                            .testTag("profile_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = CrispWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Student Details & Bio
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Name & Pronouns & Role
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = profile.name,
                            color = CrispWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BorderMoss)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = profile.pronouns,
                                color = SoftMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(OpaqueMoss)
                            .border(1.dp, BorderMoss, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = profile.role,
                            color = SoftMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // College & Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = SoftMint,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = profile.institution,
                        color = SoftMint,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "${profile.classSection} • Roll: ${profile.rollNumber}",
                        color = CrispWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = " • ", color = LightSage, fontSize = 12.sp)
                    Text(
                        text = profile.department,
                        color = LightSage,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Academic Standing & GPA
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BorderMoss)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "GPA: ${profile.gpa} (Top 5% Scholar)",
                            color = VibrantEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = profile.semester,
                        color = LightSage,
                        fontSize = 11.sp
                    )
                }

                // Student Bio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = profile.bio,
                        color = LightSage,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }

                // Metric Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MetricBox(count = profile.postsCount.toString(), label = "POSTS", modifier = Modifier.weight(1f))
                    MetricBox(count = profile.connectedCount.toString(), label = "CONNECTED", modifier = Modifier.weight(1f))
                    MetricBox(count = profile.badgesCount.toString(), label = "BADGES", icon = Icons.Default.MilitaryTech, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Segmented Tabs: Portfolio Grid vs Academic Badges (8)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(OpaqueMoss)
                        .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    val isPortfolio = subTab == ProfileSubTab.PORTFOLIO
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isPortfolio) VibrantEmerald else OpaqueMoss)
                            .clickable { viewModel.setProfileSubTab(ProfileSubTab.PORTFOLIO) }
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = null,
                            tint = if (isPortfolio) DarkGreenText else LightSage,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Portfolio Grid",
                            color = if (isPortfolio) DarkGreenText else LightSage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val isBadges = subTab == ProfileSubTab.BADGES
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isBadges) VibrantEmerald else OpaqueMoss)
                            .clickable { viewModel.setProfileSubTab(ProfileSubTab.BADGES) }
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (isBadges) DarkGreenText else LightSage,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Academic Badges (${badges.size})",
                            color = if (isBadges) DarkGreenText else LightSage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // SubTab 1: Portfolio Grid
        if (subTab == ProfileSubTab.PORTFOLIO) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = VibrantEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "FEATURED ARTIFACTS & LAB CODE",
                                color = CrispWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${portfolioItems.size} SHOWCASED",
                            color = SoftMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3-Column Grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        portfolioItems.chunked(3).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { item ->
                                    PortfolioGridItem(
                                        item = item,
                                        onClick = { viewModel.selectPortfolioItem(item) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                for (i in 0 until (3 - rowItems.size)) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // SubTab 2: Badges Grid
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = VibrantEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CMRK VERIFIED HONORS",
                                color = CrispWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "LEVEL 4 SCHOLAR",
                            color = SoftMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        badges.chunked(2).forEach { rowBadges ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowBadges.forEach { badge ->
                                    BadgeCard(
                                        badge = badge,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowBadges.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Interactive Project Detail Modal
    selectedItem?.let { item ->
        ProjectDetailModal(
            item = item,
            onDismiss = { viewModel.selectPortfolioItem(null) },
            onViewDemo = { viewModel.showToast("Opening live repository for ${item.title}...") }
        )
    }

    // Edit Profile Info Dialog
    if (showEditProfile) {
        EditProfileDialog(
            isOpen = true,
            currentProfile = profile,
            onDismiss = { viewModel.setEditProfileDialogOpen(false) },
            onSave = { name, pronouns, bio, classSection, rollNumber, department, email ->
                viewModel.updateProfile(name, pronouns, bio, classSection, rollNumber, department, email)
            }
        )
    }

    // Hive Settings Drawer (Bottom Sheet)
    if (showSettings) {
        HiveSettingsBottomSheet(
            profile = profile,
            onDismiss = { viewModel.setSettingsDrawerOpen(false) },
            onOpenEditDialog = {
                viewModel.setSettingsDrawerOpen(false)
                viewModel.setEditProfileDialogOpen(true)
            },
            onUpdateProfile = { viewModel.updateProfileSetting(it) },
            onLogout = { viewModel.logOut() }
        )
    }
}

@Composable
private fun MetricBox(
    count: String,
    label: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DeepPine)
            .border(1.dp, BorderMoss, RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = count,
                color = CrispWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SoftMint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            text = label,
            color = LightSage,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun PortfolioGridItem(
    item: PortfolioItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent, DeepPine.copy(alpha = 0.85f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
        ) {
            Text(
                text = item.title,
                color = CrispWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = SoftMint,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = item.likesCount.toString(),
                    color = LightSage,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: AcademicBadge,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DeepPine)
                .border(1.dp, BorderMoss, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = badge.emoji, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = badge.title,
            color = CrispWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = badge.subtitle,
            color = LightSage,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { badge.progressPercent / 100f },
            color = VibrantEmerald,
            trackColor = DeepPine,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun ProjectDetailModal(
    item: PortfolioItem,
    onDismiss: () -> Unit,
    onViewDemo: () -> Unit
) {
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
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DeepPine)
                        .border(1.dp, BorderMoss, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = VibrantEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = LightSage)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = item.date, color = SoftMint, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = item.title, color = CrispWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = item.meta, color = LightSage, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = item.detailedSummary, color = CrispWhite, fontSize = 13.sp, lineHeight = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onViewDemo,
                colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = DarkGreenText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "View Code / Live Demo", color = DarkGreenText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HiveSettingsBottomSheet(
    profile: StudentProfile,
    onDismiss: () -> Unit,
    onOpenEditDialog: () -> Unit,
    onUpdateProfile: ((StudentProfile) -> StudentProfile) -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = OpaqueMoss,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(BorderMoss)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = VibrantEmerald, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Hive Settings", color = CrispWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = LightSage)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Student Account Section
            Text(text = "STUDENT IDENTITY & COHORT", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            ) {
                SettingsItemRow(
                    icon = Icons.Default.Badge,
                    title = "Edit Profile & Pronouns",
                    subtitle = "${profile.pronouns} • ${profile.classSection}",
                    tint = VibrantEmerald,
                    onClick = onOpenEditDialog
                )
                SettingsItemRow(
                    icon = Icons.Default.AlternateEmail,
                    title = "University Email",
                    subtitle = profile.email,
                    tint = SoftMint,
                    onClick = onOpenEditDialog
                )
                SettingsItemRow(
                    icon = Icons.Default.LockReset,
                    title = "Security & Campus Passkey",
                    subtitle = "Roll: ${profile.rollNumber} • 2FA Active",
                    tint = LightSage,
                    isLast = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Curriculum & Department Section
            Text(text = "CURRICULUM & DEPARTMENT", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            ) {
                SettingsItemRow(
                    icon = Icons.Default.SaveAs,
                    title = "Sync Timetable",
                    subtitle = "Auto-synced with CMRK SIS Portal",
                    tint = SoftMint
                )
                SettingsItemRow(
                    icon = Icons.Default.AccountTree,
                    title = "Degree & Department",
                    subtitle = profile.department,
                    tint = VibrantEmerald,
                    isLast = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Privacy & Visibility Section
            Text(text = "PRIVACY & VISIBILITY", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            ) {
                SettingsSwitchRow(
                    title = "Private Profile",
                    subtitle = "Only approved CMRK classmates can view assignments",
                    checked = profile.isPrivate,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(isPrivate = it) } }
                )
                SettingsSwitchRow(
                    title = "Show Online Status",
                    subtitle = "Allows study group members to spot you in Library",
                    checked = profile.showOnline,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(showOnline = it) } }
                )
                SettingsSwitchRow(
                    title = "Allow Non-Classmate DMs",
                    subtitle = "Receive messages from inter-college hackathon peers",
                    checked = profile.allowNonClassmateDMs,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(allowNonClassmateDMs = it) } },
                    isLast = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Alerts & Notifications Section
            Text(text = "ALERTS & NOTIFICATIONS", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            ) {
                SettingsSwitchRow(
                    title = "Deadline Reminders",
                    subtitle = "Ping 1 hour before submission portal lock",
                    checked = profile.deadlineReminders,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(deadlineReminders = it) } }
                )
                SettingsSwitchRow(
                    title = "New Grade Posted",
                    subtitle = "Instant notification for lab & mid-term reviews",
                    checked = profile.newGradeAlerts,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(newGradeAlerts = it) } }
                )
                SettingsSwitchRow(
                    title = "Social Mentions",
                    subtitle = "When tagged in study hive circles or labs",
                    checked = profile.socialMentions,
                    onCheckedChange = { onUpdateProfile { p -> p.copy(socialMentions = it) } },
                    isLast = true
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Log Out Button
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = OpaqueMoss),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(BorderMoss)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_button")
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = VibrantEmerald, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Log Out of CMRK StudyHive", color = VibrantEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "StudyHive Mobile v3.5 (2026 Build) • CMRK Campus Node",
                color = LightSage,
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit = {},
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = CrispWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = LightSage, fontSize = 11.sp)
            }
        }
        if (!isLast) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderMoss))
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.78f)) {
                Text(text = title, color = CrispWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = LightSage, fontSize = 11.sp, lineHeight = 15.sp)
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DeepPine,
                    checkedTrackColor = VibrantEmerald,
                    uncheckedThumbColor = LightSage,
                    uncheckedTrackColor = OpaqueMoss
                )
            )
        }
        if (!isLast) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderMoss))
        }
    }
}
