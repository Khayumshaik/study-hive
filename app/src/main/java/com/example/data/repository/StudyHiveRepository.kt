package com.example.data.repository

import android.content.Context
import com.example.data.local.StudyHiveDao
import com.example.data.local.StudyHiveDatabase
import com.example.data.model.AcademicBadge
import com.example.data.model.ClassRepository
import com.example.data.model.Classmate
import com.example.data.model.MessageEntity
import com.example.data.model.PortfolioItem
import com.example.data.model.PostEntity
import com.example.data.model.ResourceEntity
import com.example.data.model.StudentProfile
import com.example.data.model.StudyGroup
import com.example.data.model.StudyHiveAssets
import com.example.data.model.StudyInvite
import com.example.data.model.TaskEntity
import com.example.data.remote.HiveAiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyHiveRepository(
    private val dao: StudyHiveDao,
    private val aiService: HiveAiService = HiveAiService()
) {
    // Confetti Event Stream for celebrations
    private val _confettiTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val confettiTrigger: SharedFlow<Unit> = _confettiTrigger.asSharedFlow()

    // Toast Message Stream
    private val _toastEvent = MutableSharedFlow<String>(extraBufferCapacity = 3)
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Student Profile State
    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    // Classmate Directory (Discover peers by Section & Department)
    private val _classmates = MutableStateFlow(
        listOf(
            Classmate(
                id = "sophia",
                name = "Sophia Lin",
                pronouns = "she/her",
                handle = "@sophia_c",
                avatarUrl = StudyHiveAssets.SOPHIA_AVATAR,
                department = "Computer Science & Data Science",
                classSection = "Section CSD-A",
                rollNumber = "24CSD012",
                isOnline = true,
                mutualCourses = "CS302, MATH240, CS201",
                isConnected = true,
                bio = "CSD Lab Lead • Exploring Vision-Language Models & Graph Neural Nets."
            ),
            Classmate(
                id = "arjun",
                name = "Arjun Singh",
                pronouns = "he/him",
                handle = "@arjun_s",
                avatarUrl = StudyHiveAssets.ARJUN_AVATAR,
                department = "Computer Science & Data Science",
                classSection = "Section CSD-A",
                rollNumber = "24CSD045",
                isOnline = true,
                mutualCourses = "CS302, CS201",
                isConnected = true,
                bio = "Systems engineering enthusiast • Audio DSP & Linux kernel geek."
            ),
            Classmate(
                id = "david",
                name = "David Kim",
                pronouns = "he/him",
                handle = "@david_k",
                avatarUrl = StudyHiveAssets.DAVID_AVATAR,
                department = "Mathematics & Computing",
                classSection = "Section MC-B",
                rollNumber = "24MC023",
                isOnline = false,
                mutualCourses = "MATH240 Linear Algebra",
                isConnected = true,
                bio = "Competitive math & probability puzzles • Study partner for MATH240."
            ),
            Classmate(
                id = "chloe",
                name = "Chloe Vance",
                pronouns = "she/her",
                handle = "@chloe_v",
                avatarUrl = StudyHiveAssets.CHLOE_AVATAR,
                department = "Computer Science & Data Science",
                classSection = "Section CSD-B",
                rollNumber = "24CSD061",
                isOnline = true,
                mutualCourses = "CS302, CS201",
                isConnected = false,
                bio = "Fullstack developer & UI designer • Always down to compare recursion trees!"
            ),
            Classmate(
                id = "liam",
                name = "Liam Ortiz",
                pronouns = "he/him",
                handle = "@liam_o",
                avatarUrl = StudyHiveAssets.LIAM_AVATAR,
                department = "Computer Science & Software Eng",
                classSection = "Section SE-A",
                rollNumber = "24SE039",
                isOnline = true,
                mutualCourses = "CS305 Databases",
                isConnected = false,
                bio = "Backend specialist • Hackathon sprint runner • Building Go microservices."
            ),
            Classmate(
                id = "emily",
                name = "Emily Zhang",
                pronouns = "she/they",
                handle = "@emily_z",
                avatarUrl = StudyHiveAssets.EMILY_AVATAR,
                department = "Artificial Intelligence & Robotics",
                classSection = "Section AIR-A",
                rollNumber = "24AIR018",
                isOnline = true,
                mutualCourses = "CS302, Robotics Lab",
                isConnected = false,
                bio = "Quadruped robotics & RL algorithms • CMRK Robotics team."
            ),
            Classmate(
                id = "rahul",
                name = "Rahul Verma",
                pronouns = "he/him",
                handle = "@rahul_v",
                avatarUrl = StudyHiveAssets.RAHUL_AVATAR,
                department = "Computer Science & Data Science",
                classSection = "Section CSD-A",
                rollNumber = "24CSD099",
                isOnline = false,
                mutualCourses = "CS302, MATH240, CS201",
                isConnected = false,
                bio = "Data pipelines and MLOps • Library room regular."
            )
        )
    )
    val classmates: StateFlow<List<Classmate>> = _classmates.asStateFlow()

    // Study Groups & Circles
    private val _studyGroups = MutableStateFlow(
        listOf(
            StudyGroup(
                id = "csd_batch",
                name = "CSD Batch 2030 Official",
                courseCode = "CSD Cohort",
                section = "Sections A & B",
                memberCount = 64,
                activeCount = 12,
                roomOrHall = "Lecture Hall A",
                lastMessage = "Professor updated midterm scope in portal!",
                lastMessageSender = "Marcus",
                lastMessageTime = "3m ago",
                isOfficial = true
            ),
            StudyGroup(
                id = "ds_lab",
                name = "Data Science Lab Team A",
                courseCode = "CS302",
                section = "Section CSD-A",
                memberCount = 4,
                activeCount = 4,
                roomOrHall = "Lab Station 3",
                lastMessage = "Focus Sprint #3 • Lab 4 PyTorch Model",
                lastMessageSender = "Team",
                lastMessageTime = "Running",
                hasActivePomodoro = true,
                isOfficial = false
            ),
            StudyGroup(
                id = "math_circle",
                name = "Math 240 Study Circle",
                courseCode = "MATH240",
                section = "All Sections",
                memberCount = 8,
                activeCount = 3,
                roomOrHall = "Room 304",
                lastMessage = "Eigenvalues_CheatSheet_v2.pdf added",
                lastMessageSender = "David",
                lastMessageTime = "1h ago",
                isOfficial = false
            )
        )
    )
    val studyGroups: StateFlow<List<StudyGroup>> = _studyGroups.asStateFlow()

    // Portfolio Items
    val portfolioItems = listOf(
        PortfolioItem(
            id = "hackfinix",
            title = "Hackfinix 1st Place",
            meta = "Grand Prize • Web3 AI Agent",
            date = "Nov 2024",
            likesCount = 142,
            imageUrl = StudyHiveAssets.PORTFOLIO_1,
            detailedSummary = "Autonomous multi-agent research assistant for academic papers. Won 1st place in National Hackfinix 2024."
        ),
        PortfolioItem(
            id = "ai_study_app",
            title = "AI Study App",
            meta = "LLM Flashcards & Memory RAG",
            date = "Oct 2024",
            likesCount = 98,
            imageUrl = StudyHiveAssets.PORTFOLIO_2,
            detailedSummary = "Personalized syllabus synthesizer utilizing vector search to index semester slide decks."
        ),
        PortfolioItem(
            id = "robotics_bot",
            title = "Robotics Bot",
            meta = "ROS2 Quadruped Obstacle Solver",
            date = "Aug 2024",
            likesCount = 84,
            imageUrl = StudyHiveAssets.PORTFOLIO_3,
            detailedSummary = "Custom quadruped rover built in CMRK Robotics Lab featuring LiDAR mapping and obstacle evasion."
        ),
        PortfolioItem(
            id = "shader_demo",
            title = "Shader Demo",
            meta = "GLSL Raymarching Fluid Sim",
            date = "July 2024",
            likesCount = 210,
            imageUrl = StudyHiveAssets.PORTFOLIO_4,
            detailedSummary = "Interactive real-time fluid refraction canvas with neon spectrum scattering."
        ),
        PortfolioItem(
            id = "data_vis",
            title = "Data Vis Dash",
            meta = "CMRK Campus Footprint Insights",
            date = "June 2024",
            likesCount = 67,
            imageUrl = StudyHiveAssets.PORTFOLIO_5,
            detailedSummary = "Real-time analytics portal tracking university lab usage and cafeteria peak occupancy."
        ),
        PortfolioItem(
            id = "notes_hub",
            title = "Notes Hub",
            meta = "Open Source Syllabus Synthesizer",
            date = "May 2024",
            likesCount = 119,
            imageUrl = StudyHiveAssets.PORTFOLIO_6,
            detailedSummary = "Markdown knowledge graph platform connecting 1200+ engineering students."
        )
    )

    // Academic Badges
    val academicBadges = listOf(
        AcademicBadge(
            id = "attendance",
            emoji = "🏆",
            title = "Top 5% Attendance",
            subtitle = "64 Consecutive Lectures Logged",
            progressPercent = 96
        ),
        AcademicBadge(
            id = "hackathon",
            emoji = "⚡",
            title = "Hackathon Finalist",
            subtitle = "1st Place @ Hackfinix 2024",
            progressPercent = 100
        ),
        AcademicBadge(
            id = "lab_ace",
            emoji = "🧪",
            title = "Lab Ace",
            subtitle = "100% Practical Verification",
            progressPercent = 100
        ),
        AcademicBadge(
            id = "mentor",
            emoji = "🌟",
            title = "Peer Mentor",
            subtitle = "Assisted 42 Junior Students",
            progressPercent = 82
        ),
        AcademicBadge(
            id = "library",
            emoji = "📚",
            title = "Library Scholar",
            subtitle = "120+ Study Hours in Hive",
            progressPercent = 88
        ),
        AcademicBadge(
            id = "streak",
            emoji = "🔥",
            title = "Perfect Streak",
            subtitle = "21 On-Time Deadlines Met",
            progressPercent = 100
        )
    )

    // Class Repositories for Vault
    val classRepositories = listOf(
        ClassRepository(
            id = "ds-folder",
            title = "Data Science",
            subtitle = "CS302 Core",
            curatedFilesCount = "24 curated files",
            upvotes = "1.2k",
            courseCode = "CS302 Core",
            iconName = "folder"
        ),
        ClassRepository(
            id = "c-folder",
            title = "C Lab Repo",
            subtitle = "CS201 Core",
            curatedFilesCount = "18 curated files",
            upvotes = "890",
            courseCode = "CS201 Core",
            iconName = "terminal"
        ),
        ClassRepository(
            id = "math-folder",
            title = "Eng. Math III",
            subtitle = "MATH240",
            curatedFilesCount = "32 curated files",
            upvotes = "2.4k",
            courseCode = "MATH240",
            iconName = "calculate"
        ),
        ClassRepository(
            id = "algo-folder",
            title = "Algorithm Design",
            subtitle = "CS305 Elective",
            curatedFilesCount = "15 curated files",
            upvotes = "650",
            courseCode = "CS305 Elective",
            iconName = "account_tree"
        )
    )

    // Study Invites in Requests Tab
    private val _studyInvites = MutableStateFlow(
        listOf(
            StudyInvite(
                id = "invite-1",
                studentName = "Chloe Vance",
                pronouns = "she/her",
                avatarUrl = StudyHiveAssets.CHLOE_AVATAR,
                timestamp = "Yesterday",
                mutualContext = "Mutual in CSD 101 & DS 202",
                message = "\"Hey Alex! Would love to compare notes on the recursion lab!\"",
                isTeamInvite = false
            ),
            StudyInvite(
                id = "invite-2",
                studentName = "Liam Ortiz",
                pronouns = "he/him",
                avatarUrl = StudyHiveAssets.LIAM_AVATAR,
                timestamp = "2d ago",
                mutualContext = "Invited you to 'Systems Hackathon Team'",
                message = "\"We need a backend specialist for next weekend's sprint.\"",
                isTeamInvite = true
            )
        )
    )
    val studyInvites: StateFlow<List<StudyInvite>> = _studyInvites.asStateFlow()

    // Tasks Flow
    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()

    // Resources Flow
    val allResources: Flow<List<ResourceEntity>> = dao.getAllResources()

    // Posts Flow
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()

    // Messages Flow
    fun getMessages(threadId: String): Flow<List<MessageEntity>> = dao.getMessagesForThread(threadId)

    // Task Actions
    suspend fun toggleTaskComplete(task: TaskEntity) {
        val newCompleted = !task.isCompleted
        val newCategory = if (newCompleted) "completed" else "upcoming"
        val completedAt = if (newCompleted) System.currentTimeMillis() else 0L

        dao.setTaskCompletion(task.id, newCompleted, newCategory, completedAt)

        if (newCompleted) {
            _confettiTrigger.emit(Unit)
            _toastEvent.emit("Completed: ${task.title} (+50 XP)")
        } else {
            _toastEvent.emit("Marked as pending: ${task.title}")
        }
    }

    suspend fun addNewTask(title: String, courseCode: String, dueDate: String, isUrgent: Boolean) {
        val task = TaskEntity(
            title = title,
            courseCode = courseCode,
            dueString = dueDate,
            isCompleted = false,
            isUrgent = isUrgent,
            category = if (isUrgent) "overdue" else "upcoming",
            progressPercent = 10,
            xpGained = 50
        )
        dao.insertTask(task)
        _toastEvent.emit("Task added to Deadlines!")
    }

    suspend fun deleteTask(taskId: Long) {
        dao.deleteTask(taskId)
        _toastEvent.emit("Task removed")
    }

    suspend fun shareTaskStory(taskTitle: String) {
        val post = PostEntity(
            authorName = _studentProfile.value.name,
            authorHandle = _studentProfile.value.handle,
            authorAvatar = _studentProfile.value.avatarUrl,
            title = "Finished: $taskTitle",
            content = "Crushed my deadline for $taskTitle! +50 XP unlocked in StudyHive 🚀",
            postType = "story",
            timestamp = "Just now",
            likesCount = 1,
            isLiked = true,
            tags = "#StudyGrind #HiveGoal"
        )
        dao.insertPost(post)
        _confettiTrigger.emit(Unit)
        _toastEvent.emit("Shared \"$taskTitle\" to Campus Story!")
    }

    // Resource Vault Actions
    suspend fun toggleUpvote(resource: ResourceEntity) {
        val newUpvoted = !resource.isUpvoted
        val newCount = if (newUpvoted) resource.upvotes + 1 else (resource.upvotes - 1).coerceAtLeast(0)
        dao.updateUpvote(resource.id, newUpvoted, newCount)
        if (newUpvoted) {
            _toastEvent.emit("Upvoted ${resource.title}!")
        }
    }

    suspend fun simulateDownload(resource: ResourceEntity) {
        dao.setDownloaded(resource.id)
        _toastEvent.emit("Downloading ${resource.title}...")
    }

    suspend fun uploadResource(title: String, courseCode: String, folderName: String, fileType: String = "pdf") {
        val entity = ResourceEntity(
            title = title,
            courseCode = courseCode,
            folderName = folderName,
            fileSize = "3.8 MB",
            author = _studentProfile.value.name,
            upvotes = 1,
            isUpvoted = true,
            fileType = fileType
        )
        dao.insertResource(entity)

        val post = PostEntity(
            authorName = _studentProfile.value.name,
            authorHandle = _studentProfile.value.handle,
            authorAvatar = _studentProfile.value.avatarUrl,
            title = "Uploaded: $title",
            content = "New course material uploaded to Resource Vault for $courseCode.",
            postType = "document",
            timestamp = "Just now",
            documentTitle = title,
            tags = "#VaultUpload #$courseCode"
        )
        dao.insertPost(post)
        _confettiTrigger.emit(Unit)
        _toastEvent.emit("Document uploaded to Vault & Feed! 🎉")
    }

    // Post Actions
    suspend fun togglePostLike(post: PostEntity) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        dao.updatePostLike(post.id, newLiked, newCount)
    }

    suspend fun publishCampusPost(
        postType: String,
        title: String,
        content: String,
        tags: String,
        imageUrl: String? = null,
        documentTitle: String? = null
    ) {
        val post = PostEntity(
            authorName = _studentProfile.value.name,
            authorHandle = _studentProfile.value.handle,
            authorAvatar = _studentProfile.value.avatarUrl,
            title = title,
            content = content,
            postType = postType,
            timestamp = "Just now",
            likesCount = 0,
            imageUrl = imageUrl,
            documentTitle = documentTitle,
            tags = tags
        )
        dao.insertPost(post)
        _confettiTrigger.emit(Unit)
        val successMsg = when (postType) {
            "document" -> "Document uploaded to Vault & Feed! 🎉"
            "question" -> "Question posted to Study Circle! 💡"
            else -> "Post published to Campus Feed! 🎉"
        }
        _toastEvent.emit(successMsg)
    }

    // Chat Actions
    suspend fun sendPeerMessage(threadId: String, text: String) {
        val msg = MessageEntity(
            threadId = threadId,
            senderName = _studentProfile.value.name,
            senderAvatar = _studentProfile.value.avatarUrl,
            text = text,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
            isFromUser = true
        )
        dao.insertMessage(msg)
    }

    suspend fun sendAiTutorQuery(query: String) {
        val userMsg = MessageEntity(
            threadId = "ai_tutor",
            senderName = _studentProfile.value.name,
            senderAvatar = _studentProfile.value.avatarUrl,
            text = query,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
            isFromUser = true
        )
        dao.insertMessage(userMsg)

        val aiReplyText = aiService.askTutor(query)
        val aiMsg = MessageEntity(
            threadId = "ai_tutor",
            senderName = "Hive AI Tutor",
            senderAvatar = "",
            text = aiReplyText,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
            isFromUser = false
        )
        dao.insertMessage(aiMsg)
    }

    // Classmate Directory Actions
    fun toggleConnectClassmate(classmateId: String) {
        _classmates.value = _classmates.value.map { peer ->
            if (peer.id == classmateId) {
                val newStatus = !peer.isConnected
                CoroutineScope(Dispatchers.Main).launch {
                    val msg = if (newStatus) "Connected with ${peer.name}! Study Circle established." else "Disconnected from ${peer.name}"
                    _toastEvent.emit(msg)
                }
                peer.copy(isConnected = newStatus)
            } else {
                peer
            }
        }
    }

    fun createStudyGroup(name: String, courseCode: String, section: String, room: String) {
        val newGroup = StudyGroup(
            id = "group_${System.currentTimeMillis()}",
            name = name,
            courseCode = courseCode,
            section = section,
            memberCount = 1,
            activeCount = 1,
            roomOrHall = room,
            lastMessage = "Group created by ${_studentProfile.value.name}",
            lastMessageSender = _studentProfile.value.name,
            lastMessageTime = "Just now",
            hasActivePomodoro = false,
            isOfficial = false
        )
        _studyGroups.value = listOf(newGroup) + _studyGroups.value
        CoroutineScope(Dispatchers.Main).launch {
            _confettiTrigger.emit(Unit)
            _toastEvent.emit("Study Circle \"$name\" created!")
        }
    }

    // Study Request Actions
    fun handleStudyInvite(inviteId: String, accepted: Boolean) {
        val invite = _studyInvites.value.find { it.id == inviteId }
        _studyInvites.value = _studyInvites.value.filterNot { it.id == inviteId }

        if (accepted && invite != null) {
            // Also mark as connected classmate
            _classmates.value = _classmates.value.map {
                if (it.name == invite.studentName) it.copy(isConnected = true) else it
            }
        }

        val msg = if (accepted) "Invitation Accepted! Connected with ${invite?.studentName ?: "peer"}" else "Invitation Declined"
        CoroutineScope(Dispatchers.Main).launch {
            _toastEvent.emit(msg)
        }
    }

    // Profile Settings Actions
    fun updateProfile(updater: (StudentProfile) -> StudentProfile) {
        _studentProfile.value = updater(_studentProfile.value)
    }

    // Account Creation
    fun registerNewAccount(
        fullName: String,
        pronouns: String,
        email: String,
        rollNumber: String,
        department: String,
        section: String,
        bio: String
    ) {
        _studentProfile.value = StudentProfile(
            name = fullName,
            pronouns = pronouns,
            email = email,
            rollNumber = rollNumber,
            department = department,
            classSection = section,
            majorYear = "$department • $section",
            bio = bio.ifBlank { "Student scholar at CMRK Institute of Technology." },
            avatarUrl = StudyHiveAssets.ALEX_AVATAR
        )
        CoroutineScope(Dispatchers.Main).launch {
            _confettiTrigger.emit(Unit)
            _toastEvent.emit("Welcome to StudyHive, $fullName! Profile created.")
        }
    }

    suspend fun emitToast(msg: String) {
        _toastEvent.emit(msg)
    }

    companion object {
        @Volatile
        private var INSTANCE: StudyHiveRepository? = null

        fun getInstance(context: Context): StudyHiveRepository {
            return INSTANCE ?: synchronized(this) {
                val db = StudyHiveDatabase.getInstance(context)
                val instance = StudyHiveRepository(db.dao())
                INSTANCE = instance
                instance
            }
        }
    }
}
