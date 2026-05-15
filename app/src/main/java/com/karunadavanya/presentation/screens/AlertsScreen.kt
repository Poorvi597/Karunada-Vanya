package com.karunadavanya.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.presentation.AlertViewModel
import com.karunadavanya.presentation.localization.LocalAppLanguage
import com.karunadavanya.presentation.localization.alertDescription
import com.karunadavanya.presentation.localization.alertTitle
import com.karunadavanya.presentation.localization.relativeTimeText
import com.karunadavanya.presentation.localization.tr
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private val ForestPrimary = Color(0xFF012D1D)
private val ForestContainer = Color(0xFF1B4332)
private val SurfaceIvory = Color(0xFFFFF8F3)
private val SurfaceLow = Color(0xFFFAF2EC)
private val EarthSecondary = Color(0xFF805533)
private val OutlineSoft = Color(0xFFC1C8C2)
private val TextMuted = Color(0xFF414844)
private val ErrorRed = Color(0xFFBA1A1A)

@Composable
fun AlertsScreen(
    onHome: () -> Unit,
    onExplore: () -> Unit,
    onReport: () -> Unit,
    onAlertClick: (String) -> Unit,
    viewModel: AlertViewModel = hiltViewModel()
) {
    val alerts = viewModel.alerts
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            scope.launchAlertLocationUpdate(context, viewModel)
        }
    }

    LaunchedEffect(Unit) {
        if (hasAlertLocationPermission(context)) {
            currentAlertLocationOrNull(context)?.let { location ->
                viewModel.updateUserLocation(location.latitude, location.longitude)
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        containerColor = SurfaceIvory,
        topBar = { KarunadaTopBar() },
        bottomBar = {
            KarunadaBottomNav(
                selected = "Alerts",
                onHome = onHome,
                onExploreWildlife = onExplore,
                onOpenAlerts = {},
                onOpenReport = onReport
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { AlertsHeader() }

            if (alerts.isEmpty()) {
                item {
                    EmptyAlertsCard()
                }
            } else {
                items(alerts, key = { it.id }) { alert ->
                    AlertFeedCard(alert = alert, onClick = { onAlertClick(alert.id) })
                }
                item { AlertsFooter() }
            }
        }
    }
}

private fun hasAlertLocationPermission(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
private suspend fun currentAlertLocationOrNull(context: Context): Location? {
    val client = LocationServices.getFusedLocationProviderClient(context)
    return runCatching {
        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await()
            ?: client.lastLocation.await()
    }.getOrNull()
}

private fun kotlinx.coroutines.CoroutineScope.launchAlertLocationUpdate(
    context: Context,
    viewModel: AlertViewModel
) {
    launch {
        currentAlertLocationOrNull(context)?.let { location ->
            viewModel.updateUserLocation(location.latitude, location.longitude)
        }
    }
}

@Composable
private fun AlertsHeader() {
    val language = LocalAppLanguage.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tr("Real-time Alert Feed", language),
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                color = ForestPrimary
            )
            Box(contentAlignment = Alignment.Center) {
                Surface(modifier = Modifier.size(20.dp), shape = CircleShape, color = ErrorRed.copy(alpha = 0.16f)) {}
                Surface(modifier = Modifier.size(12.dp), shape = CircleShape, color = ErrorRed) {}
            }
        }
        Text(
            text = tr("Active sightings from the last 6 hours. Stay informed and ensure the safety of both wildlife and community.", language),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun AlertFeedCard(alert: CommunityAlert, onClick: () -> Unit) {
    val language = LocalAppLanguage.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceIvory.copy(alpha = 0.72f)),
        border = BorderStroke(1.dp, ForestPrimary.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(192.dp)) {
                Image(
                    painter = painterResource(id = alertAnimalDrawableRes(alert)),
                    contentDescription = alert.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.18f))))
                )
                if (alert.severity.equals("High", ignoreCase = true)) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                        shape = CircleShape,
                        color = ErrorRed
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                            Text(tr("URGENT", language), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = alertTitle(alert, language),
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestPrimary
                    )
                    Text(
                        text = relativeTimeText(alert.reportedAt, language),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp), tint = EarthSecondary)
                    Text(alert.location, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, color = EarthSecondary)
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceLow,
                    border = BorderStroke(1.dp, OutlineSoft.copy(alpha = 0.30f))
                ) {
                    Text(
                        text = alertDescription(alert, language),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyAlertsCard() {
    val language = LocalAppLanguage.current
    Surface(
        modifier = Modifier.fillMaxWidth().height(260.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceLow,
        border = BorderStroke(1.dp, OutlineSoft.copy(alpha = 0.30f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.NotificationsActive, contentDescription = null, modifier = Modifier.size(44.dp), tint = ForestContainer)
            Spacer(Modifier.height(12.dp))
            Text(tr("No active alerts in your area", language), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = ForestPrimary)
            Text(tr("The feed will update as verified sightings arrive.", language), fontSize = 14.sp, color = TextMuted)
        }
    }
}

@Composable
private fun AlertsFooter() {
    val language = LocalAppLanguage.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Filled.Update, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color(0xFF3C3C3A))
        Text(
            text = tr("Alerts are automatically removed after 6 hours to ensure feed relevance.", language),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontStyle = FontStyle.Italic,
            color = Color(0xFFA8A6A4)
        )
    }
}

private fun Long.relativeTime(): String {
    val elapsed = (System.currentTimeMillis() - this).coerceAtLeast(0L)
    val hours = elapsed / 3_600_000L
    val minutes = (elapsed % 3_600_000L) / 60_000L
    return when {
        hours >= 1L -> "${hours} hours ago"
        minutes >= 1L -> "${minutes} min ago"
        else -> "Just now"
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun AlertsScreenPreview() {
    AlertFeedCard(
        alert = CommunityAlert(
            id = "1",
            title = "Elephant Movement",
            type = AlertType.Sighting,
            location = "Bandipur Buffer Zone",
            description = "A herd of four elephants, including a calf, was sighted crossing the highway towards the forest edge.",
            reportedAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000L,
            priority = AlertPriority.High,
            animalType = "Elephant",
            severity = "High",
            reporter = "Forest Ranger",
            expiresAt = System.currentTimeMillis() + 2 * 60 * 60 * 1000L
        ),
        onClick = {}
    )
}
