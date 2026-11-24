package com.example.data.api

import com.example.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BackendApiService {
    @GET("?action=getDashboard")
    suspend fun getDashboard(@Query("token") token: String? = null): DashboardStats

    @GET("?action=listNews")
    suspend fun listNews(@Query("category") category: String? = null, @Query("status") status: String? = null): List<NewsItem>

    @POST("?action=collectNews")
    suspend fun collectNews(): Map<String, Any>

    @POST("?action=researchNews")
    suspend fun researchNews(@Query("id") id: String): NewsItem

    @POST("?action=generateArticle")
    suspend fun generateArticle(@Query("id") id: String, @Query("language") language: String = "Bengali"): ArticleItem

    @GET("?action=listArticles")
    suspend fun listArticles(@Query("status") status: String? = null): List<ArticleItem>

    @POST("?action=updateArticle")
    suspend fun updateArticle(@Body article: ArticleItem): ArticleItem

    @POST("?action=saveBloggerDraft")
    suspend fun saveBloggerDraft(@Query("id") id: String): ArticleItem

    @POST("?action=publishArticle")
    suspend fun publishArticle(@Query("id") id: String): ArticleItem

    @POST("?action=scheduleArticle")
    suspend fun scheduleArticle(@Query("id") id: String, @Query("datetime") datetime: String): ArticleItem

    @GET("?action=listSources")
    suspend fun listSources(): List<SourceItem>

    @GET("?action=listCategories")
    suspend fun listCategories(): List<CategoryItem>

    @GET("?action=listLabels")
    suspend fun listLabels(): List<LabelItem>

    @POST("?action=syncLabels")
    suspend fun syncLabels(): List<LabelItem>

    @GET("?action=getAutomationSettings")
    suspend fun getAutomationSettings(): AutomationSettings

    @POST("?action=updateAutomationSettings")
    suspend fun updateAutomationSettings(@Body settings: AutomationSettings): AutomationSettings

    @GET("?action=listLogs")
    suspend fun listLogs(): List<LogEntry>

    @GET("?action=testConnections")
    suspend fun testConnections(): Map<String, Boolean>
}
