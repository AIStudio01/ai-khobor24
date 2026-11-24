package com.example.ui.automation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationScreen(viewModel: MainViewModel) {
    val automation by viewModel.automationSettings.collectAsState()
    var showAutoPublishDialog by remember { mutableStateOf(false) }

    var collectEnabled by remember(automation) { mutableStateOf(automation.collectEnabled) }
    var researchEnabled by remember(automation) { mutableStateOf(automation.researchEnabled) }
    var generateEnabled by remember(automation) { mutableStateOf(automation.generateEnabled) }
    var autoPublish by remember(automation) { mutableStateOf(automation.autoPublish) }
    var intervalMinutes by remember(automation) { mutableStateOf(automation.intervalMinutes) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pipeline Automation Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("24/7 Backend Automation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Runs on Google Apps Script triggers every $intervalMinutes minutes in timezone Asia/Kolkata.")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pipeline Steps", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("1. Collect RSS News")
                        Switch(
                            checked = collectEnabled,
                            onCheckedChange = {
                                collectEnabled = it
                                viewModel.updateAutomation(automation.copy(collectEnabled = it))
                            },
                            modifier = Modifier.testTag("switch_collect")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("2. AI Research Summary")
                        Switch(
                            checked = researchEnabled,
                            onCheckedChange = {
                                researchEnabled = it
                                viewModel.updateAutomation(automation.copy(researchEnabled = it))
                            },
                            modifier = Modifier.testTag("switch_research")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("3. AI Bengali Article Generation")
                        Switch(
                            checked = generateEnabled,
                            onCheckedChange = {
                                generateEnabled = it
                                viewModel.updateAutomation(automation.copy(generateEnabled = it))
                            },
                            modifier = Modifier.testTag("switch_generate")
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Publishing Safety", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto-Publish to Blogger", fontWeight = FontWeight.Medium)
                            Text("When OFF, outputs are saved as Blogger drafts first for your review.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(
                            checked = autoPublish,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    showAutoPublishDialog = true
                                } else {
                                    autoPublish = false
                                    viewModel.updateAutomation(automation.copy(autoPublish = false))
                                }
                            },
                            modifier = Modifier.testTag("switch_autopublish")
                        )
                    }
                }
            }
        }
    }

    if (showAutoPublishDialog) {
        AlertDialog(
            onDismissRequest = { showAutoPublishDialog = false },
            title = { Text("Enable Auto-Publish?") },
            text = { Text("Enabling auto-publish will automatically push generated articles live without manual draft approval. Are you sure?") },
            confirmButton = {
                Button(onClick = {
                    autoPublish = true
                    viewModel.updateAutomation(automation.copy(autoPublish = true))
                    showAutoPublishDialog = false
                }) {
                    Text("Enable Auto-Publish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAutoPublishDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
