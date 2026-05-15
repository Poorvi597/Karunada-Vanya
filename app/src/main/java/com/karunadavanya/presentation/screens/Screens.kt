package com.karunadavanya.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.karunadavanya.R
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.domain.Wildlife
import com.karunadavanya.domain.WildlifeCategory
import com.karunadavanya.presentation.AlertDetailViewModel
import com.karunadavanya.presentation.AlertsViewModel
import com.karunadavanya.presentation.ReportViewModel
import com.karunadavanya.presentation.WildlifeDetailViewModel
import com.karunadavanya.presentation.WildlifeViewModel
import com.karunadavanya.presentation.localization.KannadaLanguageToggle
import com.karunadavanya.presentation.localization.LocalAppLanguage
import com.karunadavanya.presentation.localization.speciesName
import com.karunadavanya.presentation.localization.tr
import com.karunadavanya.util.AnimalSoundPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KarunadaTopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFFFAFAF9).copy(alpha = 0.88f),
        border = BorderStroke(1.dp, Color(0xFFE7E5E4).copy(alpha = 0.50f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(imageVector = Icons.Filled.Park, contentDescription = null, tint = Color(0xFF064E3B))
                Text(text = "Karunada Vanya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF064E3B))
            }
            KannadaLanguageToggle()
        }
    }
}

@Composable
fun KarunadaBottomNav(
    selected: String,
    onHome: () -> Unit,
    onExploreWildlife: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenReport: () -> Unit
) {
    val language = LocalAppLanguage.current
    Surface(modifier = Modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), color = Color(0xFFFAFAF9)) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            BottomNavItem(tr("Home", language), Icons.Filled.Home, selected = selected == "Home", onClick = onHome)
            BottomNavItem(tr("Explore", language), Icons.Filled.TravelExplore, selected = selected == "Explore", onClick = onExploreWildlife)
            BottomNavItem(tr("Alerts", language), Icons.Filled.NotificationsActive, selected = selected == "Alerts", onClick = onOpenAlerts)
            BottomNavItem(tr("Report", language), Icons.Filled.Campaign, selected = selected == "Report", onClick = onOpenReport)
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = if (selected) Color(0xFF012D1D) else Color.Gray)
        Text(text = label, fontSize = 12.sp, color = if (selected) Color(0xFF012D1D) else Color.Gray)
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val language = LocalAppLanguage.current
    LaunchedEffect(Unit) {
        delay(2_500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF012D1D))
    ) {
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBg0BuFsAWLHFXM3L9aFf5PL_Fep1Fa1EZazrMX6uGrhufQrBj2y0zfC-UQRkF-dkrkDhVqPlUhNsNumJTLf0faT8JshDvITGxxYKuRTE0bPuATldUW2J_WCXFQxbGVCBZCUDOfFLqT4xiiP1xrJMh85PxLFi8figr1O2Bsra0tD2qzF0zs6lptfosocq_vJi8yFFq_vjOCmdMLmMIwRtLA2odxbxhvLKwSVKTAmUaewT4AwCAXuY9S0YO1hzvM2Su2HZjVMapylQ",
            contentDescription = "Karunada Vanya Forest Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF012D1D).copy(alpha = 0.40f),
                            0.52f to Color.Transparent,
                            1.0f to Color(0xFF012D1D).copy(alpha = 0.80f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .size(128.dp)
                    .shadow(24.dp, CircleShape)
                    .clip(CircleShape)
                    .blur(0.2.dp),
                shape = CircleShape,
                color = Color(0xFFFFFFFF).copy(alpha = 0.20f),
                tonalElevation = 0.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFE8E1DB).copy(alpha = 0.30f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Park,
                        contentDescription = "Park",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Karunada Vanya",
                fontSize = 32.sp,
                lineHeight = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = tr("NATIONAL PRIDE", language),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFFC1ECD4),
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = tr("\"Guardians of the Western Ghats, Stewards of the Wild.\"", language),
                modifier = Modifier.fillMaxWidth(0.78f),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.White.copy(alpha = 0.80f),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.20f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = tr("COMMUNING WITH NATURE...", language),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color.White.copy(alpha = 0.60f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = tr("Government of Karnataka - Wildlife Division", language),
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.30f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.6.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onExploreWildlife: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenReport: () -> Unit
) {
    val language = LocalAppLanguage.current
    Scaffold(
        containerColor = Color(0xFFFFF8F3),
        topBar = { HomeTopBar() },
        bottomBar = {
            HomeBottomNav(
                onHome = {},
                onExploreWildlife = onExploreWildlife,
                onOpenAlerts = onOpenAlerts,
                onOpenReport = onOpenReport
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item { HomeHero() }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .offset(y = (-48).dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeFeatureCard(
                        title = tr("Wildlife Wonder", language),
                        description = tr("Explore interactive educational modules about Karnataka's native fauna and habitat corridors.", language),
                        icon = Icons.Filled.Pets,
                        containerColor = Color(0xFFF4EDE6),
                        iconBackground = Color(0xFF1B4332).copy(alpha = 0.10f),
                        titleColor = Color(0xFF012D1D),
                        descriptionColor = Color(0xFF414844),
                        arrowColor = Color(0xFF717973),
                        onClick = onExploreWildlife
                    )
                    HomeFeatureCard(
                        title = tr("Community Safety", language),
                        description = tr("Real-time alerts for local wildlife movements to ensure peaceful coexistence.", language),
                        icon = Icons.Filled.NotificationsActive,
                        containerColor = Color(0xFFFFDCC5),
                        iconBackground = Color(0xFFFDC39A),
                        titleColor = Color(0xFF794E2E),
                        descriptionColor = Color(0xFF653D1E),
                        arrowColor = Color(0x99653D1E),
                        onClick = onOpenAlerts
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFFFAFAF9).copy(alpha = 0.88f),
        border = BorderStroke(1.dp, Color(0xFFE7E5E4).copy(alpha = 0.50f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Filled.Park, contentDescription = null, tint = Color(0xFF064E3B))
                Text(
                    text = "Karunada Vanya",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF064E3B)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KannadaLanguageToggle()
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = Color(0xFFEEE7E1),
                    border = BorderStroke(1.dp, Color(0xFFC1C8C2))
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwVQcZ651ZXlaIezWycomR8ST7UT6ZZUcqpqhGZQT-9Kayf5aFpLXUEJ_oc1-JKj7NecsOf99yLrOx2za6GWSxtCL5JmsIQC8hM_E79voYms-3GwyCUwqrgDRQdJu2_ucBDYHxJaNuNF6Dktg2H4TCdc78INeleoZAirFxu5KiXswV0mHMO-MXG83NV6-0FfK-ttTjCoWq5e_5Aoi-XYHnAZDIyrw3qtsN4nu_Nq54kV3f2cpKQq8Pzsu1-koXAMQ5C4QWN0lO_w",
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeBottomNav(
    onHome: () -> Unit,
    onExploreWildlife: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenReport: () -> Unit
) {
    val language = LocalAppLanguage.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color(0xFFFAFAF9).copy(alpha = 0.94f),
        shadowElevation = 12.dp,
        border = BorderStroke(1.dp, Color(0xFFE7E5E4).copy(alpha = 0.50f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeBottomNavItem(tr("Home", language), Icons.Filled.Home, selected = true, onClick = onHome)
            HomeBottomNavItem(tr("Explore", language), Icons.Filled.TravelExplore, selected = false, onClick = onExploreWildlife)
            HomeBottomNavItem(tr("Alerts", language), Icons.Filled.NotificationsActive, selected = false, onClick = onOpenAlerts)
            HomeBottomNavItem(tr("Report", language), Icons.Filled.Campaign, selected = false, onClick = onOpenReport)
        }
    }
}

@Composable
private fun HomeBottomNavItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val contentColor = if (selected) Color(0xFF064E3B) else Color(0xFF78716C)
    val backgroundColor = if (selected) Color(0xFFD1FAE5).copy(alpha = 0.50f) else Color.Transparent
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, tint = contentColor)
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = contentColor)
    }
}

@Composable
private fun HomeHero() {
    val language = LocalAppLanguage.current
    Box(modifier = Modifier.fillMaxWidth().height(530.dp)) {
        Image(
            painter = painterResource(id = R.drawable.tiger),
            contentDescription = "A majestic Bengal tiger lying in the golden tall grass of Bandipur National Park",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0xFF012D1D).copy(alpha = 0.20f),
                            Color(0xFF012D1D).copy(alpha = 0.80f)
                        )
                    )
                )
        )
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(32.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFAFAF9).copy(alpha = 0.10f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.20f)),
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1B4332)
                ) {
                    Text(
                        text = tr("Live Conservation", language),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFF86AF99),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = tr("Preserving Karnataka's Natural Heritage", language),
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = tr("Experience the untamed beauty of our forests while contributing to vital local species protection programs.", language),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.White.copy(alpha = 0.90f)
                )
            }
        }
    }
}

@Composable
private fun HomeFeatureCard(title: String, description: String, icon: ImageVector, containerColor: Color, iconBackground: Color, titleColor: Color, descriptionColor: Color, arrowColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(232.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color(0xFFC1C8C2).copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            Surface(
                modifier = Modifier.align(Alignment.TopStart),
                shape = RoundedCornerShape(16.dp),
                color = iconBackground
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp).size(40.dp),
                    tint = titleColor
                )
            }
            Icon(
                Icons.Filled.NorthEast,
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd),
                tint = arrowColor
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = title, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold, color = titleColor)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = descriptionColor,
                    modifier = Modifier.fillMaxWidth(0.86f)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 44.dp, y = 44.dp)
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(titleColor.copy(alpha = 0.05f))
                    .blur(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel, onHome: () -> Unit, onExplore: () -> Unit, onAlerts: () -> Unit) {
    val formState by viewModel.formState.collectAsState()
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var submitAfterLocationPermission by remember { mutableStateOf(false) }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    fun fetchLocationThenSubmit() {
        scope.launch {
            val location = currentLocationOrNull(context)
            viewModel.updateCoordinates(location?.latitude, location?.longitude)
            if (location != null) {
                viewModel.updateLocation(resolveReadableLocation(context, location.latitude, location.longitude))
            }
            viewModel.submit()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (submitAfterLocationPermission) {
            submitAfterLocationPermission = false
            if (granted) {
                fetchLocationThenSubmit()
            } else {
                viewModel.submit()
            }
        } else if (granted) {
            scope.launch {
                val location = currentLocationOrNull(context)
                viewModel.updateCoordinates(location?.latitude, location?.longitude)
                viewModel.updateLocation(
                    if (location != null) {
                        resolveReadableLocation(context, location.latitude, location.longitude)
                    } else {
                        "Near Karnataka Forest Range"
                    }
                )
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFFFF8F3),
        topBar = { KarunadaTopBar() },
        bottomBar = { KarunadaBottomNav(selected = "Report", onHome = onHome, onExploreWildlife = onExplore, onOpenAlerts = onAlerts, onOpenReport = {}) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFFF8F3)),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tr("Report Sighting", language), fontSize = 34.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF012D1D))
                    Text(
                        tr("Share verified details so nearby communities and forest teams can respond quickly.", language),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF414844)
                    )
                }
            }

            item {
                ReportPanel {
                    LabeledReportField(tr("Species", language)) {
                        AnimalTypeDropdown(selectedAnimal = formState.species, onAnimalSelected = viewModel::updateSpecies)
                    }
                    LabeledReportField(tr("Description", language)) {
                        OutlinedTextField(
                            value = formState.description,
                            onValueChange = viewModel::updateDescription,
                            modifier = Modifier.fillMaxWidth().height(116.dp),
                            placeholder = { Text(tr("Describe what you saw, group size, direction, and urgency...", language)) },
                            minLines = 3,
                            maxLines = 4,
                            shape = RoundedCornerShape(4.dp)
                        )
                    }
                    LabeledReportField(tr("Landmark", language)) {
                        ReportLocationMap(
                            currentLocation = formState.locationDescription,
                            onUseCurrentLocation = {
                                if (hasLocationPermission()) {
                                    scope.launch {
                                        val location = currentLocationOrNull(context)
                                        viewModel.updateCoordinates(location?.latitude, location?.longitude)
                                        viewModel.updateLocation(
                                            if (location != null) {
                                                resolveReadableLocation(context, location.latitude, location.longitude)
                                            } else {
                                                tr("Near Karnataka Forest Range", language)
                                            }
                                        )
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
                        )
                        OutlinedTextField(
                            value = formState.locationDescription,
                            onValueChange = viewModel::updateLocation,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(tr("Or enter nearest landmark...", language)) },
                            singleLine = true,
                            shape = RoundedCornerShape(4.dp)
                        )
                    }
                    LabeledReportField(tr("Date and Time", language)) {
                        DateTimePickerField(
                            date = formState.date,
                            time = formState.time,
                            onTimestampSelected = viewModel::updateTimestamp
                        )
                    }
                    ReportSubmitButton(
                        enabled = formState.canSubmit,
                        onClick = {
                            if (hasLocationPermission()) {
                                fetchLocationThenSubmit()
                            } else {
                                submitAfterLocationPermission = true
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        }
                    )
                    formState.submittedMessage?.let { message ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = if (message.contains("posted", ignoreCase = true)) Color(0xFFC1ECD4) else Color(0xFFFFDAD6)
                        ) {
                            Text(
                                text = tr(message, language),
                                modifier = Modifier.padding(14.dp),
                                color = if (message.contains("posted", ignoreCase = true)) Color(0xFF012D1D) else MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                CoexistenceGuidePanel()
            }

            item {
                RespectSpaceReportCard()
            }

            item {
                EssentialSafetyQuickTips()
            }
        }
    }
}

@SuppressLint("MissingPermission")
private suspend fun currentLocationOrNull(context: Context): Location? {
    val client = LocationServices.getFusedLocationProviderClient(context)
    return runCatching {
        client.lastLocation.await()
            ?: client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            ?: client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await()
    }.getOrNull()
}

private suspend fun resolveReadableLocation(context: Context, latitude: Double, longitude: Double): String {
    return reverseGeocodeAddress(context, latitude, longitude) ?: "Near Karnataka Forest Range"
}

private suspend fun reverseGeocodeAddress(context: Context, latitude: Double, longitude: Double): String? {
    return withContext(Dispatchers.IO) {
        var attempts = 0
        while (attempts < 3) {
            attempts++
            try {
                @Suppress("DEPRECATION")
                val address = Geocoder(context, Locale.getDefault())
                    .getFromLocation(latitude, longitude, 1)
                    ?.firstOrNull()

                if (address != null) {
                    val areaAddress = listOf(
                        address.subLocality.orEmpty(),
                        address.locality.orEmpty(),
                        address.adminArea.orEmpty()
                    )
                        .filter { it.isNotBlank() }
                        .distinct()
                        .joinToString(", ")
                        .ifBlank { address.getAddressLine(0).orEmpty() }

                    if (areaAddress.isNotBlank()) return@withContext areaAddress
                }
            } catch (_: Exception) {
                // Retry before falling back to the forest range label.
            }
        }
        null
    }
}

@Composable
private fun ReportHeroCard() {
    val language = LocalAppLanguage.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.elephant),
            contentDescription = "Elephant near forest edge",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFF012D1D).copy(alpha = 0.86f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                tr("Community Response", language),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFFC1ECD4),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )
            Text(
                tr("Report with care. Keep distance. Help everyone stay safe.", language),
                fontSize = 22.sp,
                lineHeight = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ReportPanel(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFC1C8C2).copy(alpha = 0.30f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            content = content
        )
    }
}

@Composable
private fun LabeledReportField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF012D1D))
        content()
    }
}

@Composable
private fun ReportLocationMap(currentLocation: String, onUseCurrentLocation: () -> Unit) {
    val language = LocalAppLanguage.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE8E1DB))
    ) {
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCFN2SYxK4kJfesNLr-yGXbvadvHRoEcBS_YU-0sDgCz0RVdxkTWe9h6vwHh1lOOhWKONSIKSVHfW94JLi8QbmD1wIk8yXhQV3Po3KtuYUOWBHkHPlodx1AB1T3R0cXWI1-L41P56eoFNZajIsFwBtMt8T-0amLndPt0ZE2p_uT_HtWBPhsFTwt0hAYF_-pHCW8VmScCuIeRb3O3GG5Ptice-p0BAp2aXVh-amz9IiB8hLuoD1F6mNYqErnSpqpRaXt2pPvfSQTPQ",
            contentDescription = "Location Map",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = CircleShape,
            color = Color(0xFF012D1D),
            shadowElevation = 8.dp
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = Color.White
            )
        }
        currentLocation.takeIf { it.isNotBlank() }?.let { location ->
            Surface(
                modifier = Modifier.align(Alignment.TopStart).padding(14.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFF8F3).copy(alpha = 0.90f),
                border = BorderStroke(1.dp, Color(0xFFC1C8C2).copy(alpha = 0.50f))
            ) {
                Text(
                    text = location,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = Color(0xFF414844),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clickable(onClick = onUseCurrentLocation),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFF8F3).copy(alpha = 0.92f),
            border = BorderStroke(1.dp, Color(0xFFC1C8C2).copy(alpha = 0.50f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.MyLocation, contentDescription = null, tint = Color(0xFF414844), modifier = Modifier.size(18.dp))
                Text(tr("Use Current Location", language), color = Color(0xFF414844), fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun ReportSubmitButton(enabled: Boolean, onClick: () -> Unit) {
    val language = LocalAppLanguage.current
    val background = if (enabled) Color(0xFF012D1D) else Color(0xFF717973)
    Surface(
        modifier = Modifier.fillMaxWidth().height(56.dp).clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = background
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Send, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(10.dp))
            Text(tr("Submit Sighting Report", language), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CommunitySafetyGuidelines() {
    val language = LocalAppLanguage.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(tr("Community Safety Guidelines", language), fontSize = 22.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF012D1D))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1B4332)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SafetyGuidelineCard(Icons.Filled.Visibility, tr("Observe From Distance", language), tr("Do not approach, feed, surround, photograph with flash, or block the animal's path.", language))
                SafetyGuidelineCard(Icons.Filled.Groups, tr("Move As A Group", language), tr("Keep children and livestock indoors, avoid isolated travel, and alert nearby residents calmly.", language))
                SafetyGuidelineCard(Icons.Filled.Call, tr("Call Forest Support", language), tr("For immediate danger, contact the forest helpline or local authorities with landmark and species details.", language))
            }
        }
    }
}

@Composable
private fun SafetyGuidelineCard(icon: ImageVector, title: String, body: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8F3).copy(alpha = 0.10f),
        border = BorderStroke(1.dp, Color(0xFF86AF99).copy(alpha = 0.22f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF012D1D)) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp).size(20.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF86AF99))
                Text(body, fontSize = 13.sp, lineHeight = 19.sp, color = Color(0xFF86AF99).copy(alpha = 0.90f))
            }
        }
    }
}

@Composable
private fun EmergencyContactPanel() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Helpline & Emergency", fontSize = 22.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF012D1D))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EmergencyContactCard(
                title = "Forest Helpline",
                value = "1926",
                icon = Icons.Filled.SupportAgent,
                modifier = Modifier.weight(1f)
            )
            EmergencyContactCard(
                title = "Emergency",
                value = "112",
                icon = Icons.Filled.Emergency,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun EmergencyContactCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(132.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF4EDE6),
        border = BorderStroke(1.dp, Color(0xFFC1C8C2).copy(alpha = 0.35f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(shape = CircleShape, color = Color(0xFFFFDCC5)) {
                Icon(icon, contentDescription = null, tint = Color(0xFF805533), modifier = Modifier.padding(8.dp).size(26.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, lineHeight = 16.sp, color = Color(0xFF414844), fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
            Text(value, fontSize = 22.sp, lineHeight = 28.sp, color = Color(0xFF012D1D), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CoexistenceGuidePanel() {
    val language = LocalAppLanguage.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1B4332)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF012D1D)) {
                    Icon(Icons.Filled.Security, contentDescription = null, modifier = Modifier.padding(8.dp), tint = Color.White)
                }
                Text(tr("Co-existence Guide", language), fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF86AF99))
            }
            GuideTip(tr("Elephant in your field?", language), listOf(tr("Maintain a distance of at least 50-100 meters.", language), tr("Avoid using bright camera flashes or loud honking.", language), tr("Check for calves; mothers are extremely protective.", language)))
            GuideTip(tr("Predator Encounter (Tiger/Leopard)", language), listOf(tr("Do not run. Back away slowly while facing the animal.", language), tr("Make yourself look larger by raising your arms.", language), tr("Make noise in a calm, firm voice; do not scream.", language)))
        }
    }
}

@Composable
private fun GuideTip(title: String, bullets: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8F3).copy(alpha = 0.10f),
        border = BorderStroke(1.dp, Color(0xFF86AF99).copy(alpha = 0.20f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF86AF99))
            bullets.forEach {
                Text("- $it", fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF86AF99).copy(alpha = 0.90f))
            }
        }
    }
}

@Composable
private fun RespectSpaceReportCard() {
    val language = LocalAppLanguage.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.elephant),
            contentDescription = "Elephant in habitat",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF012D1D).copy(alpha = 0.82f))))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = tr("Local Awareness", language),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color.White.copy(alpha = 0.80f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.4.sp
            )
            Text(
                text = tr("Respect Their Space", language),
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun EssentialSafetyQuickTips() {
    val language = LocalAppLanguage.current
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFC1C8C2).copy(alpha = 0.30f)))
        Text(tr("Essential Safety Quick-Tips", language), fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF012D1D))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickTipTile(tr("Dim Lights", language), Icons.Filled.FlashlightOff, Modifier.weight(1f))
            QuickTipTile(tr("Keep Quiet", language), Icons.Filled.VolumeOff, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickTipTile(tr("Stay in Groups", language), Icons.Filled.Groups, Modifier.weight(1f))
            QuickTipTile(tr("Helpline: 1926", language), Icons.Filled.Call, Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickTipTile(label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF4EDE6)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFF805533))
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E1B17), textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimalTypeDropdown(selectedAnimal: String, onAnimalSelected: (String) -> Unit) {
    val language = LocalAppLanguage.current
    val animalTypes = listOf(
        "Elephant",
        "Tiger",
        "Leopard",
        "Wild Boar",
        "Gaur (Indian Bison)",
        "Sloth Bear"
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = speciesName(selectedAnimal, language),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
            leadingIcon = selectedAnimal.takeIf { it.isNotBlank() }?.let { animal ->
                {
                    Image(
                        painter = painterResource(id = animalDrawableRes(animal)),
                        contentDescription = speciesName(animal, language),
                        modifier = Modifier.size(32.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            placeholder = { Text(tr("Select species", language)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            animalTypes.forEach { animal ->
                DropdownMenuItem(
                    text = { Text(speciesName(animal, language)) },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = animalDrawableRes(animal)),
                            contentDescription = speciesName(animal, language),
                            modifier = Modifier.size(40.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    },
                    onClick = {
                        onAnimalSelected(animal)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DateTimePickerField(
    date: String,
    time: String,
    onTimestampSelected: (date: String, time: String, timestamp: Long) -> Unit
) {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hour)
                                calendar.set(Calendar.MINUTE, minute)
                                calendar.set(Calendar.SECOND, 0)
                                calendar.set(Calendar.MILLISECOND, 0)
                                val timestamp = calendar.timeInMillis
                                onTimestampSelected(
                                    dateFormatter.format(Date(timestamp)),
                                    timeFormatter.format(Date(timestamp)),
                                    timestamp
                                )
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (date.isBlank() || time.isBlank()) tr("Pick date and time", language) else "$date  $time",
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(Icons.Filled.ExpandMore, contentDescription = "Pick date and time")
        }
    }
}
