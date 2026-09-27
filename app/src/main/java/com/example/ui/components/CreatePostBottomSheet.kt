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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.StudyHiveAssets
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostBottomSheet(
    isOpen: Boolean,
    currentFormType: String?,
    studentProfile: com.example.data.model.StudentProfile = com.example.data.model.StudentProfile(),
    onDismiss: () -> Unit,
    onSelectType: (String) -> Unit,
    onBackToMenu: () -> Unit,
    onPublish: (postType: String, title: String, content: String, tags: String, imageUrl: String?, docTitle: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            if (currentFormType == null) {
                // View A: 4 Actions Grid
                CreateActionGrid(
                    onClose = onDismiss,
                    onSelect = onSelectType
                )
            } else {
                // View B: Post Composer Form
                CreateComposerForm(
                    formType = currentFormType,
                    studentProfile = studentProfile,
                    onBack = onBackToMenu,
                    onPublish = onPublish
                )
            }
        }
    }
}

@Composable
private fun CreateActionGrid(
    onClose: () -> Unit,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Create in StudyHive",
                        color = CrispWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Select what you'd like to share or track",
                        color = LightSage,
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(
                onClick = onClose,
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

        Spacer(modifier = Modifier.height(18.dp))

        // 2x2 Grid of Actions
        Row(modifier = Modifier.fillMaxWidth()) {
            CreateOptionCard(
                icon = Icons.Default.PhotoCamera,
                title = "Post a Photo",
                subtitle = "Share moments or project snaps to Campus Feed",
                onClick = { onSelect("photo") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CreateOptionCard(
                icon = Icons.Default.Description,
                title = "Upload Document",
                subtitle = "Upload notes, handouts or syllabus to Vault",
                onClick = { onSelect("document") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CreateOptionCard(
                icon = Icons.Default.EditNote,
                title = "General Post",
                subtitle = "Share a thought or campus shoutout",
                onClick = { onSelect("general") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CreateOptionCard(
                icon = Icons.Default.Help,
                title = "Ask a Question",
                subtitle = "Ask classmates or get peer tutoring",
                onClick = { onSelect("question") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "StudyHive Network • End-to-end Verified",
            color = LightSage,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun CreateOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHigh)
            .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(VibrantEmerald),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DarkGreenText,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            color = CrispWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            color = LightSage,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun CreateComposerForm(
    formType: String,
    studentProfile: com.example.data.model.StudentProfile,
    onBack: () -> Unit,
    onPublish: (postType: String, title: String, content: String, tags: String, imageUrl: String?, docTitle: String?) -> Unit
) {
    val scrollState = rememberScrollState()

    var postTitle by remember { mutableStateOf("") }
    var postCaption by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("CS302 Data Science") }
    var hasPhotoSelected by remember { mutableStateOf(formType == "photo") }
    var selectedDocName by remember { mutableStateOf(if (formType == "document") "CS302_Lecture_Cheatsheet.pdf" else "") }
    var selectedTags by remember { mutableStateOf("#CSD #StudyHive") }

    val formHeaderTitle = when (formType) {
        "photo" -> "New Photo Post"
        "document" -> "Upload Document"
        "general" -> "General Post"
        "question" -> "Ask a Question"
        else -> "New Post"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { onBack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LightSage,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Back",
                    color = LightSage,
                    fontSize = 14.sp
                )
            }

            Text(
                text = formHeaderTitle,
                color = CrispWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    val finalTitle = if (postTitle.isNotBlank()) postTitle else when (formType) {
                        "photo" -> "Campus Project Snap"
                        "document" -> selectedDocName.ifBlank { "Study Handout" }
                        "question" -> "Class Question"
                        else -> "Campus Update"
                    }
                    val finalImage = if (hasPhotoSelected) StudyHiveAssets.POST_SAMPLE_IMG else null
                    val finalDoc = if (formType == "document") selectedDocName else null
                    onPublish(formType, finalTitle, postCaption, selectedTags, finalImage, finalDoc)
                },
                colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("create_publish_button")
            ) {
                Text(
                    text = "Publish",
                    color = DarkGreenText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Author Preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderMoss, RoundedCornerShape(8.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, VibrantEmerald, CircleShape)
            ) {
                AsyncImage(
                    model = studentProfile.avatarUrl,
                    contentDescription = studentProfile.name,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = studentProfile.name,
                        color = CrispWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${studentProfile.classSection}",
                        color = LightSage,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(VibrantEmerald)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Posting to Campus Grid",
                        color = SoftMint,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section: Photo Picker
        if (formType == "photo") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, if (hasPhotoSelected) VibrantEmerald else BorderMoss, RoundedCornerShape(12.dp))
                    .clickable { hasPhotoSelected = !hasPhotoSelected },
                contentAlignment = Alignment.Center
            ) {
                if (hasPhotoSelected) {
                    AsyncImage(
                        model = StudyHiveAssets.POST_SAMPLE_IMG,
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DeepPine.copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Tap to change",
                            color = SoftMint,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Add Photo",
                            tint = VibrantEmerald,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap to select photo from campus gallery",
                            color = CrispWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "JPG, PNG or WEBP up to 10MB",
                            color = LightSage,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Section: Document Upload
        if (formType == "document") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepPine)
                    .border(1.dp, BorderMoss, RoundedCornerShape(12.dp))
                    .clickable {
                        selectedDocName = "CS302_Lecture_Cheatsheet.pdf"
                        if (postTitle.isBlank()) postTitle = "Data Science Lab 4 Cheatsheet"
                    }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = "Upload Doc",
                        tint = VibrantEmerald,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedDocName.isNotBlank()) "📄 $selectedDocName (4.2 MB)" else "Select PDF, DOCX or Code File",
                        color = if (selectedDocName.isNotBlank()) VibrantEmerald else CrispWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Verified uploads get stored directly in Resource Vault",
                        color = LightSage,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = postTitle,
                onValueChange = { postTitle = it },
                label = { Text("Document Title *", color = LightSage, fontSize = 12.sp) },
                placeholder = { Text("e.g. Data Science Lab 4 Cheatsheet", color = LightSage.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Section: Question
        if (formType == "question") {
            OutlinedTextField(
                value = postTitle,
                onValueChange = { postTitle = it },
                label = { Text("Question Title *", color = LightSage, fontSize = 12.sp) },
                placeholder = { Text("e.g. How do you calculate k-means centroid distance?", color = LightSage.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantEmerald,
                    unfocusedBorderColor = BorderMoss,
                    focusedTextColor = CrispWhite,
                    unfocusedTextColor = CrispWhite
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Quick Tag Suggestions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tags:", color = LightSage, fontSize = 11.sp)
            listOf("#Hackfinix", "#CSD", "#StudyGrind", "#ExamSprint").forEach { tag ->
                val isSelected = selectedTags.contains(tag)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) BorderMoss else DeepPine)
                        .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, CircleShape)
                        .clickable {
                            selectedTags = if (isSelected) {
                                selectedTags.replace(tag, "").trim()
                            } else {
                                "$selectedTags $tag".trim()
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = tag,
                        color = if (isSelected) SoftMint else LightSage,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Caption / Body Input with Character Counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (formType == "photo") "Caption" else "Details / Body",
                color = LightSage,
                fontSize = 12.sp
            )
            Text(
                text = "${postCaption.length}/280",
                color = LightSage,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = postCaption,
            onValueChange = { if (it.length <= 280) postCaption = it },
            placeholder = { Text("What's happening on campus? Provide context...", color = LightSage.copy(alpha = 0.6f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibrantEmerald,
                unfocusedBorderColor = BorderMoss,
                focusedTextColor = CrispWhite,
                unfocusedTextColor = CrispWhite
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Security / Verification Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = VibrantEmerald,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "End-to-End Encrypted",
                    color = SoftMint,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "StudyHive Node 4",
                color = LightSage,
                fontSize = 11.sp
            )
        }
    }
}
