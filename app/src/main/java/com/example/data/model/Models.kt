package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DashboardStats(
    val collectedCount: Int = 14,
    val researchedCount: Int = 8,
    val articleReadyCount: Int = 5,
    val draftSavedCount: Int = 3,
    val scheduledCount: Int = 1,
    val publishedCount: Int = 42,
    val todayPublished: Int = 3,
    val backendConnected: Boolean = true,
    val sheetsConnected: Boolean = true,
    val geminiConnected: Boolean = true,
    val bloggerConnected: Boolean = true,
    val driveConnected: Boolean = true,
    val activities: List<ActivityLog> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ActivityLog(
    val timestamp: String,
    val module: String,
    val message: String,
    val level: String = "INFO"
)

@JsonClass(generateAdapter = true)
data class NewsItem(
    val id: String,
    val sourceId: String,
    val title: String,
    val url: String,
    val publishedAt: String,
    val category: String,
    val snippet: String,
    val hash: String,
    val status: String,
    val researchSummary: String? = null,
    val sourceUrls: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ArticleItem(
    val id: String,
    val newsId: String,
    val language: String = "Bengali",
    val title: String,
    val bodyHtml: String,
    val labels: List<String>,
    val slug: String,
    val metaDescription: String,
    val imageId: String? = null,
    val imageUrl: String? = null,
    val status: String,
    val bloggerPostId: String? = null,
    val bloggerUrl: String? = null,
    val scheduledAt: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class SourceItem(
    val id: String,
    val name: String,
    val url: String,
    val category: String,
    val enabled: Boolean,
    val intervalMin: Int,
    val lastFetch: String,
    val lastError: String? = null
)

@JsonClass(generateAdapter = true)
data class CategoryItem(
    val name: String,
    val bloggerLabel: String,
    val prompt: String,
    val style: String,
    val dailyLimit: Int,
    val enabled: Boolean
)

@JsonClass(generateAdapter = true)
data class LabelItem(
    val label: String,
    val postCount: Int,
    val lastSeen: String,
    val enabled: Boolean
)

@JsonClass(generateAdapter = true)
data class AutomationSettings(
    val collectEnabled: Boolean = true,
    val researchEnabled: Boolean = true,
    val generateEnabled: Boolean = true,
    val autoPublish: Boolean = false,
    val intervalMinutes: Int = 60,
    val timezone: String = "Asia/Kolkata",
    val allowNewLabels: Boolean = false
)

@JsonClass(generateAdapter = true)
data class LogEntry(
    val timestamp: String,
    val jobId: String,
    val level: String,
    val module: String,
    val message: String
)
