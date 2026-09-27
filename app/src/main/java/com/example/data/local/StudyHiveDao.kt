package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MessageEntity
import com.example.data.model.PostEntity
import com.example.data.model.ResourceEntity
import com.example.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyHiveDao {
    // Tasks
    @Query("SELECT * FROM academic_tasks ORDER BY isCompleted ASC, id ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE academic_tasks SET isCompleted = :isCompleted, category = :category, completedAt = :completedAt WHERE id = :id")
    suspend fun setTaskCompletion(id: Long, isCompleted: Boolean, category: String, completedAt: Long)

    @Query("DELETE FROM academic_tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    // Resource Vault
    @Query("SELECT * FROM resource_vault ORDER BY upvotes DESC")
    fun getAllResources(): Flow<List<ResourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ResourceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ResourceEntity>)

    @Query("UPDATE resource_vault SET isUpvoted = :isUpvoted, upvotes = :upvotes WHERE id = :id")
    suspend fun updateUpvote(id: Long, isUpvoted: Boolean, upvotes: Int)

    @Query("UPDATE resource_vault SET isDownloaded = 1 WHERE id = :id")
    suspend fun setDownloaded(id: Long)

    // Posts
    @Query("SELECT * FROM campus_posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE campus_posts SET isLiked = :isLiked, likesCount = :likesCount WHERE id = :id")
    suspend fun updatePostLike(id: Long, isLiked: Boolean, likesCount: Int)

    // Messages
    @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY id ASC")
    fun getMessagesForThread(threadId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
}
