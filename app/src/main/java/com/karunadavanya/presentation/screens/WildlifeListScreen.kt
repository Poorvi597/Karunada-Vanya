package com.karunadavanya.presentation.screens

import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.karunadavanya.R
import com.karunadavanya.domain.WildlifeCategory
import com.karunadavanya.presentation.localization.LocalAppLanguage
import com.karunadavanya.presentation.localization.localized
import com.karunadavanya.presentation.localization.tr
import com.karunadavanya.presentation.WikiViewModel
import com.karunadavanya.presentation.WildlifeListItem

private val ForestPrimary = Color(0xFF012D1D)
private val ForestContainer = Color(0xFF1B4332)
private val SurfaceIvory = Color(0xFFFFF8F3)
private val SurfaceContainer = Color(0xFFF4EDE6)
private val SurfaceHigh = Color(0xFFEEE7E1)
private val EarthSecondary = Color(0xFF805533)
private val OutlineSoft = Color(0xFFC1C8C2)
private val TextMuted = Color(0xFF414844)

@Composable
fun WildlifeListScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onAlerts: () -> Unit,
    onReport: () -> Unit,
    onWildlifeClick: (Int) -> Unit,
    viewModel: WikiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val mediaPlayerHolder = remember { arrayOfNulls<MediaPlayer>(1) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerHolder[0]?.release()
            mediaPlayerHolder[0] = null
        }
    }

    Scaffold(
        containerColor = SurfaceIvory,
        topBar = { KarunadaTopBar() },
        bottomBar = {
            KarunadaBottomNav(
                selected = "Explore",
                onHome = onHome,
                onExploreWildlife = {},
                onOpenAlerts = onAlerts,
                onOpenReport = onReport
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ExploreHeader(onBack = onBack)
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        ExploreChip(
                            label = tr("All", language),
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) }
                        )
                    }
                    items(WildlifeCategory.entries) { category ->
                        ExploreChip(
                            label = tr(category.label, language),
                            selected = selectedCategory == category,
                            onClick = { viewModel.selectCategory(category) }
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(360.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ForestPrimary)
                    }
                }
            } else {
                val expanded = uiState.species.firstOrNull()?.localized(language)
                if (expanded != null) {
                    item {
                        ExpandedWildlifeCard(
                            species = expanded,
                            onPlaySound = expanded.soundResId?.let { soundResId ->
                                {
                                    mediaPlayerHolder[0]?.release()
                                    mediaPlayerHolder[0] = null

                                    val player = runCatching { MediaPlayer.create(context, soundResId) }.getOrNull()
                                        ?: runCatching { MediaPlayer.create(context, R.raw.forest_sound) }.getOrNull()

                                    mediaPlayerHolder[0] = player
                                    player?.setOnCompletionListener { completedPlayer ->
                                        completedPlayer.release()
                                        if (mediaPlayerHolder[0] === completedPlayer) {
                                            mediaPlayerHolder[0] = null
                                        }
                                    }
                                    player?.setOnErrorListener { errorPlayer, _, _ ->
                                        errorPlayer.release()
                                        if (mediaPlayerHolder[0] === errorPlayer) {
                                            mediaPlayerHolder[0] = null
                                        }
                                        true
                                    }
                                    runCatching { player?.start() }
                                }
                            },
                            onClick = { onWildlifeClick(expanded.id) }
                        )
                    }
                }
                items(uiState.species.drop(1).map { it.localized(language) }, key = { it.id }) { species ->
                    CompactWildlifeCard(
                        species = species,
                        onClick = { onWildlifeClick(species.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExploreHeader(onBack: () -> Unit) {
    val language = LocalAppLanguage.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = tr("Explore Wildlife", language),
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                color = ForestPrimary
            )
            Text(
                text = tr("The heart of Karnataka's forests", language),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = TextMuted
            )
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = tr("Offline-Ready", language),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp,
                color = EarthSecondary
            )
            Switch(
                checked = true,
                onCheckedChange = { onBack() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFFDC39A)
                )
            )
        }
    }
}

@Composable
private fun ExploreChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = SurfaceContainer,
            labelColor = TextMuted,
            selectedContainerColor = ForestContainer,
            selectedLabelColor = Color(0xFFC1ECD4)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = OutlineSoft.copy(alpha = 0.45f),
            selectedBorderColor = ForestContainer
        )
    )
}

@Composable
private fun ExpandedWildlifeCard(
    species: WildlifeListItem,
    onPlaySound: (() -> Unit)?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
        border = BorderStroke(1.dp, OutlineSoft.copy(alpha = 0.30f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
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
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.42f), Color.Black.copy(alpha = 0.84f)),
                            startY = 180f
                        )
                    )
            )
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 222.dp)
                    .background(Color.Black.copy(alpha = 0.25f), CircleShape)
            ) {
                Icon(Icons.Filled.ExpandLess, contentDescription = "Open", tint = Color.White)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(Modifier.weight(1f)) {
                        Surface(shape = CircleShape, color = ForestContainer.copy(alpha = 0.84f)) {
                            Text(
                                text = species.category,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color(0xFF86AF99),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(species.name, fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            species.scientificName,
                            fontSize = 14.sp,
                            color = Color(0xFFE7E5E4),
                            fontStyle = FontStyle.Italic
                        )
                    }
                    if (onPlaySound != null) {
                        Surface(
                            modifier = Modifier
                                .size(56.dp)
                                .clickable(onClick = onPlaySound),
                            shape = CircleShape,
                            color = ForestPrimary,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.20f)),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.VolumeUp, contentDescription = "Sound", modifier = Modifier.size(28.dp), tint = Color.White)
                            }
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.72f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.30f))
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = species.description,
                            color = ForestPrimary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (onPlaySound != null) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clickable(onClick = onPlaySound),
                                shape = RoundedCornerShape(12.dp),
                                color = ForestPrimary
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.GraphicEq, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                                    Spacer(Modifier.width(8.dp))
                                    Text(tr("Play Forest Sound", LocalAppLanguage.current), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactWildlifeCard(species: WildlifeListItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
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
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.68f))))
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(Modifier.weight(1f)) {
                Text(species.name, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(
                    species.scientificName,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFE7E5E4),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(Icons.Filled.NorthEast, contentDescription = "Open", tint = Color.White)
        }
    }
}
