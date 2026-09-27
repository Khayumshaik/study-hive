package com.example.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

enum class AuthMode {
    SIGN_IN,
    CREATE_ACCOUNT
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: (
        name: String,
        pronouns: String,
        email: String,
        rollNumber: String,
        department: String,
        section: String,
        bio: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val verticalScrollState = rememberScrollState()
    val pronounsScrollState = rememberScrollState()
    val sectionsScrollState = rememberScrollState()

    var authMode by remember { mutableStateOf(AuthMode.SIGN_IN) }

    // Sign In Fields
    var loginEmail by remember { mutableStateOf("alex.chen@cmrk.edu") }
    var loginPassword by remember { mutableStateOf("CyberStudent2026!") }
    var isLoginPasswordVisible by remember { mutableStateOf(false) }

    // Register Fields
    var regFullName by remember { mutableStateOf("") }
    var regPronouns by remember { mutableStateOf("he/him") }
    var regEmail by remember { mutableStateOf("") }
    var regRollNumber by remember { mutableStateOf("") }
    var regDepartment by remember { mutableStateOf("Computer Science & Data Science") }
    var regSection by remember { mutableStateOf("Section CSD-A") }
    var regPassword by remember { mutableStateOf("") }
    var regBio by remember { mutableStateOf("") }
    var isRegPasswordVisible by remember { mutableStateOf(false) }

    val pronounChoices = listOf("he/him", "she/her", "they/them", "she/they", "he/they")
    val departmentChoices = listOf(
        "Computer Science & Data Science",
        "Artificial Intelligence & Robotics",
        "Software Engineering",
        "Mathematics & Computing",
        "Electronics & Comm. Engineering"
    )
    val sectionChoices = listOf(
        "Section CSD-A",
        "Section CSD-B",
        "Section AIR-A",
        "Section SE-A",
        "Section MC-B"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPine)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Official Campus Emblem & Branding
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(OpaqueMoss)
                    .border(1.5.dp, BorderMoss, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalLibrary,
                    contentDescription = "StudyHive Emblem",
                    tint = VibrantEmerald,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CMRK CAMPUS PORTAL • 2026",
                    color = SoftMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "StudyHive",
                color = CrispWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Academic Tracker & Connected Student Network",
                color = LightSage,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Official Auth Mode Toggle: Sign In vs Create New Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                val isSignIn = authMode == AuthMode.SIGN_IN
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSignIn) VibrantEmerald else OpaqueMoss)
                        .clickable { authMode = AuthMode.SIGN_IN }
                        .padding(vertical = 12.dp)
                        .testTag("auth_tab_signin"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Student Sign In",
                        color = if (isSignIn) DarkGreenText else LightSage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val isCreate = authMode == AuthMode.CREATE_ACCOUNT
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isCreate) VibrantEmerald else OpaqueMoss)
                        .clickable { authMode = AuthMode.CREATE_ACCOUNT }
                        .padding(vertical = 12.dp)
                        .testTag("auth_tab_create"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create New Account",
                        color = if (isCreate) DarkGreenText else LightSage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Form Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(OpaqueMoss)
                    .border(1.dp, BorderMoss, RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                if (authMode == AuthMode.SIGN_IN) {
                    // ================= SIGN IN FORM =================
                    Text(text = "STUDENT EMAIL / CAMPUS ID", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.School, contentDescription = null, tint = LightSage, modifier = Modifier.size(20.dp))
                        },
                        placeholder = { Text("e.g. alex.chen@cmrk.edu", color = LightSage.copy(alpha = 0.5f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantEmerald,
                            unfocusedBorderColor = BorderMoss,
                            focusedTextColor = CrispWhite,
                            unfocusedTextColor = CrispWhite,
                            focusedContainerColor = DeepPine,
                            unfocusedContainerColor = DeepPine
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "PORTAL PASSWORD", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = LightSage, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            IconButton(onClick = { isLoginPasswordVisible = !isLoginPasswordVisible }) {
                                Icon(
                                    imageVector = if (isLoginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = LightSage,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (isLoginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibrantEmerald,
                            unfocusedBorderColor = BorderMoss,
                            focusedTextColor = CrispWhite,
                            unfocusedTextColor = CrispWhite,
                            focusedContainerColor = DeepPine,
                            unfocusedContainerColor = DeepPine
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onLoginSuccess,
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("enter_campus_btn")
                    ) {
                        Text(
                            text = "Sign In to Campus Hive",
                            color = DarkGreenText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fast Demo Access Button for convenience
                    Button(
                        onClick = onLoginSuccess,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPine),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = VibrantEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ Quick Demo Scholar Sign In",
                            color = CrispWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                } else {
                    // ================= CREATE NEW ACCOUNT FORM =================
                    Text(text = "FULL STUDENT NAME *", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regFullName,
                        onValueChange = { regFullName = it },
                        placeholder = { Text("e.g. Alex Chen or Priya Sharma", color = LightSage.copy(alpha = 0.5f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_fullname_input"),
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

                    // Pronouns Selection (smooth horizontal scroll)
                    Text(text = "PRONOUNS", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(pronounsScrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pronounChoices.forEach { choice ->
                            val isSelected = regPronouns == choice
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) VibrantEmerald else DeepPine)
                                    .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, CircleShape)
                                    .clickable { regPronouns = choice }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = choice,
                                    color = if (isSelected) DarkGreenText else LightSage,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Campus Email & Roll Number in a 2-column row
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(text = "STUDENT EMAIL *", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it },
                                placeholder = { Text("user@cmrk.edu", color = LightSage.copy(alpha = 0.5f)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_email_input"),
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

                        Column(modifier = Modifier.weight(0.8f)) {
                            Text(text = "ROLL NUMBER", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = regRollNumber,
                                onValueChange = { regRollNumber = it },
                                placeholder = { Text("24CSD084", color = LightSage.copy(alpha = 0.5f)) },
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

                    // Class Section Chips (smooth horizontal scroll)
                    Text(text = "CLASS SECTION", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(sectionsScrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sectionChoices.forEach { sec ->
                            val isSelected = regSection == sec
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) VibrantEmerald else DeepPine)
                                    .border(1.dp, if (isSelected) VibrantEmerald else BorderMoss, RoundedCornerShape(8.dp))
                                    .clickable { regSection = sec }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = sec,
                                    color = if (isSelected) DarkGreenText else LightSage,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Department / Major
                    Text(text = "DEPARTMENT / MAJOR", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regDepartment,
                        onValueChange = { regDepartment = it },
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

                    // Student Bio
                    Text(text = "STUDENT BIO & RESEARCH INTERESTS", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regBio,
                        onValueChange = { regBio = it },
                        placeholder = { Text("e.g. AI & fullstack developer • Lab lead • Building research projects 🚀", color = LightSage.copy(alpha = 0.5f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
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

                    // Password
                    Text(text = "SET PASSCODE *", color = LightSage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        trailingIcon = {
                            IconButton(onClick = { isRegPasswordVisible = !isRegPasswordVisible }) {
                                Icon(
                                    imageVector = if (isRegPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = LightSage,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_password_input"),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Register Button
                    Button(
                        onClick = {
                            val finalName = if (regFullName.isNotBlank()) regFullName else "Student Scholar"
                            val finalEmail = if (regEmail.isNotBlank()) regEmail else "scholar@cmrk.edu"
                            val finalRoll = if (regRollNumber.isNotBlank()) regRollNumber else "24CSD084"
                            val finalBio = if (regBio.isNotBlank()) regBio else "Student scholar at CMRK Institute of Technology."
                            onRegisterSuccess(finalName, regPronouns, finalEmail, finalRoll, regDepartment, regSection, finalBio)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("create_account_btn")
                    ) {
                        Text(
                            text = "Create Profile & Enter Campus",
                            color = DarkGreenText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clean Campus Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(VibrantEmerald)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "CMRK Institute of Technology • StudyHive Network 2026", color = LightSage, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
