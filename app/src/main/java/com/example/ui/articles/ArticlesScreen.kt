package com.example.ui.articles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ArticleItem
import com.example.ui.main.MainViewModel

@Composable
fun ArticlesScreen(viewModel: MainViewModel) {
    val articles by viewModel.articlesList.collectAsState()
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Draft Saved", "Scheduled", "Published")

    var editingArticle by remember { mutableStateOf<ArticleItem?>(null) }

    val filteredArticles = if (selectedTab == "All") articles else articles.filter { it.status == selectedTab }

    if (editingArticle != null) {
        ArticleEditorScreen(
            article = editingArticle!!,
            viewModel = viewModel,
            onDismiss = { editingArticle = null }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Articles & Drafts", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            ScrollableTabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                edgePadding = 0.dp
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab) },
                        modifier = Modifier.testTag("tab_$tab")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredArticles) { article ->
                    ArticleCard(
                        article = article,
                        onEdit = { editingArticle = article },
                        onSaveDraft = { viewModel.saveDraftToBlogger(article.id) },
                        onPublish = { viewModel.publishArticle(article.id) },
                        onDelete = { viewModel.deleteArticle(article.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: ArticleItem,
    onEdit: () -> Unit,
    onSaveDraft: () -> Unit,
    onPublish: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("article_card_${article.id}"),
        onClick = onEdit,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Text(article.status)
                }
                Text(article.updatedAt.take(10), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(article.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(article.metaDescription, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                article.labels.forEach { label ->
                    AssistChip(onClick = {}, label = { Text(label) })
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
                if (article.status != "Published" && article.status != "Draft Saved") {
                    OutlinedButton(onClick = onSaveDraft) {
                        Text("Save Draft")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                if (article.status != "Published") {
                    Button(onClick = onPublish) {
                        Text("Publish")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleEditorScreen(
    article: ArticleItem,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(article.title) }
    var bodyHtml by remember { mutableStateOf(article.bodyHtml) }
    var metaDescription by remember { mutableStateOf(article.metaDescription) }
    var slug by remember { mutableStateOf(article.slug) }
    var showPreview by remember { mutableStateOf(false) }

    val wordCount = bodyHtml.split(Regex("\\s+")).size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Article Editor") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showPreview = !showPreview }) {
                        Text(if (showPreview) "Edit Mode" else "Preview")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showPreview) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Preview", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Slug: $slug", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Body HTML length: ${bodyHtml.length} chars ($wordCount words)")
                    }
                }
            } else {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = slug,
                    onValueChange = { slug = it },
                    label = { Text("Slug / URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = metaDescription,
                    onValueChange = { metaDescription = it },
                    label = { Text("Meta Description (<= 160 chars)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bodyHtml,
                    onValueChange = { bodyHtml = it },
                    label = { Text("Body HTML") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Word count: $wordCount", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {
                            viewModel.updateArticle(article.copy(title = title, bodyHtml = bodyHtml, metaDescription = metaDescription, slug = slug))
                            onDismiss()
                        }) {
                            Text("Save Changes")
                        }
                        Button(onClick = {
                            val updated = article.copy(title = title, bodyHtml = bodyHtml, metaDescription = metaDescription, slug = slug)
                            viewModel.updateArticle(updated)
                            viewModel.saveDraftToBlogger(updated.id)
                            onDismiss()
                        }) {
                            Text("Save Draft to Blogger")
                        }
                    }
                }
            }
        }
    }
}
