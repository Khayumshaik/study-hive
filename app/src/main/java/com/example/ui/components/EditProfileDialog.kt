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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.StudentProfile
import com.example.ui.theme.BorderMoss
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.DarkGreenText
import com.example.ui.theme.DeepPine
import com.example.ui.theme.LightSage
import com.example.ui.theme.OpaqueMoss
import com.example.ui.theme.SoftMint
import com.example.ui.theme.VibrantEmerald

@Composable
fun EditProfileDialog(
    isOpen: Boolean,
    currentProfile: StudentProfile,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        pronouns: String,
        bio: String,
        classSection: String,
        rollNumber: String,
        department: String,
        email: String
    ) -> Unit
) {
    if (!isOpen) return

    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf(currentProfile.name) }
    var pronouns by remember { mutableStateOf(currentProfile.pronouns) }
    var bio by remember { mutableStateOf(currentProfile.bio) }
    var classSection by remember { mutableStateOf(currentProfile.classSection) }
    var rollNumber by remember { mutableStateOf(currentProfile.rollNumber) }
    var department by remember { mutableStateOf(currentProfile.department) }
    var email by remember { mutableStateOf(currentProfile.email) }

    val pronounOptions = listOf("he/him", "she/her", "they/them", "she/they", "he/they")

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(OpaqueMoss)
                .border(1.dp, BorderMoss, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .verticalScroll(scrollState)
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
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Edit Student Profile",
                        color = CrispWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LightSage
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full Name
            Text(text = "FULL NAME", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            // Pronouns Selection
            Text(text = "PRONOUNS", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .androidx.compose.foundation.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                pronounOptions.forEach { p ->
                    val isSelected = pronouns.equals(p, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) VibrantEmerald else DeepPine)
                            .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, CircleShape)
                            .clickable { pronouns = p }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = p,
                            color = if (isSelected) DarkGreenText else LightSage,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student Bio
            Text(text = "STUDENT BIO", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                placeholder = { Text("What are you building or studying?", color = LightSage.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
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

            Spacer(modifier = Modifier.height(12.dp))

            // Class Section & Roll Number
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "CLASS SECTION", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = classSection,
                        onValueChange = { classSection = it },
                        modifier = Modifier.fillMaxWidth(),
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
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "ROLL NUMBER", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        modifier = Modifier.fillMaxWidth(),
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
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Department
            Text(text = "DEPARTMENT / MAJOR", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            // University Email
            Text(text = "CAMPUS EMAIL", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(18.dp))

            // Save Button
            Button(
                onClick = {
                    onSave(name, pronouns, bio, classSection, rollNumber, department, email)
                },
                colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_profile_button")
            ) {
                Text(
                    text = "Save Profile Changes",
                    color = DarkGreenText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
