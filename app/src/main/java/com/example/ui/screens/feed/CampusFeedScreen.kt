package com.example.ui.screens.feed

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PostEntity
import com.example.data.model.StudyHiveAssets
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.VibrantEmerald
import com.example.ui.viewmodel.StudyHiveViewModel

@Composable
fun CampusFeedScreen(
    viewModel: StudyHiveViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.allPosts.collectAsState()
    val profile by viewModel.studentProfile.collectAsState()
    var feedFilter by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPine)
            .padding(horizontal = 12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Campus Stories Row
        item {
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // User Story (Add)
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { viewModel.openCreateModal("photo") }
                    ) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, BorderMoss, CircleShape)
                                    .padding(2.dp)
                            ) {
                                AsyncImage(
                                    model = profile.avatarUrl,
                                    contentDescription = "Your Story",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(VibrantEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = DarkGreenText,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Your Story", color = LightSage, fontSize = 11.sp)
                    }
                }

                // Peers Stories
                val peers = listOf(
                    Pair("Sophia L.", StudyHiveAssets.SOPHIA_AVATAR),
                    Pair("Arjun S.", StudyHiveAssets.ARJUN_AVATAR),
                    Pair("Chloe V.", StudyHiveAssets.CHLOE_AVATAR),
                    Pair("Liam O.", StudyHiveAssets.LIAM_AVATAR),
                    Pair("David K.", StudyHiveAssets.DAVID_AVATAR)
                )

                items(peers) { (name, avatar) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { viewModel.showToast("Viewing $name's Story") }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(2.dp, VibrantEmerald, CircleShape)
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = avatar,
                                contentDescription = name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = name, color = CrispWhite, fontSize = 11.sp)
                    }
                }
            }
        }

        // Quick Post Prompt Card
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                    .clickable { viewModel.openCreateModal() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(DeepPine)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Share a project snap, lab note, or question...",
                        color = LightSage,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Feed Filter Pills
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf("All", "Projects", "Notes & Slides", "Questions", "Hackathons")
                items(filters) { filter ->
                    val isSelected = feedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) VibrantEmerald else OpaqueMoss)
                            .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, CircleShape)
                            .clickable { feedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) DarkGreenText else LightSage,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Posts Stream
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLike = { viewModel.togglePostLike(post) },
                onComment = { viewModel.showToast("Opening comments on ${post.title}...") },
                onShare = { viewModel.showToast("Shared post to Study Circle!") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PostCard(
    post: PostEntity,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(OpaqueMoss)
            .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.authorAvatar,
                    contentDescription = post.authorName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            color = CrispWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.authorHandle,
                            color = SoftMint,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = post.timestamp,
                        color = LightSage,
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(onClick = { }, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = LightSage
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title & Content
        Text(
            text = post.title,
            color = CrispWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = post.content,
            color = CrispWhite.copy(alpha = 0.9f),
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        // Photo Attachment if present
        post.imageUrl?.let { imgUrl ->
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = imgUrl,
                    contentDescription = "Post image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Document Attachment Card if present
        post.documentTitle?.let { docTitle ->
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = SoftMint,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = docTitle,
                        color = CrispWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Resource Vault • 4.2 MB",
                        color = LightSage,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Tags
        if (post.tags.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.tags,
                color = SoftMint,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Interaction Bar: Like, Comment, Share
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onLike() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.isLiked) VibrantEmerald else LightSage,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = post.likesCount.toString(),
                    color = if (post.isLiked) VibrantEmerald else LightSage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onComment() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = LightSage,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Comment",
                    color = LightSage,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = LightSage,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
