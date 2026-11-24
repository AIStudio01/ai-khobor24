package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.api.BackendApiService
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore by preferencesDataStore(name = "aikhobor_prefs")

class NewsRepository(private val context: Context) {
    companion object {
        val BACKEND_URL_KEY = stringPreferencesKey("backend_url")
        val OWNER_EMAIL_KEY = stringPreferencesKey("owner_email")
    }

    val backendUrlFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[BACKEND_URL_KEY] ?: ""
    }

    val ownerEmailFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[OWNER_EMAIL_KEY] ?: "officemailsaty1@gmail.com"
    }

    suspend fun saveBackendUrl(url: String) {
        context.dataStore.edit { prefs ->
            prefs[BACKEND_URL_KEY] = url
        }
    }

    suspend fun saveOwnerEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[OWNER_EMAIL_KEY] = email
        }
    }

    // In-memory mock store for robust fallback & immediate testing
    private val _mockNews = MutableStateFlow(
        listOf(
            NewsItem(
                id = "n1",
                sourceId = "s1",
                title = "West Bengal Cabinet Approves New IT Park Expansion in Rajarhat",
                url = "https://news.google.com/rss/articles/1",
                publishedAt = "2026-11-23T10:30:00Z",
                category = "West Bengal",
                snippet = "The state cabinet on Monday approved Phase-2 expansion of Rajarhat IT hub, expected to create 25,000 tech jobs over 3 years.",
                hash = "hash1",
                status = "Collected",
                researchSummary = "Rajarhat IT hub expansion approved. 25,000 jobs expected. Investment ₹1,200 crore.",
                sourceUrls = listOf("https://news.google.com/1")
            ),
            NewsItem(
                id = "n2",
                sourceId = "s2",
                title = "Global Climate Summit Reaches Landmark Accord on Renewable Financing",
                url = "https://news.google.com/rss/articles/2",
                publishedAt = "2026-11-23T09:15:00Z",
                category = "World",
                snippet = "Delegates at the global climate summit agreed on a $500B fund to accelerate green energy transitions in developing nations.",
                hash = "hash2",
                status = "Research Done",
                researchSummary = "$500B green energy fund established. 195 countries signed. Implementation starts Q1 2027.",
                sourceUrls = listOf("https://news.google.com/2")
            ),
            NewsItem(
                id = "n3",
                sourceId = "s3",
                title = "AI Breakthrough: New Open-Source Model Outperforms Benchmarks in Bengali NLP",
                url = "https://news.google.com/rss/articles/3",
                publishedAt = "2026-11-23T08:00:00Z",
                category = "Technology",
                snippet = "Researchers unveiled a state-of-the-art language model tailored for Bengali dialects, boosting sentiment analysis and translation accuracy.",
                hash = "hash3",
                status = "Article Generated",
                researchSummary = "New Bengali NLP model released. Open source under Apache 2.0. 94% benchmark score.",
                sourceUrls = listOf("https://news.google.com/3")
            ),
            NewsItem(
                id = "n4",
                sourceId = "s4",
                title = "ISL 2026: Mohun Bagan Secures Dramatic Last-Minute Victory in Derby Clash",
                url = "https://news.google.com/rss/articles/4",
                publishedAt = "2026-11-22T21:00:00Z",
                category = "Sports",
                snippet = "A stunning 92nd-minute strike sealed a 2-1 win for Mohun Bagan Super Giant in front of a packed Salt Lake Stadium.",
                hash = "hash4",
                status = "Collected",
                researchSummary = "Mohun Bagan 2-1 win. Goal by striker in 92nd min. Attendance 65,000.",
                sourceUrls = listOf("https://news.google.com/4")
            )
        )
    )
    val mockNews: StateFlow<List<NewsItem>> = _mockNews.asStateFlow()

    private val _mockArticles = MutableStateFlow(
        listOf(
            ArticleItem(
                id = "a1",
                newsId = "n3",
                language = "Bengali",
                title = "বাংলা ভাষা প্রক্রিয়াকরণে যুগান্তকারী সাফল্য: নতুন ওপেন-সোর্স এআই মডেল উন্মোচন",
                bodyHtml = "<p>বাংলা ভাষার জন্য তৈরি একটি নতুন অত্যাধুনিক ওপেন-সোর্স এআই মডেল গবেষকদের দ্বারা উন্মোচিত হয়েছে, যা বিভিন্ন বেঞ্চমার্কে অসাধারণ সাফল্য দেখিয়েছে।</p><h2>বিশেষ বৈশিষ্ট্য</h2><p>এই মডেলে বাংলার বিভিন্ন আঞ্চলিক রূপ এবং উপভাষার নিখুঁত বিশ্লেষণ করা সম্ভব হয়েছে।</p><h3>ভবিষ্যৎ পরিকল্পনা</h3><p>গবেষকরা আশা করছেন এটি শিক্ষা এবং প্রযুক্তি ক্ষেত্রে বিপ্লব ঘটাবে।</p><h3>Sources</h3><ul><li><a href=\"https://news.google.com/3\">Google News</a></li></ul>",
                labels = listOf("Technology", "AI", "Research"),
                slug = "bengali-nlp-ai-model-2026",
                metaDescription = "বাংলা ভাষা প্রক্রিয়াকরণে নতুন এআই মডেল উন্মোচন। জানুন এর বিশেষ বৈশিষ্ট্য ও সুবিধা।",
                status = "Draft Saved",
                bloggerPostId = "bp_12345",
                bloggerUrl = "https://aikhobor24.blogspot.com/2026/11/bengali-nlp-ai-model.html",
                createdAt = "2026-11-23T08:30:00Z",
                updatedAt = "2026-11-23T09:00:00Z"
            ),
            ArticleItem(
                id = "a2",
                newsId = "n1",
                language = "Bengali",
                title = "রাজহাটে নতুন আইটি পার্কের সম্প্রসারণে অনুমোদন দিল রাজ্য মন্ত্রিসভা",
                bodyHtml = "<p>রাজ্য মন্ত্রিসভা সোমবার রাজারহাট আইটি পার্কের দ্বিতীয় পর্যায়ের সম্প্রসারণ অনুমোদন করেছে, যার ফলে আগামী ৩ বছরে ২৫,০০০ নতুন প্রযুক্তি চাকরি তৈরি হবে বলে আশা করা হচ্ছে।</p><h2>বিনিয়োগ ও কর্মসংস্থান</h2><p>এই প্রকল্পে প্রায় ১,২০০ কোটি টাকার বিনিয়োগ আসছে।</p><h3>Sources</h3><ul><li><a href=\"https://news.google.com/1\">Google News</a></li></ul>",
                labels = listOf("West Bengal", "Politics", "Technology"),
                slug = "rajarhat-it-park-expansion",
                metaDescription = "রাজহাটে নতুন আইটি পার্কের সম্প্রসারণে মন্ত্রিসভার অনুমোদন। ২৫ হাজার নতুন কর্মসংস্থানের সম্ভাবনা।",
                status = "Scheduled",
                scheduledAt = "2026-11-24T09:00:00Z",
                createdAt = "2026-11-23T11:00:00Z",
                updatedAt = "2026-11-23T11:30:00Z"
            )
        )
    )
    val mockArticles: StateFlow<List<ArticleItem>> = _mockArticles.asStateFlow()

    private val _mockSources = MutableStateFlow(
        listOf(
            SourceItem("s1", "Google News - West Bengal", "https://news.google.com/rss/search?q=West+Bengal+bengali&hl=bn&gl=IN&ceid=IN:bn", "West Bengal", true, 60, "2026-11-23T22:00:00Z"),
            SourceItem("s2", "Google News - World", "https://news.google.com/rss/search?q=World+news&hl=en&gl=IN&ceid=IN:en", "World", true, 60, "2026-11-23T21:45:00Z"),
            SourceItem("s3", "Google News - Technology", "https://news.google.com/rss/search?q=Technology+AI&hl=en&gl=IN&ceid=IN:en", "Technology", true, 120, "2026-11-23T20:00:00Z"),
            SourceItem("s4", "Google News - Sports", "https://news.google.com/rss/search?q=ISL+Mohun+Bagan+Cricket&hl=en&gl=IN&ceid=IN:en", "Sports", true, 60, "2026-11-23T21:30:00Z")
        )
    )
    val mockSources: StateFlow<List<SourceItem>> = _mockSources.asStateFlow()

    private val _mockLabels = MutableStateFlow(
        listOf(
            LabelItem("West Bengal", 154, "2026-11-23", true),
            LabelItem("World", 98, "2026-11-23", true),
            LabelItem("Politics", 210, "2026-11-23", true),
            LabelItem("Technology", 145, "2026-11-23", true),
            LabelItem("Sports", 188, "2026-11-23", true),
            LabelItem("Entertainment", 85, "2026-11-23", true),
            LabelItem("AI", 42, "2026-11-23", true),
            LabelItem("Research", 30, "2026-11-23", true)
        )
    )
    val mockLabels: StateFlow<List<LabelItem>> = _mockLabels.asStateFlow()

    private val _mockAutomation = MutableStateFlow(AutomationSettings())
    val mockAutomation: StateFlow<AutomationSettings> = _mockAutomation.asStateFlow()

    private val _mockLogs = MutableStateFlow(
        listOf(
            LogEntry("2026-11-23 22:15:00", "job_01", "INFO", "Collector", "Successfully fetched 4 RSS sources."),
            LogEntry("2026-11-23 21:00:00", "job_02", "INFO", "Researcher", "Gemini API research summary generated for n3."),
            LogEntry("2026-11-23 20:30:00", "job_03", "INFO", "Blogger", "Draft saved successfully to Blogger (bp_12345).")
        )
    )
    val mockLogs: StateFlow<List<LogEntry>> = _mockLogs.asStateFlow()

    fun updateArticleInMock(article: ArticleItem) {
        val list = _mockArticles.value.toMutableList()
        val index = list.indexOfFirst { it.id == article.id }
        if (index >= 0) {
            list[index] = article
        } else {
            list.add(0, article)
        }
        _mockArticles.value = list
    }

    fun deleteArticleInMock(id: String) {
        _mockArticles.value = _mockArticles.value.filter { it.id != id }
    }

    fun updateAutomation(settings: AutomationSettings) {
        _mockAutomation.value = settings
    }
}
