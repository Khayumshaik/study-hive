package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Classmate
import com.example.data.model.MessageEntity
import com.example.data.model.PortfolioItem
import com.example.data.model.PostEntity
import com.example.data.model.ResourceEntity
import com.example.data.model.StudentProfile
import com.example.data.model.StudyGroup
import com.example.data.model.StudyInvite
import com.example.data.model.TaskEntity
import com.example.data.repository.StudyHiveRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainNavigationTab {
    HOME,
    TRACKER,
    CHAT,
    PROFILE
}

enum class TrackerSubTab {
    DEADLINES,
    VAULT
}

enum class TaskFilter {
    ALL,
    OVERDUE,
    UPCOMING,
    COMPLETED
}

enum class ChatSubTab {
    HIVE_AI,
    DIRECT,
    GROUPS,
    REQUESTS
}

enum class ProfileSubTab {
    PORTFOLIO,
    BADGES
}

data class ActivePeerChat(
    val threadId: String,
    val name: String,
    val subtitle: String,
    val avatarUrl: String,
    val isOnline: Boolean
)

class StudyHiveViewModel(application: Application) : AndroidViewModel(application) {
    val repository = StudyHiveRepository.getInstance(application)

    // Auth & Navigation
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentTab = MutableStateFlow(MainNavigationTab.TRACKER)
    val currentTab: StateFlow<MainNavigationTab> = _currentTab.asStateFlow()

    // Modals and Sheets
    private val _showCreateSheet = MutableStateFlow(false)
    val showCreateSheet: StateFlow<Boolean> = _showCreateSheet.asStateFlow()

    private val _createFormType = MutableStateFlow<String?>(null)
    val createFormType: StateFlow<String?> = _createFormType.asStateFlow()

    private val _showSettingsDrawer = MutableStateFlow(false)
    val showSettingsDrawer: StateFlow<Boolean> = _showSettingsDrawer.asStateFlow()

    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _showCreateGroupDialog = MutableStateFlow(false)
    val showCreateGroupDialog: StateFlow<Boolean> = _showCreateGroupDialog.asStateFlow()

    private val _showClassmateDirectoryDialog = MutableStateFlow(false)
    val showClassmateDirectoryDialog: StateFlow<Boolean> = _showClassmateDirectoryDialog.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog: StateFlow<Boolean> = _showSearchDialog.asStateFlow()

    private val _showAddTaskDialog = MutableStateFlow(false)
    val showAddTaskDialog: StateFlow<Boolean> = _showAddTaskDialog.asStateFlow()

    private val _selectedPortfolioItem = MutableStateFlow<PortfolioItem?>(null)
    val selectedPortfolioItem: StateFlow<PortfolioItem?> = _selectedPortfolioItem.asStateFlow()

    // Tracker State
    private val _trackerSubTab = MutableStateFlow(TrackerSubTab.DEADLINES)
    val trackerSubTab: StateFlow<TrackerSubTab> = _trackerSubTab.asStateFlow()

    private val _taskFilter = MutableStateFlow(TaskFilter.ALL)
    val taskFilter: StateFlow<TaskFilter> = _taskFilter.asStateFlow()

    private val _vaultSearchQuery = MutableStateFlow("")
    val vaultSearchQuery: StateFlow<String> = _vaultSearchQuery.asStateFlow()

    // Chat State
    private val _chatSubTab = MutableStateFlow(ChatSubTab.HIVE_AI)
    val chatSubTab: StateFlow<ChatSubTab> = _chatSubTab.asStateFlow()

    private val _chatSearchQuery = MutableStateFlow("")
    val chatSearchQuery: StateFlow<String> = _chatSearchQuery.asStateFlow()

    private val _activePeerChat = MutableStateFlow<ActivePeerChat?>(null)
    val activePeerChat: StateFlow<ActivePeerChat?> = _activePeerChat.asStateFlow()

    // Profile State
    private val _profileSubTab = MutableStateFlow(ProfileSubTab.PORTFOLIO)
    val profileSubTab: StateFlow<ProfileSubTab> = _profileSubTab.asStateFlow()

    // Confetti Event counter for animation trigger
    private val _confettiCount = MutableStateFlow(0)
    val confettiCount: StateFlow<Int> = _confettiCount.asStateFlow()

    // Pomodoro Timer for Team A
    private val _pomodoroSecondsLeft = MutableStateFlow(18 * 60 + 42)
    val pomodoroSecondsLeft: StateFlow<Int> = _pomodoroSecondsLeft.asStateFlow()
    private var pomodoroJob: Job? = null

    // Reactive Data from Repository
    val studentProfile: StateFlow<StudentProfile> = repository.studentProfile
    val studyInvites: StateFlow<List<StudyInvite>> = repository.studyInvites
    val classmates: StateFlow<List<Classmate>> = repository.classmates
    val studyGroups: StateFlow<List<StudyGroup>> = repository.studyGroups

    val portfolioItems = repository.portfolioItems
    val academicBadges = repository.academicBadges
    val classRepositories = repository.classRepositories

    val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val filteredTasks: StateFlow<List<TaskEntity>> = combine(allTasks, _taskFilter) { tasks, filter ->
        when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.OVERDUE -> tasks.filter { it.isUrgent && !it.isCompleted }
            TaskFilter.UPCOMING -> tasks.filter { !it.isCompleted && !it.isUrgent }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allResources: StateFlow<List<ResourceEntity>> = combine(repository.allResources, _vaultSearchQuery) { res, query ->
        if (query.isBlank()) res else res.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.courseCode.contains(query, ignoreCase = true) ||
            it.folderName.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPosts: StateFlow<List<PostEntity>> = repository.allPosts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            repository.confettiTrigger.collect {
                _confettiCount.value += 1
            }
        }

        pomodoroJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_pomodoroSecondsLeft.value > 0) {
                    _pomodoroSecondsLeft.value -= 1
                } else {
                    _pomodoroSecondsLeft.value = 25 * 60
                }
            }
        }
    }

    // Navigation Setters
    fun setNavigationTab(tab: MainNavigationTab) {
        _currentTab.value = tab
    }

    fun setTrackerSubTab(subTab: TrackerSubTab) {
        _trackerSubTab.value = subTab
    }

    fun setTaskFilter(filter: TaskFilter) {
        _taskFilter.value = filter
    }

    fun setChatSubTab(subTab: ChatSubTab) {
        _chatSubTab.value = subTab
    }

    fun setProfileSubTab(subTab: ProfileSubTab) {
        _profileSubTab.value = subTab
    }

    fun setVaultSearchQuery(q: String) {
        _vaultSearchQuery.value = q
    }

    fun setChatSearchQuery(q: String) {
        _chatSearchQuery.value = q
    }

    // Modal Setters
    fun openCreateModal(initialType: String? = null) {
        _createFormType.value = initialType
        _showCreateSheet.value = true
    }

    fun closeCreateModal() {
        _showCreateSheet.value = false
        _createFormType.value = null
    }

    fun selectCreateOption(type: String) {
        _createFormType.value = type
    }

    fun backToCreateMenu() {
        _createFormType.value = null
    }

    fun setSettingsDrawerOpen(open: Boolean) {
        _showSettingsDrawer.value = open
    }

    fun setEditProfileDialogOpen(open: Boolean) {
        _showEditProfileDialog.value = open
    }

    fun setCreateGroupDialogOpen(open: Boolean) {
        _showCreateGroupDialog.value = open
    }

    fun setClassmateDirectoryOpen(open: Boolean) {
        _showClassmateDirectoryDialog.value = open
    }

    fun setNotificationsSheetOpen(open: Boolean) {
        _showNotificationsSheet.value = open
    }

    fun setSearchDialogOpen(open: Boolean) {
        _showSearchDialog.value = open
    }

    fun setAddTaskDialogOpen(open: Boolean) {
        _showAddTaskDialog.value = open
    }

    fun selectPortfolioItem(item: PortfolioItem?) {
        _selectedPortfolioItem.value = item
    }

    fun openPeerChat(threadId: String, name: String, subtitle: String, avatarUrl: String, isOnline: Boolean) {
        _activePeerChat.value = ActivePeerChat(threadId, name, subtitle, avatarUrl, isOnline)
    }

    fun closePeerChat() {
        _activePeerChat.value = null
    }

    // Actions
    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskComplete(task)
        }
    }

    fun addTask(title: String, courseCode: String, dueDate: String, isUrgent: Boolean) {
        viewModelScope.launch {
            repository.addNewTask(title, courseCode, dueDate, isUrgent)
            _showAddTaskDialog.value = false
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun shareTaskStory(taskTitle: String) {
        viewModelScope.launch {
            repository.shareTaskStory(taskTitle)
        }
    }

    fun toggleUpvote(resource: ResourceEntity) {
        viewModelScope.launch {
            repository.toggleUpvote(resource)
        }
    }

    fun downloadResource(resource: ResourceEntity) {
        viewModelScope.launch {
            repository.simulateDownload(resource)
        }
    }

    fun uploadDocument(title: String, courseCode: String, folderName: String) {
        viewModelScope.launch {
            repository.uploadResource(title, courseCode, folderName)
            closeCreateModal()
        }
    }

    fun togglePostLike(post: PostEntity) {
        viewModelScope.launch {
            repository.togglePostLike(post)
        }
    }

    fun publishPost(postType: String, title: String, content: String, tags: String, imageUrl: String? = null, documentTitle: String? = null) {
        viewModelScope.launch {
            repository.publishCampusPost(postType, title, content, tags, imageUrl, documentTitle)
            closeCreateModal()
        }
    }

    fun sendAiTutorMessage(promptText: String) {
        viewModelScope.launch {
            repository.sendAiTutorQuery(promptText)
        }
    }

    fun sendPeerMessage(threadId: String, text: String) {
        viewModelScope.launch {
            repository.sendPeerMessage(threadId, text)
        }
    }

    fun handleStudyInvite(inviteId: String, accept: Boolean) {
        repository.handleStudyInvite(inviteId, accept)
    }

    fun toggleConnectClassmate(peerId: String) {
        repository.toggleConnectClassmate(peerId)
    }

    fun createStudyGroup(name: String, courseCode: String, section: String, room: String) {
        repository.createStudyGroup(name, courseCode, section, room)
        _showCreateGroupDialog.value = false
    }

    fun updateProfile(
        name: String,
        pronouns: String,
        bio: String,
        classSection: String,
        rollNumber: String,
        department: String,
        email: String
    ) {
        repository.updateProfile { p ->
            p.copy(
                name = name,
                pronouns = pronouns,
                bio = bio,
                classSection = classSection,
                rollNumber = rollNumber,
                department = department,
                majorYear = "$department • $classSection",
                email = email
            )
        }
        _showEditProfileDialog.value = false
        showToast("Profile details updated!")
    }

    fun updateProfileSetting(updater: (StudentProfile) -> StudentProfile) {
        repository.updateProfile(updater)
    }

    fun registerNewAccount(
        fullName: String,
        pronouns: String,
        email: String,
        rollNumber: String,
        department: String,
        section: String,
        bio: String
    ) {
        repository.registerNewAccount(
            fullName = fullName,
            pronouns = pronouns,
            email = email,
            rollNumber = rollNumber,
            department = department,
            section = section,
            bio = bio
        )
        _isLoggedIn.value = true
    }

    fun triggerConfetti() {
        _confettiCount.value += 1
    }

    fun showToast(msg: String) {
        viewModelScope.launch {
            repository.emitToast(msg)
        }
    }

    fun logIn() {
        _isLoggedIn.value = true
        showToast("Welcome back, ${studentProfile.value.name}!")
    }

    fun logOut() {
        _showSettingsDrawer.value = false
        _isLoggedIn.value = false
        showToast("Signed out of StudyHive")
    }
}
