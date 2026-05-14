package com.karunadavanya.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.presentation.AlertDetailViewModel
import com.karunadavanya.presentation.localization.LocalAppLanguage
import com.karunadavanya.presentation.localization.alertDescription
import com.karunadavanya.presentation.localization.alertTitle
import com.karunadavanya.presentation.localization.tr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDetailScreen(
    alertId: String,
    onBack: () -> Unit,
    viewModel: AlertDetailViewModel = hiltViewModel()
) {
    val alert by viewModel.alert.collectAsState()
    val language = LocalAppLanguage.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(tr("Alert Details", language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        val currentAlert = alert
        if (currentAlert == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Priority Badge
            item {
                Surface(
                    color = getPriorityColor(currentAlert.severity).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.PriorityHigh,
                            contentDescription = null,
                            tint = getPriorityColor(currentAlert.severity)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "${tr(currentAlert.severity, language)} ${tr("Priority Alert", language)}",
                            fontWeight = FontWeight.Bold,
                            color = getPriorityColor(currentAlert.severity)
                        )
                    }
                }
            }

            // Title and Description
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = alertTitle(currentAlert, language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = alertDescription(currentAlert, language),
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                }
            }

            // Location Card
            item {
                InfoCard(
                    icon = Icons.Default.LocationOn,
                    label = tr("Location", language),
                    value = currentAlert.location
                )
            }

            // Time and Reporter Info
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AccessTime,
                        label = tr("Reported At", language),
                        value = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(currentAlert.reportedAt))
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Person,
                        label = tr("Reporter", language),
                        value = tr(currentAlert.reporter, language)
                    )
                }
            }

            // Safety Tips Section
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.width(8.dp))
                            Text(tr("Safety Advice", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            tr("Please maintain a safe distance and do not attempt to approach the animal. Notify local authorities if the situation escalates.", language),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun getPriorityColor(severity: String): Color {
    return when (severity.lowercase()) {
        "high" -> Color(0xFFD32F2F)
        "medium" -> Color(0xFFF57C00)
        "low" -> Color(0xFF388E3C)
        else -> Color.Gray
    }
}
