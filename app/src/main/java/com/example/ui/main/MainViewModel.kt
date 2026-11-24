package com.example.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.NewsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsRepository(application)

    val backendUrl: StateFlow<String> = repository.backendUrlFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val ownerEmail: StateFlow<String> = repository.ownerEmailFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "officemailsaty1@gmail.com")

    val newsList: StateFlow<List<NewsItem>> = repository.mockNews
    val articlesList: StateFlow<List<ArticleItem>> = repository.mockArticles
    val sourcesList: StateFlow<List<SourceItem>> = repository.mockSources
    val labelsList: StateFlow<List<LabelItem>> = repository.mockLabels
    val automationSettings: StateFlow<AutomationSettings> = repository.mockAutomation
    val logsList: StateFlow<List<LogEntry>> = repository.mockLogs

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _connectionTestStatus = MutableStateFlow<Map<String, Boolean>?>(null)
    val connectionTestStatus: StateFlow<Map<String, Boolean>?> = _connectionTestStatus.asStateFlow()

    private val _isTesting = MutableStateFlow(false)
    val isTesting: StateFlow<Boolean> = _isTesting.asStateFlow()

    fun setBackendUrl(url: String) {
        viewModelScope.launch {
            repository.saveBackendUrl(url)
        }
    }

    fun setOwnerEmail(email: String) {
        viewModelScope.launch {
            repository.saveOwnerEmail(email)
        }
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun testConnections() {
        viewModelScope.launch {
            _isTesting.value = true
            kotlinx.coroutines.delay(1200)
            _connectionTestStatus.value = mapOf(
                "backend" to true,
                "sheets" to true,
                "gemini" to true,
                "blogger" to true,
                "drive" to true
            )
            _isTesting.value = false
        }
    }

    fun collectNewsNow() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            // simulate adding a new collected news item
            val current = repository.mockNews.value.toMutableList()
            val newId = "n${current.size + 1}"
            current.add(
                0,
                NewsItem(
                    id = newId,
                    sourceId = "s1",
                    title = "Kolkata Metro Extends Orange Line Services to New Hub",
                    url = "https://news.google.com/rss/articles/new",
                    publishedAt = "2026-11-23T22:00:00Z",
                    category = "West Bengal",
                    snippet = "Commuters welcomed the extended Orange Line metro services connecting major transit points with high frequency.",
                    hash = "hash_new",
                    status = "Collected",
                    researchSummary = "Orange Line extended. Daily passenger count expected 50,000.",
                    sourceUrls = listOf("https://news.google.com/new")
                )
            )
            // Also add log
            val logs = repository.mockLogs.value.toMutableList()
            logs.add(0, LogEntry("2026-11-23 22:30:00", "job_manual", "INFO", "Collector", "Manual collection triggered successfully."))
        }
    }

    fun generateArticle(newsId: String) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            val newsItem = repository.mockNews.value.find { it.id == newsId } ?: return@launch
            val newArticle = ArticleItem(
                id = "a_${System.currentTimeMillis()}",
                newsId = newsId,
                language = "Bengali",
                title = newsItem.title,
                bodyHtml = "<p>${newsItem.snippet}</p><h2>বিশ্লেষণ</h2><p>${newsItem.researchSummary ?: ""}</p><h3>Sources</h3><ul><li><a href=\"${newsItem.url}\">Original Source</a></li></ul>",
                labels = listOf(newsItem.category, "AI"),
                slug = newsItem.title.lowercase().replace(Regex("[^a-z0-9]+"), "-"),
                metaDescription = newsItem.snippet,
                status = "Article Generated",
                createdAt = "2026-11-23T22:35:00Z",
                updatedAt = "2026-11-23T22:35:00Z"
            )
            repository.updateArticleInMock(newArticle)
        }
    }

    fun saveDraftToBlogger(articleId: String) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(900)
            val article = repository.mockArticles.value.find { it.id == articleId } ?: return@launch
            val updated = article.copy(
                status = "Draft Saved",
                bloggerPostId = "bp_${System.currentTimeMillis()}",
                bloggerUrl = "https://aikhobor24.blogspot.com/2026/11/" + article.slug + ".html",
                updatedAt = "2026-11-23T22:40:00Z"
            )
            repository.updateArticleInMock(updated)
        }
    }

    fun publishArticle(articleId: String) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(900)
            val article = repository.mockArticles.value.find { it.id == articleId } ?: return@launch
            val updated = article.copy(
                status = "Published",
                bloggerPostId = article.bloggerPostId ?: "bp_${System.currentTimeMillis()}",
                bloggerUrl = article.bloggerUrl ?: "https://aikhobor24.blogspot.com/2026/11/" + article.slug + ".html",
                updatedAt = "2026-11-23T22:42:00Z"
            )
            repository.updateArticleInMock(updated)
        }
    }

    fun updateArticle(article: ArticleItem) {
        repository.updateArticleInMock(article)
    }

    fun deleteArticle(articleId: String) {
        repository.deleteArticleInMock(articleId)
    }

    fun updateAutomation(settings: AutomationSettings) {
        repository.updateAutomation(settings)
    }
}
