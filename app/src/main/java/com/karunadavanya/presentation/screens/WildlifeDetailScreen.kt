package com.karunadavanya.presentation.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.karunadavanya.R
import com.karunadavanya.presentation.localization.LocalAppLanguage
import com.karunadavanya.presentation.localization.localized
import com.karunadavanya.presentation.localization.tr
import com.karunadavanya.presentation.WildlifeDetailItem
import com.karunadavanya.presentation.WildlifeDetailUiState
import com.karunadavanya.presentation.WikiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WildlifeDetailScreen(
    wildlifeId: Int,
    onBack: () -> Unit,
    viewModel: WikiViewModel = hiltViewModel()
) {
    val detailState by remember(wildlifeId) { viewModel.detailState(wildlifeId) }.collectAsState()
    val language = LocalAppLanguage.current
    val species = detailState.species?.localized(language)
    val context = LocalContext.current
    val mediaPlayerHolder = remember { arrayOfNulls<MediaPlayer>(1) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerHolder[0]?.release()
            mediaPlayerHolder[0] = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (species == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Image Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Image(
                        painter = painterResource(id = species.imageResId),
                        contentDescription = species.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                    startY = 400f
                                )
                            )
                    )
                }
            }

            // Info Content
            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = species.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = species.scientificName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    species.soundResId?.let { soundResId ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                mediaPlayerHolder[0]?.release()
                                mediaPlayerHolder[0] = null
                                try {
                                    val mediaPlayer = MediaPlayer.create(context, soundResId)
                                    mediaPlayerHolder[0] = mediaPlayer
                                    mediaPlayer.setOnCompletionListener {
                                        it.release()
                                        if (mediaPlayerHolder[0] === it) {
                                            mediaPlayerHolder[0] = null
                                        }
                                    }
                                    mediaPlayer.setOnErrorListener { erroredPlayer, _, _ ->
                                        erroredPlayer.release()
                                        if (mediaPlayerHolder[0] === erroredPlayer) {
                                            mediaPlayerHolder[0] = null
                                        }
                                        true
                                    }
                                    mediaPlayer.start()
                                } catch (_: Exception) {
                                    mediaPlayerHolder[0]?.release()
                                    mediaPlayerHolder[0] = null
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(tr("Play Animal Sound", language))
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    DetailBlock(tr("Description", language), species.description)
                    DetailBlock(tr("Habitat", language), species.habitat)
                    
                    // Conservation Status
                    Text(tr("Conservation Status", language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = getStatusColor(species.conservationStatus), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = species.conservationStatus,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = getStatusColor(species.conservationStatus)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    // Locations in Karnataka
                    Text(tr("Found in Karnataka", language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    species.locations.forEach { location ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = location, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Fun Fact Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(tr("Fun Fact", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                            Spacer(Modifier.height(8.dp))
                            Text(species.funFact, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Gemini Fact Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(tr("Did you know? (Powered by Gemini)", language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.height(8.dp))
                            Text(detailState.geminiFact, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailBlock(title: String, body: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, lineHeight = 24.sp)
    }
}

private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "critically endangered" -> Color(0xFFD32F2F)
        "endangered" -> Color(0xFFE64A19)
        "vulnerable" -> Color(0xFFF57C00)
        "near threatened" -> Color(0xFFFBC02D)
        "least concern" -> Color(0xFF388E3C)
        else -> Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
fun WildlifeDetailScreenPreview() {
    WildlifeDetailScreenPreviewContent(
        species = WildlifeDetailItem(
            id = 1,
            name = "Bengal Tiger",
            imageResId = R.drawable.tiger,
            description = "A powerful apex predator.",
            habitat = "Deciduous forests and grasslands.",
            conservationStatus = "Endangered",
            funFact = "Each tiger has a unique stripe pattern.",
            rawId = "bengal_tiger",
            soundResId = R.raw.tiger,
            scientificName = "Panthera tigris tigris",
            locations = listOf("Bandipur", "Nagarhole")
        ),
        detailState = WildlifeDetailUiState(geminiFact = "Placeholder fact")
    )
}

@Composable
private fun WildlifeDetailScreenPreviewContent(species: WildlifeDetailItem, detailState: WildlifeDetailUiState) {
     // Content for preview
}
