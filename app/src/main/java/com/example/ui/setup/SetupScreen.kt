package com.example.ui.setup

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
fun SetupScreen(viewModel: MainViewModel, onSetupComplete: () -> Unit) {
    val backendUrl by viewModel.backendUrl.collectAsState()
    val ownerEmail by viewModel.ownerEmail.collectAsState()
    val isTesting by viewModel.isTesting.collectAsState()
    val testStatus by viewModel.connectionTestStatus.collectAsState()

    var urlInput by remember { mutableStateOf(backendUrl) }
    var emailInput by remember { mutableStateOf(ownerEmail) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Khobor24 - Setup & Onboarding", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Welcome to AI Khobor24", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Control your automated Bengali/English AI news pipeline for aikhobor24.blogspot.com directly from Android. Connect your Google Apps Script backend and verify connections.")
                }
            }

            OutlinedTextField(
                value = urlInput,
                onValueChange = { urlInput = it },
                label = { Text("Google Apps Script Web App URL") },
                placeholder = { Text("https://script.google.com/macros/s/.../exec") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("backend_url_input"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) }
            )

            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Owner Google Email") },
                placeholder = { Text("officemailsaty1@gmail.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("owner_email_input"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.setBackendUrl(urlInput)
                        viewModel.setOwnerEmail(emailInput)
                        viewModel.testConnections()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_connections_button")
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Test Connections")
                    }
                }

                Button(
                    onClick = {
                        viewModel.setBackendUrl(urlInput)
                        viewModel.setOwnerEmail(emailInput)
                        onSetupComplete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("save_and_proceed_button")
                ) {
                    Text("Proceed to App")
                }
            }

            if (testStatus != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Connection Test Results", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        testStatus?.forEach { (service, passed) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(service.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Medium)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.Error,
                                        contentDescription = null,
                                        tint = if (passed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (passed) "PASS" else "FAIL", color = if (passed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
