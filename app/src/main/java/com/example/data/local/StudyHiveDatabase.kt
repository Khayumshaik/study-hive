package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.MessageEntity
import com.example.data.model.PostEntity
import com.example.data.model.ResourceEntity
import com.example.data.model.StudyHiveAssets
import com.example.data.model.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TaskEntity::class,
        ResourceEntity::class,
        PostEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StudyHiveDatabase : RoomDatabase() {
    abstract fun dao(): StudyHiveDao

    companion object {
        @Volatile
        private var INSTANCE: StudyHiveDatabase? = null

        fun getInstance(context: Context): StudyHiveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyHiveDatabase::class.java,
                    "studyhive_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.dao()?.let { dao ->
                                    populateInitialData(dao)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(dao: StudyHiveDao) {
            // Initial Tasks
            dao.insertTasks(
                listOf(
                    TaskEntity(
                        title = "Data Science Lab 4: Neural Nets",
                        courseCode = "CS302",
                        dueString = "Tonight, 11:59 PM",
                        isCompleted = false,
                        isUrgent = true,
                        category = "overdue",
                        progressPercent = 85,
                        xpGained = 50
                    ),
                    TaskEntity(
                        title = "Engineering Math Problem Set 5",
                        courseCode = "MATH240",
                        dueString = "Tomorrow, 4:00 PM",
                        isCompleted = false,
                        isUrgent = false,
                        category = "upcoming",
                        progressPercent = 40,
                        xpGained = 50
                    ),
                    TaskEntity(
                        title = "C Lab: Dynamic Memory Allocation",
                        courseCode = "CS201",
                        dueString = "Friday, 2:30 PM",
                        isCompleted = false,
                        isUrgent = false,
                        category = "upcoming",
                        progressPercent = 15,
                        xpGained = 50
                    ),
                    TaskEntity(
                        title = "Database Schema Design",
                        courseCode = "CS305",
                        dueString = "Turned in yesterday",
                        isCompleted = true,
                        isUrgent = false,
                        category = "completed",
                        progressPercent = 100,
                        xpGained = 50,
                        completedAt = System.currentTimeMillis() - 86400000
                    ),
                    TaskEntity(
                        title = "Distributed Systems Sprint 2",
                        courseCode = "CS302",
                        dueString = "Next Monday, 6:00 PM",
                        isCompleted = false,
                        isUrgent = false,
                        category = "upcoming",
                        progressPercent = 30,
                        xpGained = 50
                    ),
                    TaskEntity(
                        title = "Linear Algebra Quiz Review",
                        courseCode = "MATH240",
                        dueString = "Next Tuesday, 10:00 AM",
                        isCompleted = false,
                        isUrgent = false,
                        category = "upcoming",
                        progressPercent = 20,
                        xpGained = 50
                    )
                )
            )

            // Initial Vault Resources
            dao.insertResources(
                listOf(
                    ResourceEntity(
                        title = "Lecture_08_Transformers.pdf",
                        courseCode = "CS302 Core",
                        folderName = "Data Science",
                        fileSize = "14 MB",
                        author = "Prof. Aris • ML Module",
                        upvotes = 142,
                        isUpvoted = false,
                        fileType = "pdf"
                    ),
                    ResourceEntity(
                        title = "2025_EndSem_PYQ_Solved.pdf",
                        courseCode = "MATH240",
                        folderName = "Eng. Math III",
                        fileSize = "8 MB",
                        author = "Solved by Toppers",
                        upvotes = 287,
                        isUpvoted = false,
                        fileType = "pdf"
                    ),
                    ResourceEntity(
                        title = "Lab_Pointers_CheatSheet.c",
                        courseCode = "CS201 Core",
                        folderName = "C Lab Repo",
                        fileSize = "240 KB",
                        author = "Malloc & Structs sample",
                        upvotes = 94,
                        isUpvoted = false,
                        fileType = "code"
                    ),
                    ResourceEntity(
                        title = "Graph_Algorithms_Visualized.pdf",
                        courseCode = "CS305 Elective",
                        folderName = "Algorithm Design",
                        fileSize = "11 MB",
                        author = "CSD Honor Guild",
                        upvotes = 156,
                        isUpvoted = false,
                        fileType = "pdf"
                    )
                )
            )

            // Initial Campus Posts
            dao.insertPosts(
                listOf(
                    PostEntity(
                        authorName = "Alex Chen",
                        authorHandle = "@Hackfinix",
                        authorAvatar = StudyHiveAssets.ALEX_AVATAR,
                        title = "Hackfinix 2024 1st Place Win!",
                        content = "Huge thank you to everyone who supported our NeuroSymbolic AI agent! Open sourced the code & model weights in Hive Vault. Check out the project demo!",
                        postType = "photo",
                        timestamp = "2h ago",
                        likesCount = 142,
                        isLiked = true,
                        imageUrl = StudyHiveAssets.PORTFOLIO_1,
                        tags = "#Hackfinix #CSD #AI"
                    ),
                    PostEntity(
                        authorName = "Sophia Lin",
                        authorHandle = "@sophia_c",
                        authorAvatar = StudyHiveAssets.SOPHIA_AVATAR,
                        title = "Data Science Lab 4 Cheatsheet",
                        content = "Uploaded the tensor shape debugging notes for Lab 4. Hope this saves folks from backpropagation headache tonight!",
                        postType = "document",
                        timestamp = "4h ago",
                        likesCount = 98,
                        isLiked = false,
                        documentTitle = "CS302_Lecture_Cheatsheet.pdf",
                        tags = "#CS302 #StudyGrind"
                    ),
                    PostEntity(
                        authorName = "Arjun Singh",
                        authorHandle = "@arjun_s",
                        authorAvatar = StudyHiveAssets.ARJUN_AVATAR,
                        title = "Library Pod 3 Active",
                        content = "Doing a 3-hour Pomodoro grind for Math 240 problem set 5. Feel free to drop by or ping in Hive chat!",
                        postType = "general",
                        timestamp = "6h ago",
                        likesCount = 45,
                        isLiked = false,
                        tags = "#CampusLife #Pomodoro"
                    )
                )
            )

            // Initial Chat Messages for Hive AI Tutor
            dao.insertMessages(
                listOf(
                    MessageEntity(
                        threadId = "ai_tutor",
                        senderName = "Hive AI Tutor",
                        senderAvatar = "",
                        text = "Hey Alex! 👋 I have indexed your CSD Year 1 syllabus and recent lecture slide decks. What are we studying today?",
                        timestamp = "Just now",
                        isFromUser = false
                    ),
                    MessageEntity(
                        threadId = "ai_tutor",
                        senderName = "Alex Chen",
                        senderAvatar = StudyHiveAssets.ALEX_AVATAR,
                        text = "Summarize my Data Science lecture notes on K-Means",
                        timestamp = "Just now",
                        isFromUser = true
                    ),
                    MessageEntity(
                        threadId = "ai_tutor",
                        senderName = "Hive AI Tutor",
                        senderAvatar = "",
                        text = "K-Means Core Synthesis:\n• Centroid Initialization: Pick k seed points.\n• Assignment Step: Group points using Euclidean metric.\n• Update Step: Recalculate mean of assignments.\n\nReady for a 3-question diagnostic quiz?",
                        timestamp = "Just now",
                        isFromUser = false
                    )
                )
            )

            // Initial Chat Messages for Sophia
            dao.insertMessages(
                listOf(
                    MessageEntity(
                        threadId = "sophia",
                        senderName = "Sophia Lin",
                        senderAvatar = StudyHiveAssets.SOPHIA_AVATAR,
                        text = "Did you finish question 4 of the problem set?",
                        timestamp = "2:15 PM",
                        isFromUser = false
                    ),
                    MessageEntity(
                        threadId = "sophia",
                        senderName = "Sophia Lin",
                        senderAvatar = StudyHiveAssets.SOPHIA_AVATAR,
                        text = "I am getting a segmentation fault on the binary tree traversals 😅",
                        timestamp = "2:16 PM",
                        isFromUser = false
                    ),
                    MessageEntity(
                        threadId = "sophia",
                        senderName = "Alex Chen",
                        senderAvatar = StudyHiveAssets.ALEX_AVATAR,
                        text = "Yes! Make sure you check if root == NULL before accessing the left child node!",
                        timestamp = "2:18 PM",
                        isFromUser = true
                    )
                )
            )
        }
    }
}
