package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "academic_tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val courseCode: String,
    val dueString: String,
    val isCompleted: Boolean = false,
    val isUrgent: Boolean = false,
    val category: String = "upcoming", // "overdue", "upcoming", "completed"
    val progressPercent: Int = 0,
    val xpGained: Int = 50,
    val completedAt: Long = 0
)

@Entity(tableName = "resource_vault")
data class ResourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val courseCode: String,
    val folderName: String,
    val fileSize: String,
    val author: String,
    val upvotes: Int,
    val isUpvoted: Boolean = false,
    val fileType: String = "pdf", // "pdf", "code", "slide"
    val isDownloaded: Boolean = false
)

@Entity(tableName = "campus_posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val title: String,
    val content: String,
    val postType: String, // "photo", "document", "general", "question", "story"
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val imageUrl: String? = null,
    val documentTitle: String? = null,
    val tags: String = "",
    val courseTag: String? = null
)

@Entity(tableName = "chat_messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val threadId: String, // "ai_tutor", "sophia", "arjun", "david", "csd_batch", "ds_lab", "math_circle"
    val senderName: String,
    val senderAvatar: String,
    val text: String,
    val timestamp: String,
    val isFromUser: Boolean = false,
    val attachmentName: String? = null
)
