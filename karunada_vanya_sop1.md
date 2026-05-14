

Karunada Vanya - Standard Operating Procedure (SOP)
Project Overview
Project Name: Karunada Vanya
Platform: Android
Development Environment: Android Studio
Programming Language: Kotlin
Target SDK: 34 (Android 14)
Minimum SDK: 24 (Android 7.0)

Table of Contents
Executive Summary
Technical Architecture
Feature Specifications
Screen-by-Screen Specifications
Database Schema
Firebase Configuration
Development Workflow
Implementation Guide
Testing Requirements
Deployment Guidelines
1. Executive Summary
1.1 Project Vision
Karunada Vanya is a wildlife conservation and awareness application designed to help users explore Karnataka's wildlife and receive real-time community alerts about wildlife sightings and incidents.

1.2 Core Features
Explore Wildlife: Offline-accessible wildlife encyclopedia with detailed species information
Community Alerts: Real-time firebase-powered alert system for wildlife sightings
Audio Playback: Species-specific sound samples
Offline-First Architecture: Complete wildlife data available without internet
1.3 Key Technologies
UI Framework: Jetpack Compose + MotionLayout
Local Database: Room Database
Real-time Data: Firebase Realtime Database / Firestore
Push Notifications: Firebase Cloud Messaging (FCM)
Media Playback: ExoPlayer / MediaPlayer
Image Loading: Coil
Dependency Injection: Hilt/Dagger
2. Technical Architecture
2.1 Application Architecture
text

┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │   Compose    │  │ MotionLayout │  │ ViewModels│ │
│  │   Screens    │  │   Animations │  │           │ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│                   Domain Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │  Use Cases   │  │  Repository  │  │  Models   │ │
│  │              │  │  Interfaces  │  │           │ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│                    Data Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │     Room     │  │   Firebase   │  │  Local    │ │
│  │   Database   │  │   Firestore  │  │  Storage  │ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────┘
2.2 Package Structure
text

com.karunadavanya
├── data
│   ├── local
│   │   ├── dao
│   │   │   ├── WildlifeDao.kt
│   │   │   └── AlertDao.kt
│   │   ├── database
│   │   │   └── KarunadaVanyaDatabase.kt
│   │   └── entities
│   │       ├── WildlifeEntity.kt
│   │       └── AlertEntity.kt
│   ├── remote
│   │   ├── firebase
│   │   │   ├── FirebaseAlertService.kt
│   │   │   └── FirebaseMessagingService.kt
│   │   └── dto
│   │       └── AlertDto.kt
│   └── repository
│       ├── WildlifeRepositoryImpl.kt
│       └── AlertRepositoryImpl.kt
├── domain
│   ├── model
│   │   ├── Wildlife.kt
│   │   ├── Alert.kt
│   │   └── Category.kt
│   ├── repository
│   │   ├── WildlifeRepository.kt
│   │   └── AlertRepository.kt
│   └── usecase
│       ├── GetWildlifeListUseCase.kt
│       ├── GetWildlifeDetailUseCase.kt
│       ├── GetAlertsUseCase.kt
│       └── PlaySoundUseCase.kt
├── presentation
│   ├── home
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── wildlife
│   │   ├── WildlifeListScreen.kt
│   │   ├── WildlifeDetailScreen.kt
│   │   ├── WildlifeViewModel.kt
│   │   └── components
│   │       └── WildlifeCard.kt
│   ├── alerts
│   │   ├── AlertsScreen.kt
│   │   ├── AlertDetailScreen.kt
│   │   └── AlertsViewModel.kt
│   ├── navigation
│   │   └── NavGraph.kt
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── di
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   ├── FirebaseModule.kt
│   └── RepositoryModule.kt
└── util
    ├── Constants.kt
    ├── Resource.kt
    └── AudioPlayer.kt
3. Feature Specifications
3.1 Home Screen
Purpose: Primary navigation hub

Components:

App branding/logo
Two navigation cards:
Explore Wildlife
Community Alerts
Navigation Flow:

text

Home Screen
├── Explore Wildlife → Wildlife List Screen
└── Community Alerts → Alerts List Screen
3.2 Explore Wildlife (Offline Encyclopedia)
Features:

Grid/List view of wildlife species
Category filtering (Mammals, Birds, Reptiles, etc.)
Search functionality
Offline-first data access
MotionLayout animations for card interactions
Data Storage:

Room Database with pre-populated data
Local audio file storage
Cached images
3.3 Community Alerts (Real-time System)
Features:

Real-time alert feed
Location-based alerts
Alert categories (Sighting, Conflict, Emergency)
Push notifications
User submission capability
Technology:

Firebase Firestore for data
Firebase Cloud Messaging for notifications
GeoFire for location queries
4. Screen-by-Screen Specifications
4.1 Splash Screen
File: SplashScreen.kt

Purpose: App initialization and branding

Duration: 2-3 seconds

Elements:

App logo (centered)
App name "Karunada Vanya"
Tagline: "Protecting Karnataka's Wildlife"
Technical Requirements:

Kotlin

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        delay(2500L)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Karunada Vanya",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Text(
                text = "Protecting Karnataka's Wildlife",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
4.2 Home Screen
File: HomeScreen.kt

Layout:

text

┌─────────────────────────────┐
│      ☰  Karunada Vanya      │
├─────────────────────────────┤
│                             │
│  ┌───────────────────────┐  │
│  │                       │  │
│  │   🦁                  │  │
│  │   Explore Wildlife    │  │
│  │                       │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │                       │  │
│  │   🔔                  │  │
│  │   Community Alerts    │  │
│  │                       │  │
│  └───────────────────────┘  │
│                             │
└─────────────────────────────┘
Implementation:

Kotlin

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Karunada Vanya") },
                navigationIcon = {
                    IconButton(onClick = { /* Open drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
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
            // Explore Wildlife Card
            NavigationCard(
                title = "Explore Wildlife",
                description = "Discover Karnataka's diverse wildlife species",
                icon = R.drawable.ic_wildlife,
                backgroundColor = Color(0xFF4CAF50),
                onClick = {
                    navController.navigate(Screen.WildlifeList.route)
                }
            )
            
            // Community Alerts Card
            NavigationCard(
                title = "Community Alerts",
                description = "Real-time wildlife sightings and alerts",
                icon = R.drawable.ic_alert,
                backgroundColor = Color(0xFFFF9800),
                onClick = {
                    navController.navigate(Screen.AlertsList.route)
                }
            )
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    description: String,
    icon: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
4.3 Wildlife List Screen
File: WildlifeListScreen.kt

Layout:

text

┌─────────────────────────────┐
│  ←  Wildlife Encyclopedia   │
├─────────────────────────────┤
│  🔍 Search wildlife...      │
├─────────────────────────────┤
│  [All] [Mammals] [Birds]... │
├─────────────────────────────┤
│  ┌───────┐  ┌───────┐       │
│  │ Tiger │  │Elephant│      │
│  │  🐅   │  │  🐘   │       │
│  └───────┘  └───────┘       │
│  ┌───────┐  ┌───────┐       │
│  │ Peacock│  │ Cobra │      │
│  │  🦚   │  │  🐍   │       │
│  └───────┘  └───────┘       │
└─────────────────────────────┘
MotionLayout Card Implementation:

Create wildlife_card_motion_scene.xml in res/xml/:

XML

<?xml version="1.0" encoding="utf-8"?>
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res-auto">

    <Transition
        motion:constraintSetStart="@id/start"
        motion:constraintSetEnd="@id/end"
        motion:duration="300">
        <OnClick
            motion:targetId="@id/wildlifeCard"
            motion:clickAction="toggle" />
    </Transition>

    <ConstraintSet android:id="@+id/start">
        <Constraint
            android:id="@+id/wildlifeCard"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            motion:layout_constraintTop_toTopOf="parent"
            android:elevation="2dp"
            android:scaleX="1.0"
            android:scaleY="1.0" />
    </ConstraintSet>

    <ConstraintSet android:id="@+id/end">
        <Constraint
            android:id="@+id/wildlifeCard"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            motion:layout_constraintTop_toTopOf="parent"
            android:elevation="8dp"
            android:scaleX="0.95"
            android:scaleY="0.95" />
    </ConstraintSet>
</MotionScene>
Kotlin Implementation:

Kotlin

@Composable
fun WildlifeListScreen(
    navController: NavController,
    viewModel: WildlifeViewModel = hiltViewModel()
) {
    val wildlifeList by viewModel.wildlifeList.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wildlife Encyclopedia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Category Filter
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.selectCategory(category) },
                        label = { Text(category.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wildlife Grid
            when (val state = wildlifeList) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data ?: emptyList()) { wildlife ->
                            WildlifeCard(
                                wildlife = wildlife,
                                onClick = {
                                    navController.navigate(
                                        Screen.WildlifeDetail.createRoute(wildlife.id)
                                    )
                                }
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    ErrorView(message = state.message ?: "Unknown error")
                }
            }
        }
    }
}

@Composable
fun WildlifeCard(
    wildlife: Wildlife,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .scale(if (isPressed) 0.95f else 1f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image
            AsyncImage(
                model = wildlife.imageUrl,
                contentDescription = wildlife.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            
            // Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {
                Text(
                    text = wildlife.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = wildlife.scientificName,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = getCategoryIcon(wildlife.category)),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = wildlife.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search wildlife...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}
ViewModel:

Kotlin

@HiltViewModel
class WildlifeViewModel @Inject constructor(
    private val getWildlifeListUseCase: GetWildlifeListUseCase,
    private val getWildlifeDetailUseCase: GetWildlifeDetailUseCase
) : ViewModel() {

    private val _wildlifeList = MutableStateFlow<Resource<List<Wildlife>>>(Resource.Loading())
    val wildlifeList: StateFlow<Resource<List<Wildlife>>> = _wildlifeList

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadWildlife()
        loadCategories()
    }

    private fun loadWildlife() {
        viewModelScope.launch {
            getWildlifeListUseCase().collect { result ->
                _wildlifeList.value = result
            }
        }
    }

    private fun loadCategories() {
        _categories.value = listOf(
            Category("All", "all"),
            Category("Mammals", "mammals"),
            Category("Birds", "birds"),
            Category("Reptiles", "reptiles"),
            Category("Amphibians", "amphibians"),
            Category("Insects", "insects")
        )
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
        filterWildlife()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterWildlife()
    }

    private fun filterWildlife() {
        viewModelScope.launch {
            getWildlifeListUseCase(
                category = _selectedCategory.value?.id,
                searchQuery = _searchQuery.value
            ).collect { result ->
                _wildlifeList.value = result
            }
        }
    }
}






4.4 Wildlife Detail Screen
File: WildlifeDetailScreen.kt

Layout:

text

┌─────────────────────────────┐
│  ←          ⋮               │
├─────────────────────────────┤
│                             │
│     [Wildlife Image]        │
│                             │
├─────────────────────────────┤
│  Bengal Tiger               │
│  Panthera tigris tigris     │
│                             │
│  🔊 Play Sound              │
├─────────────────────────────┤
│  Description:               │
│  The Bengal tiger is...     │
│                             │
│  Habitat:                   │
│  Tropical forests...        │
│                             │
│  Conservation Status:       │
│  🔴 Endangered              │
│                             │
│  Found in Karnataka:        │
│  • Bandipur National Park   │
│  • Nagarhole National Park  │
│                             │
└─────────────────────────────┘
Implementation:

Kotlin

@Composable
fun WildlifeDetailScreen(
    wildlifeId: String,
    navController: NavController,
    viewModel: WildlifeDetailViewModel = hiltViewModel()
) {
    val wildlife by viewModel.wildlife.collectAsState()
    val isPlayingSound by viewModel.isPlayingSound.collectAsState()

    LaunchedEffect(wildlifeId) {
        viewModel.loadWildlife(wildlifeId)
    }

    when (val state = wildlife) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            state.data?.let { wildlifeData ->
                WildlifeDetailContent(
                    wildlife = wildlifeData,
                    isPlayingSound = isPlayingSound,
                    onPlaySound = { viewModel.playSound() },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        is Resource.Error -> {
            ErrorView(
                message = state.message ?: "Unknown error",
                onRetry = { viewModel.loadWildlife(wildlifeId) }
            )
        }
    }
}

@Composable
fun WildlifeDetailContent(
    wildlife: Wildlife,
    isPlayingSound: Boolean,
    onPlaySound: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Hero Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = wildlife.imageUrl,
                        contentDescription = wildlife.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                }
            }

            // Basic Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = wildlife.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = wildlife.scientificName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Play Sound Button
                    if (wildlife.soundUrl != null) {
                        SoundPlayButton(
                            isPlaying = isPlayingSound,
                            onClick = onPlaySound
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            // Description
            item {
                DetailSection(
                    title = "Description",
                    content = wildlife.description
                )
            }

            // Habitat
            item {
                DetailSection(
                    title = "Habitat",
                    content = wildlife.habitat
                )
            }

            // Conservation Status
            item {
                ConservationStatusSection(
                    status = wildlife.conservationStatus,
                    details = wildlife.conservationDetails
                )
            }

            // Found in Karnataka
            if (wildlife.locationsInKarnataka.isNotEmpty()) {
                item {
                    LocationsSection(
                        locations = wildlife.locationsInKarnataka
                    )
                }
            }

            // Characteristics
            if (wildlife.characteristics.isNotEmpty()) {
                item {
                    CharacteristicsSection(
                        characteristics = wildlife.characteristics
                    )
                }
            }

            // Fun Facts
            if (wildlife.funFacts.isNotEmpty()) {
                item {
                    FunFactsSection(
                        facts = wildlife.funFacts
                    )
                }
            }
        }
    }
}

@Composable
fun SoundPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Stop Sound" else "Play Sound"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isPlaying) "Stop Sound" else "Play Animal Sound"
        )
    }
}

@Composable
fun DetailSection(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun ConservationStatusSection(
    status: String,
    details: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Conservation Status",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = getStatusColor(status),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = getStatusColor(status)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = details,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun LocationsSection(
    locations: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Found in Karnataka",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        locations.forEach { location ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CharacteristicsSection(
    characteristics: Map<String, String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Characteristics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        characteristics.forEach { (key, value) ->
            CharacteristicItem(label = key, value = value)
        }
    }
}

@Composable
fun CharacteristicItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun FunFactsSection(
    facts: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Fun Facts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        facts.forEachIndexed { index, fact ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = fact,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "critically endangered" -> Color(0xFFD32F2F)
        "endangered" -> Color(0xFFE64A19)
        "vulnerable" -> Color(0xFFF57C00)
        "near threatened" -> Color(0xFFFBC02D)
        "least concern" -> Color(0xFF388E3C)
        else -> Color.Gray
    }
}
ViewModel:

Kotlin

@HiltViewModel
class WildlifeDetailViewModel @Inject constructor(
    private val getWildlifeDetailUseCase: GetWildlifeDetailUseCase,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _wildlife = MutableStateFlow<Resource<Wildlife>>(Resource.Loading())
    val wildlife: StateFlow<Resource<Wildlife>> = _wildlife

    private val _isPlayingSound = MutableStateFlow(false)
    val isPlayingSound: StateFlow<Boolean> = _isPlayingSound

    fun loadWildlife(id: String) {
        viewModelScope.launch {
            getWildlifeDetailUseCase(id).collect { result ->
                _wildlife.value = result
            }
        }
    }

    fun playSound() {
        viewModelScope.launch {
            val wildlifeData = (_wildlife.value as? Resource.Success)?.data
            wildlifeData?.soundUrl?.let { soundUrl ->
                if (_isPlayingSound.value) {
                    audioPlayer.stop()
                    _isPlayingSound.value = false
                } else {
                    audioPlayer.play(soundUrl) { isPlaying ->
                        _isPlayingSound.value = isPlaying
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
4.5 Community Alerts Screen
File: AlertsScreen.kt

Layout:

text

┌─────────────────────────────┐
│  ←  Community Alerts    +   │
├─────────────────────────────┤
│  [All] [Sighting] [Conflict]│
├─────────────────────────────┤
│  ┌─────────────────────────┐│
│  │ 🔴 Tiger Sighting       ││
│  │ Bandipur National Park  ││
│  │ 2 hours ago             ││
│  └─────────────────────────┘│
│  ┌─────────────────────────┐│
│  │ 🟡 Elephant Herd        ││
│  │ Near Highway 212        ││
│  │ 5 hours ago             ││
│  └─────────────────────────┘│
│  ┌─────────────────────────┐│
│  │ 🟢 Leopard Spotted      ││
│  │ Nagarhole Forest        ││
│  │ 1 day ago               ││
│  └─────────────────────────┘│
└─────────────────────────────┘
Implementation:

Kotlin

@Composable
fun AlertsScreen(
    navController: NavController,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val alerts by viewModel.alerts.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community Alerts") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.CreateAlert.route)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Create Alert")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter Chips
            AlertFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.selectFilter(it) }
            )

            // Alerts List
            when (val state = alerts) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    if (state.data.isNullOrEmpty()) {
                        EmptyAlertsView()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.data) { alert ->
                                AlertCard(
                                    alert = alert,
                                    onClick = {
                                        navController.navigate(
                                            Screen.AlertDetail.createRoute(alert.id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    ErrorView(
                        message = state.message ?: "Failed to load alerts",
                        onRetry = { viewModel.loadAlerts() }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertFilterRow(
    selectedFilter: AlertFilter,
    onFilterSelected: (AlertFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(AlertFilter.values()) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.displayName) },
                leadingIcon = {
                    if (selectedFilter == filter) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AlertCard(
    alert: Alert,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Priority Indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = getPriorityColor(alert.priority),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = getAlertIcon(alert.type)),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Alert Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = alert.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getTimeAgo(alert.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // Distance (if available)
            if (alert.distance != null) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${alert.distance} km",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "away",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyAlertsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_alerts),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No alerts yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Be the first to report a sighting",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

fun getPriorityColor(priority: String): Color {
    return when (priority.lowercase()) {
        "high", "emergency" -> Color(0xFFD32F2F)
        "medium", "warning" -> Color(0xFFFBC02D)
        "low", "info" -> Color(0xFF388E3C)
        else -> Color.Gray
    }
}

fun getAlertIcon(type: String): Int {
    return when (type.lowercase()) {
        "sighting" -> R.drawable.ic_visibility
        "conflict" -> R.drawable.ic_warning
        "emergency" -> R.drawable.ic_emergency
        else -> R.drawable.ic_alert
    }
}

fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} minutes ago"
        diff < 86400_000 -> "${diff / 3600_000} hours ago"
        diff < 604800_000 -> "${diff / 86400_000} days ago"
        else -> "${diff / 604800_000} weeks ago"
    }
}
ViewModel:

Kotlin

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getAlertsUseCase: GetAlertsUseCase,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _alerts = MutableStateFlow<Resource<List<Alert>>>(Resource.Loading())
    val alerts: StateFlow<Resource<List<Alert>>> = _alerts

    private val _selectedFilter = MutableStateFlow(AlertFilter.ALL)
    val selectedFilter: StateFlow<AlertFilter> = _selectedFilter

    private var currentLocation: Location? = null

    init {
        loadAlerts()
        observeLocation()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            getAlertsUseCase(
                filter = _selectedFilter.value,
                location = currentLocation
            ).collect { result ->
                _alerts.value = result
            }
        }
    }

    fun selectFilter(filter: AlertFilter) {
        _selectedFilter.value = filter
        loadAlerts()
    }

    private fun observeLocation() {
        viewModelScope.launch {
            locationProvider.getLocation().collect { location ->
                currentLocation = location
                loadAlerts()
            }
        }
    }
}

enum class AlertFilter(val displayName: String) {
    ALL("All"),
    SIGHTING("Sightings"),
    CONFLICT("Conflicts"),
    EMERGENCY("Emergency")
}
4.6 Alert Detail Screen
File: AlertDetailScreen.kt

Kotlin

@Composable
fun AlertDetailScreen(
    alertId: String,
    navController: NavController,
    viewModel: AlertDetailViewModel = hiltViewModel()
) {
    val alert by viewModel.alert.collectAsState()

    LaunchedEffect(alertId) {
        viewModel.loadAlert(alertId)
    }

    when (val state = alert) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            state.data?.let { alertData ->
                AlertDetailContent(
                    alert = alertData,
                    onNavigateBack = { navController.popBackStack() },
                    onOpenMap = { /* Open maps */ }
                )
            }
        }
        is Resource.Error -> {
            ErrorView(
                message = state.message ?: "Failed to load alert",
                onRetry = { viewModel.loadAlert(alertId) }
            )
        }
    }
}

@Composable
fun AlertDetailContent(
    alert: Alert,
    onNavigateBack: () -> Unit,
    onOpenMap: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alert Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Alert Image (if available)
            alert.imageUrl?.let { imageUrl ->
                item {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Alert image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Priority Badge
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = getPriorityColor(alert.priority).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = getAlertIcon(alert.type)),
                            contentDescription = null,
                            tint = getPriorityColor(alert.priority)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${alert.type} - ${alert.priority} Priority",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getPriorityColor(alert.priority)
                        )
                    }
                }
            }

            // Title & Description
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = alert.description,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                }
            }

            // Location Info
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LocationInfoCard(
                    location = alert.location,
                    coordinates = alert.coordinates,
                    distance = alert.distance,
                    onOpenMap = onOpenMap
                )
            }

            // Time Info
            item {
                TimeInfoCard(
                    timestamp = alert.timestamp,
                    reportedBy = alert.reportedBy
                )
            }

            // Additional Details
            if (alert.additionalDetails.isNotEmpty()) {
                item {
                    AdditionalDetailsSection(
                        details = alert.additionalDetails
                    )
                }
            }

            // Safety Tips
            if (alert.safetyTips.isNotEmpty()) {
                item {
                    SafetyTipsSection(
                        tips = alert.safetyTips
                    )
                }
            }
        }
    }
}

@Composable
fun LocationInfoCard(
    location: String,
    coordinates: LatLng?,
    distance: Double?,
    onOpenMap: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                distance?.let {
                    Text(
                        text = "$it km",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            coordinates?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenMap,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View on Map")
                }
            }
        }
    }
}

@Composable
fun TimeInfoCard(
    timestamp: Long,
    reportedBy: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Reported",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = getTimeAgo(timestamp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            reportedBy?.let {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "By",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyTipsSection(tips: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Safety Tips",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        tips.forEach { tip ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
5. Database Schema
5.1 Room Database Entities
WildlifeEntity.kt:

Kotlin

@Entity(tableName = "wildlife")
data class WildlifeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val scientificName: String,
    val category: String,
    val description: String,
    val habitat: String,
    val conservationStatus: String,
    val conservationDetails: String,
    val imageUrl: String,
    val soundUrl: String?,
    val locationsInKarnataka: String, // JSON array stored as String
    val characteristics: String, // JSON object stored as String
    val funFacts: String, // JSON array stored as String
    val createdAt: Long,
    val updatedAt: Long
)
AlertEntity.kt:

Kotlin

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val location: String,
    val latitude: Double?,
    val longitude: Double?,
    val imageUrl: String?,
    val reportedBy: String?,
    val timestamp: Long,
    val additionalDetails: String, // JSON object
    val safetyTips: String, // JSON array
    val isSynced: Boolean = false
)
5.2 DAOs
WildlifeDao.kt:

Kotlin

@Dao
interface WildlifeDao {
    
    @Query("SELECT * FROM wildlife")
    fun getAllWildlife(): Flow<List<WildlifeEntity>>
    
    @Query("SELECT * FROM wildlife WHERE category = :category")
    fun getWildlifeByCategory(category: String): Flow<List<WildlifeEntity>>
    
    @Query("SELECT * FROM wildlife WHERE name LIKE '%' || :query || '%' OR scientificName LIKE '%' || :query || '%'")
    fun searchWildlife(query: String): Flow<List<WildlifeEntity>>
    
    @Query("SELECT * FROM wildlife WHERE id = :id")
    suspend fun getWildlifeById(id: String): WildlifeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWildlife(wildlife: WildlifeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWildlife(wildlife: List<WildlifeEntity>)
    
    @Delete
    suspend fun deleteWildlife(wildlife: WildlifeEntity)
    
    @Query("DELETE FROM wildlife")
    suspend fun deleteAllWildlife()
}
AlertDao.kt:

Kotlin

@Dao
interface AlertDao {
    
    @Query("SELECT * FROM alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>
    
    @Query("SELECT * FROM alerts WHERE type = :type ORDER BY timestamp DESC")
    fun getAlertsByType(type: String): Flow<List<AlertEntity>>
    
    @Query("SELECT * FROM alerts WHERE id = :id")
    suspend fun getAlertById(id: String): AlertEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlerts(alerts: List<AlertEntity>)
    
    @Query("DELETE FROM alerts WHERE timestamp < :cutoffTime")
    suspend fun deleteOldAlerts(cutoffTime: Long)
    
    @Query("SELECT * FROM alerts WHERE isSynced = 0")
    suspend fun getUnsyncedAlerts(): List<AlertEntity>
    
    @Query("UPDATE alerts SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
}
5.3 Database Class
KarunadaVanyaDatabase.kt:

Kotlin

@Database(
    entities = [
        WildlifeEntity::class,
        AlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KarunadaVanyaDatabase : RoomDatabase() {
    
    abstract fun wildlifeDao(): WildlifeDao
    abstract fun alertDao(): AlertDao
    
    companion object {
        const val DATABASE_NAME = "karunada_vanya_db"
    }
}
Converters.kt:

Kotlin

class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }
    
    @TypeConverter
    fun fromStringMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }
    
    @TypeConverter
    fun toStringMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }
}
6. Firebase Configuration
6.1 Firestore Structure
text

karunada_vanya/
├── alerts/
│   └── {alertId}/
│       ├── id: String
│       ├── title: String
│       ├── description: String
│       ├── type: String
│       ├── priority: String
│       ├── location: String
│       ├── coordinates: GeoPoint
│       ├── imageUrl: String
│       ├── reportedBy: String
│       ├── timestamp: Timestamp
│       ├── additionalDetails: Map
│       └── safetyTips: Array
├── wildlife_updates/
│   └── {updateId}/
│       ├── wildlifeId: String
│       ├── updateType: String
│       ├── data: Map
│       └── timestamp: Timestamp
└── users/
    └── {userId}/
        ├── name: String
        ├── email: String
        ├── notifications: Boolean
        └── location: GeoPoint
6.2 Firebase Services
FirebaseAlertService.kt:

Kotlin

class FirebaseAlertService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    
    private val alertsCollection = firestore.collection("alerts")
    
    fun observeAlerts(): Flow<List<AlertDto>> = callbackFlow {
        val listener = alertsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val alerts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(AlertDto::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(alerts)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun observeAlertsByLocation(
        center: GeoPoint,
        radiusInKm: Double
    ): Flow<List<AlertDto>> = callbackFlow {
        val bounds = GeoFireUtils.getGeoHashQueryBounds(
            center.latitude,
            center.longitude,
            radiusInKm * 1000
        )
        
        val listeners = mutableListOf<ListenerRegistration>()
        
        bounds.forEach { bound ->
            val query = alertsCollection
                .orderBy("geohash")
                .startAt(bound.startHash)
                .endAt(bound.endHash)
            
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val alerts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(AlertDto::class.java)?.copy(id = doc.id)
                }?.filter { alert ->
                    alert.coordinates?.let { coords ->
                        val distance = GeoFireUtils.getDistanceBetween(
                            center.latitude,
                            center.longitude,
                            coords.latitude,
                            coords.longitude
                        )
                        distance <= radiusInKm * 1000
                    } ?: false
                } ?: emptyList()
                
                trySend(alerts)
            }
            
            listeners.add(listener)
        }
        
        awaitClose { listeners.forEach { it.remove() } }
    }
    
    suspend fun createAlert(alert: AlertDto): Result<String> = withContext(Dispatchers.IO) {
        try {
            val alertWithHash = alert.copy(
                geohash = alert.coordinates?.let { coords ->
                    GeoFireUtils.getGeoHashForLocation(
                        GeoLocation(coords.latitude, coords.longitude)
                    )
                }
            )
            
            val docRef = alertsCollection.add(alertWithHash).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAlertById(id: String): AlertDto? = withContext(Dispatchers.IO) {
        try {
            alertsCollection.document(id).get().await()
                .toObject(AlertDto::class.java)?.copy(id = id)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun deleteAlert(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            alertsCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
FirebaseMessagingService.kt:

Kotlin

@HiltAndroidApp
class KarunadaVanyaMessagingService : FirebaseMessagingService() {
    
    @Inject
    lateinit var notificationHelper: NotificationHelper
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        remoteMessage.data.let { data ->
            val type = data["type"] ?: "general"
            val title = data["title"] ?: "Karunada Vanya"
            val body = data["body"] ?: ""
            val alertId = data["alertId"]
            
            when (type) {
                "new_alert" -> {
                    notificationHelper.showAlertNotification(
                        title = title,
                        body = body,
                        alertId = alertId
                    )
                }
                "emergency" -> {
                    notificationHelper.showEmergencyNotification(
                        title = title,
                        body = body,
                        alertId = alertId
                    )
                }
                else -> {
                    notificationHelper.showGeneralNotification(
                        title = title,
                        body = body
                    )
                }
            }
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to server
        saveTokenToPreferences(token)
    }
    
    private fun saveTokenToPreferences(token: String) {
        val prefs = getSharedPreferences("karunada_vanya_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()
    }
}
7. Development Workflow
7.1 Git Workflow
text

main (production)
  ↓
develop (development)
  ↓
feature/wildlife-wiki
feature/alerts-system
feature/audio-player
bugfix/issue-name
7.2 Branch Naming Convention
feature/feature-name - New features
bugfix/issue-name - Bug fixes
hotfix/critical-issue - Critical production fixes
refactor/component-name - Code refactoring
docs/update-name - Documentation updates
7.3 Commit Message Format
text

<type>(<scope>): <subject>

<body>

<footer>
Types:

feat: New feature
fix: Bug fix
docs: Documentation
style: Formatting
refactor: Code restructuring
test: Tests
chore: Maintenance
Example:

text

feat(wildlife): Add MotionLayout animations to wildlife cards

- Implemented card press animation
- Added elevation change on interaction
- Improved user feedback

Closes #123
8. Implementation Guide
8.1 Project Setup
Step 1: Create New Project

Bash

# Android Studio: New Project
# Select: Empty Compose Activity
# Name: Karunada Vanya
# Package: com.karunadavanya
# Minimum SDK: 24
# Language: Kotlin
Step 2: Update build.gradle (Project level)

Kotlin

// build.gradle (Project)
buildscript {
    ext {
        compose_version = '1.5.4'
        kotlin_version = '1.9.20'
        hilt_version = '2.48'
        room_version = '2.6.0'
    }
    dependencies {
        classpath "com.google.gms:google-services:4.4.0"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

plugins {
    id 'com.android.application' version '8.1.4' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.20' apply false
}
Step 3: Update build.gradle (App level)

Kotlin

// build.gradle (App)
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
    id 'com.google.gms.google-services'
}

android {
    namespace 'com.karunadavanya'
    compileSdk 34

    defaultConfig {
        applicationId "com.karunadavanya"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary true
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            applicationIdSuffix ".debug"
            debuggable true
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = '17'
    }
    
    buildFeatures {
        compose true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.4'
    }
    
    packagingOptions {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}

dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2'
    implementation 'androidx.activity:activity-compose:1.8.1'

    // Compose
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.material:material-icons-extended'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.5'
    
    // MotionLayout for Compose
    implementation 'androidx.constraintlayout:constraintlayout-compose:1.0.1'
    
    // Hilt Dependency Injection
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    
    // Room Database
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.6.0')
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    
    // GeoFire for location queries
    implementation 'com.firebase:geofire-android:3.2.0'
    
    // Image Loading
    implementation 'io.coil-kt:coil-compose:2.5.0'
    
    // ExoPlayer for audio
    implementation 'androidx.media3:media3-exoplayer:1.2.0'
    implementation 'androidx.media3:media3-ui:1.2.0'
    
    // Gson for JSON parsing
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3'
    
    // Location Services
    implementation 'com.google.android.gms:play-services-location:21.0.1'
    
    // Permissions
    implementation 'com.google.accompanist:accompanist-permissions:0.32.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation platform('androidx.compose:compose-bom:2023.10.01')
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    debugImplementation 'androidx.compose.ui:ui-tooling'
    debugImplementation 'androidx.compose.ui:ui-test-manifest'
}
8.2 Hilt Setup
KarunadaVanyaApplication.kt:

Kotlin

@HiltAndroidApp
class KarunadaVanyaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Setup notification channels
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    "alerts",
                    "Wildlife Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for wildlife sightings and alerts"
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                },
                NotificationChannel(
                    "emergency",
                    "Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Critical emergency notifications"
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                        null
                    )
                }
            )
            
            val manager = getSystemService(NotificationManager::class.java)
            channels.forEach { manager.createNotificationChannel(it) }
        }
    }
}
AndroidManifest.xml:

XML

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <application
        android:name=".KarunadaVanyaApplication"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.KarunadaVanya"
        tools:targetApi="31">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.KarunadaVanya">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".KarunadaVanyaMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

        <meta-data
            android:name="com.google.firebase.messaging.default_notification_channel_id"
            android:value="alerts" />
    </application>

</manifest>
8.3 Dependency Injection Modules
AppModule.kt:

Kotlin

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
    
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
    
    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AudioPlayer {
        return AudioPlayerImpl(context)
    }
    
    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider {
        return LocationProviderImpl(context)
    }
}
DatabaseModule.kt:

Kotlin

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KarunadaVanyaDatabase {
        return Room.databaseBuilder(
            context,
            KarunadaVanyaDatabase::class.java,
            KarunadaVanyaDatabase.DATABASE_NAME
        )
            .createFromAsset("database/karunada_vanya.db") // Pre-populated database
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideWildlifeDao(database: KarunadaVanyaDatabase): WildlifeDao {
        return database.wildlifeDao()
    }
    
    @Provides
    fun provideAlertDao(database: KarunadaVanyaDatabase): AlertDao {
        return database.alertDao()
    }
}
FirebaseModule.kt:

Kotlin

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore.apply {
            firestoreSettings = firestoreSettings {
                isPersistenceEnabled = true
                cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            }
        }
    }
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
    
    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return Firebase.messaging
    }
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }
    
    @Provides
    @Singleton
    fun provideFirebaseAlertService(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FirebaseAlertService {
        return FirebaseAlertService(firestore, auth)
    }
}
RepositoryModule.kt:

Kotlin

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideWildlifeRepository(
        wildlifeDao: WildlifeDao,
        gson: Gson
    ): WildlifeRepository {
        return WildlifeRepositoryImpl(wildlifeDao, gson)
    }
    
    @Provides
    @Singleton
    fun provideAlertRepository(
        alertDao: AlertDao,
        firebaseAlertService: FirebaseAlertService,
        gson: Gson
    ): AlertRepository {
        return AlertRepositoryImpl(alertDao, firebaseAlertService, gson)
    }
}
8.4 Domain Layer Implementation
Models:

Wildlife.kt:

Kotlin

data class Wildlife(
    val id: String,
    val name: String,
    val scientificName: String,
    val category: String,
    val description: String,
    val habitat: String,
    val conservationStatus: String,
    val conservationDetails: String,
    val imageUrl: String,
    val soundUrl: String?,
    val locationsInKarnataka: List<String>,
    val characteristics: Map<String, String>,
    val funFacts: List<String>,
    val createdAt: Long,
    val updatedAt: Long
)
Alert.kt:

Kotlin

data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val location: String,
    val coordinates: LatLng?,
    val distance: Double?,
    val imageUrl: String?,
    val reportedBy: String?,
    val timestamp: Long,
    val additionalDetails: Map<String, Any>,
    val safetyTips: List<String>
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)
Use Cases:

GetWildlifeListUseCase.kt:

Kotlin

class GetWildlifeListUseCase @Inject constructor(
    private val repository: WildlifeRepository
) {
    operator fun invoke(
        category: String? = null,
        searchQuery: String? = null
    ): Flow<Resource<List<Wildlife>>> = flow {
        emit(Resource.Loading())
        
        try {
            val wildlifeFlow = when {
                !searchQuery.isNullOrBlank() -> repository.searchWildlife(searchQuery)
                !category.isNullOrBlank() && category != "all" -> 
                    repository.getWildlifeByCategory(category)
                else -> repository.getAllWildlife()
            }
            
            wildlifeFlow.collect { wildlife ->
                emit(Resource.Success(wildlife))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}
GetWildlifeDetailUseCase.kt:

Kotlin

class GetWildlifeDetailUseCase @Inject constructor(
    private val repository: WildlifeRepository
) {
    operator fun invoke(id: String): Flow<Resource<Wildlife>> = flow {
        emit(Resource.Loading())
        
        try {
            val wildlife = repository.getWildlifeById(id)
            if (wildlife != null) {
                emit(Resource.Success(wildlife))
            } else {
                emit(Resource.Error("Wildlife not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}
GetAlertsUseCase.kt:

Kotlin

class GetAlertsUseCase @Inject constructor(
    private val repository: AlertRepository,
    private val locationProvider: LocationProvider
) {
    operator fun invoke(
        filter: AlertFilter = AlertFilter.ALL,
        location: Location? = null
    ): Flow<Resource<List<Alert>>> = flow {
        emit(Resource.Loading())
        
        try {
            val alertsFlow = if (location != null) {
                repository.getAlertsByLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    radiusInKm = 50.0
                )
            } else {
                repository.getAllAlerts()
            }
            
            alertsFlow.collect { alerts ->
                val filtered = when (filter) {
                    AlertFilter.ALL -> alerts
                    AlertFilter.SIGHTING -> alerts.filter { it.type == "sighting" }
                    AlertFilter.CONFLICT -> alerts.filter { it.type == "conflict" }
                    AlertFilter.EMERGENCY -> alerts.filter { it.priority == "emergency" }
                }
                
                // Calculate distances if location is available
                val withDistance = if (location != null) {
                    filtered.map { alert ->
                        alert.coordinates?.let { coords ->
                            val distance = calculateDistance(
                                location.latitude,
                                location.longitude,
                                coords.latitude,
                                coords.longitude
                            )
                            alert.copy(distance = distance)
                        } ?: alert
                    }
                } else {
                    filtered
                }
                
                emit(Resource.Success(withDistance))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load alerts"))
        }
    }
    
    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
8.5 Audio Player Implementation
AudioPlayer.kt:

Kotlin

interface AudioPlayer {
    suspend fun play(url: String, onPlaybackStateChanged: (Boolean) -> Unit)
    fun pause()
    fun resume()
    fun stop()
    fun release()
    fun isPlaying(): Boolean
}
AudioPlayerImpl.kt:

Kotlin

@Singleton
class AudioPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlayer {
    
    private var exoPlayer: ExoPlayer? = null
    private var currentUrl: String? = null
    private var playbackStateCallback: ((Boolean) -> Unit)? = null
    
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> playbackStateCallback?.invoke(true)
                Player.STATE_ENDED -> {
                    playbackStateCallback?.invoke(false)
                    stop()
                }
                Player.STATE_IDLE -> playbackStateCallback?.invoke(false)
            }
        }
        
        override fun onPlayerError(error: PlaybackException) {
            playbackStateCallback?.invoke(false)
            Log.e("AudioPlayer", "Playback error: ${error.message}")
        }
    }
    
    override suspend fun play(url: String, onPlaybackStateChanged: (Boolean) -> Unit) {
        playbackStateCallback = onPlaybackStateChanged
        
        if (currentUrl == url && exoPlayer?.isPlaying == true) {
            stop()
            return
        }
        
        withContext(Dispatchers.Main) {
            if (exoPlayer == null) {
                exoPlayer = ExoPlayer.Builder(context).build().apply {
                    addListener(playerListener)
                }
            }
            
            val mediaItem = if (url.startsWith("http")) {
                MediaItem.fromUri(url)
            } else {
                // Local file from assets
                MediaItem.fromUri("asset:///$url")
            }
            
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
            
            currentUrl = url
        }
    }
    
    override fun pause() {
        exoPlayer?.pause()
        playbackStateCallback?.invoke(false)
    }
    
    override fun resume() {
        exoPlayer?.play()
        playbackStateCallback?.invoke(true)
    }
    
    override fun stop() {
        exoPlayer?.stop()
        currentUrl = null
        playbackStateCallback?.invoke(false)
    }
    
    override fun release() {
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
        currentUrl = null
        playbackStateCallback = null
    }
    
    override fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }
}
8.6 Navigation Setup
Screen.kt:

Kotlin

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object WildlifeList : Screen("wildlife_list")
    object WildlifeDetail : Screen("wildlife_detail/{wildlifeId}") {
        fun createRoute(wildlifeId: String) = "wildlife_detail/$wildlifeId"
    }
    object AlertsList : Screen("alerts_list")
    object AlertDetail : Screen("alert_detail/{alertId}") {
        fun createRoute(alertId: String) = "alert_detail/$alertId"
    }
    object CreateAlert : Screen("create_alert")
}
NavGraph.kt:

Kotlin

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        
        composable(Screen.WildlifeList.route) {
            WildlifeListScreen(navController)
        }
        
        composable(
            route = Screen.WildlifeDetail.route,
            arguments = listOf(
                navArgument("wildlifeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val wildlifeId = backStackEntry.arguments?.getString("wildlifeId") ?: return@composable
            WildlifeDetailScreen(
                wildlifeId = wildlifeId,
                navController = navController
            )
        }
        
        composable(Screen.AlertsList.route) {
            AlertsScreen(navController)
        }
        
        composable(
            route = Screen.AlertDetail.route,
            arguments = listOf(
                navArgument("alertId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val alertId = backStackEntry.arguments?.getString("alertId") ?: return@composable
            AlertDetailScreen(
                alertId = alertId,
                navController = navController
            )
        }
        
        composable(Screen.CreateAlert.route) {
            CreateAlertScreen(navController)
        }
    }
}
MainActivity.kt:

Kotlin

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            KarunadaVanyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
9. Testing Requirements
9.1 Unit Tests
WildlifeViewModelTest.kt:

Kotlin

@ExperimentalCoroutinesApi
class WildlifeViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: WildlifeViewModel
    private lateinit var getWildlifeListUseCase: GetWildlifeListUseCase
    private lateinit var mockRepository: WildlifeRepository
    
    @Before
    fun setup() {
        mockRepository = mockk()
        getWildlifeListUseCase = GetWildlifeListUseCase(mockRepository)
        viewModel = WildlifeViewModel(getWildlifeListUseCase, mockk())
    }
    
    @Test
    fun `load wildlife list should emit success state`() = runTest {
        // Given
        val mockWildlife = listOf(
            Wildlife(/* ... */)
        )
        coEvery { mockRepository.getAllWildlife() } returns flowOf(mockWildlife)
        
        // When
        viewModel.loadWildlife()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.wildlifeList.value
        assertTrue(state is Resource.Success)
        assertEquals(mockWildlife, (state as Resource.Success).data)
    }
    
    @Test
    fun `search wildlife should filter results`() = runTest {
        // Given
        val searchQuery = "tiger"
        val mockResults = listOf(
            Wildlife(id = "1", name = "Bengal Tiger", /* ... */)
        )
        coEvery { mockRepository.searchWildlife(searchQuery) } returns flowOf(mockResults)
        
        // When
        viewModel.updateSearchQuery(searchQuery)
        advanceUntilIdle()
        
        // Then
        assertEquals(searchQuery, viewModel.searchQuery.value)
        val state = viewModel.wildlifeList.value
        assertTrue(state is Resource.Success)
        assertEquals(1, (state as Resource.Success).data?.size)
    }
}
9.2 Integration Tests
DatabaseTest.kt:

Kotlin

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    
    private lateinit var database: KarunadaVanyaDatabase
    private lateinit var wildlifeDao: WildlifeDao
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            KarunadaVanyaDatabase::class.java
        ).build()
        wildlifeDao = database.wildlifeDao()
    }
    
    @After
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun insertAndRetrieveWildlife() = runBlocking {
        // Given
        val wildlife = WildlifeEntity(
            id = "1",
            name = "Bengal Tiger",
            scientificName = "Panthera tigris tigris",
            category = "mammals",
            // ... other fields
        )
        
        // When
        wildlifeDao.insertWildlife(wildlife)
        val retrieved = wildlifeDao.getWildlifeById("1")
        
        // Then
        assertNotNull(retrieved)
        assertEquals(wildlife.name, retrieved?.name)
    }
    
    @Test
    fun searchWildlifeByName() = runBlocking {
        // Given
        val wildlife1 = WildlifeEntity(id = "1", name = "Bengal Tiger", /* ... */)
        val wildlife2 = WildlifeEntity(id = "2", name = "Asian Elephant", /* ... */)
        wildlifeDao.insertAllWildlife(listOf(wildlife1, wildlife2))
        
        // When
        val results = wildlifeDao.searchWildlife("tiger").first()
        
        // Then
        assertEquals(1, results.size)
        assertEquals("Bengal Tiger", results[0].name)
    }
}
9.3 UI Tests
WildlifeListScreenTest.kt:

Kotlin

@RunWith(AndroidJUnit4::class)
class WildlifeListScreenTest {
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Test
    fun wildlifeListDisplaysItems() {
        // Navigate to wildlife list
        composeTestRule.onNodeWithText("Explore Wildlife").performClick()
        
        // Verify list is displayed
        composeTestRule.onNodeWithTag("wildlife_list").assertExists()
        
        // Verify search bar exists
        composeTestRule.onNodeWithText("Search wildlife...").assertExists()
    }
    
    @Test
    fun searchFiltersList() {
        composeTestRule.onNodeWithText("Explore Wildlife").performClick()
        
        // Enter search query
        composeTestRule.onNodeWithText("Search wildlife...")
            .performTextInput("tiger")
        
        // Verify filtered results
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bengal Tiger").assertExists()
    }
    
    @Test
    fun clickingCardNavigatesToDetail() {
        composeTestRule.onNodeWithText("Explore Wildlife").performClick()
        
        // Click on first item
        composeTestRule.onNodeWithTag("wildlife_card_0").performClick()
        
        // Verify detail screen is shown
        composeTestRule.onNodeWithTag("wildlife_detail").assertExists()
    }
}
10. Deployment Guidelines
10.1 Pre-deployment Checklist
 All unit tests passing
 All integration tests passing
 UI tests passing
 No critical bugs
 Performance optimization completed
 Security audit completed
 Privacy policy updated
 Terms of service updated
 App store listing prepared
 Screenshots prepared (all screen sizes)
 Demo video created
 Beta testing completed
10.2 Build Configuration
Release Build:

Kotlin

// build.gradle (app)
android {
    signingConfigs {
        release {
            storeFile file("../keystore/karunada_vanya.jks")
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias System.getenv("KEY_ALIAS")
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 
                         'proguard-rules.pro'
        }
    }
}
ProGuard Rules:

proguard

# proguard-rules.pro

# Keep data classes
-keep class com.karunadavanya.domain.model.** { *; }
-keep class com.karunadavanya.data.local.entities.** { *; }
-keep class com.karunadavanya.data.remote.dto.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Retrofit & OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**
10.3 Version Management
Version Naming:

Format: MAJOR.MINOR.PATCH
Example: 1.0.0 → 1.0.1 → 1.1.0 → 2.0.0
Version Code:

Increment by 1 for each release
Start from 1
10.4 Release Process
Step 1: Prepare Release Branch

Bash

git checkout develop
git pull origin develop
git checkout -b release/1.0.0
Step 2: Update Version

Kotlin

// build.gradle (app)
defaultConfig {
    versionCode 1
    versionName "1.0.0"
}
Step 3: Build Release APK/AAB

Bash

./gradlew assembleRelease
./gradlew bundleRelease
Step 4: Test Release Build

Bash

./gradlew connectedAndroidTest
Step 5: Create Git Tag

Bash

git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
Step 6: Upload to Play Console

Login to Google Play Console
Select app
Navigate to "Release" → "Production"
Create new release
Upload AAB file
Add release notes
Review and rollout
10.5 Post-deployment
 Monitor crash reports
 Monitor ANRs
 Check user reviews
 Monitor Firebase Analytics
 Monitor API usage
 Check performance metrics
 Verify all features working
 Prepare hotfix if needed
11. Maintenance & Support
11.1 Monitoring
Firebase Crashlytics Setup:

Kotlin

// Initialize in Application class
FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

// Log custom events
FirebaseCrashlytics.getInstance().log("User viewed wildlife: $wildlifeName")

// Set user identifiers
FirebaseCrashlytics.getInstance().setUserId(userId)

// Record non-fatal exceptions
try {
    // Code that might throw
} catch (e: Exception) {
    FirebaseCrashlytics.getInstance().recordException(e)
}
Analytics Events:

Kotlin

// Track screen views
firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
    param(FirebaseAnalytics.Param.SCREEN_NAME, "Wildlife Detail")
    param(FirebaseAnalytics.Param.SCREEN_CLASS, "WildlifeDetailScreen")
}

// Track user actions
firebaseAnalytics.logEvent("play_animal_sound") {
    param("wildlife_id", wildlifeId)
    param("wildlife_name", wildlifeName)
}

// Track alert views
firebaseAnalytics.logEvent("view_alert") {
    param("alert_type", alertType)
    param("alert_priority", priority)
}
11.2 Database Updates
Migration Strategy:

Kotlin

@Database(
    entities = [WildlifeEntity::class, AlertEntity::class],
    version = 2,
    exportSchema = true
)
abstract class KarunadaVanyaDatabase : RoomDatabase() {
    
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE wildlife ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}

// In DatabaseModule
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): KarunadaVanyaDatabase {
    return Room.databaseBuilder(
        context,
        KarunadaVanyaDatabase::class.java,
        KarunadaVanyaDatabase.DATABASE_NAME
    )
        .addMigrations(KarunadaVanyaDatabase.MIGRATION_1_2)
        .build()
}
11.3 Content Updates
Wildlife Data Update Process:

Prepare new wildlife data in JSON format
Create migration script
Test locally
Deploy via Firebase Remote Config or in-app update
Example Remote Config:

Kotlin

@Singleton
class RemoteConfigManager @Inject constructor() {
    
    private val remoteConfig = Firebase.remoteConfig
    
    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // 1 hour
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
    
    suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()
    }
    
    fun getMinimumAppVersion(): Int {
        return remoteConfig.getLong("minimum_app_version").toInt()
    }
    
    fun shouldShowNewFeature(): Boolean {
        return remoteConfig.getBoolean("show_new_feature")
    }
}
12. Additional Resources
12.1 Design Assets
Required Assets:

App icon (adaptive icon with foreground and background)
Splash screen logo
Wildlife category icons
Alert type icons
Navigation icons
Empty state illustrations
Error state illustrations
Asset Specifications:

text

ic_launcher/
├── mipmap-mdpi/ (48x48 dp)
├── mipmap-hdpi/ (72x72 dp)
├── mipmap-xhdpi/ (96x96 dp)
├── mipmap-xxhdpi/ (144x144 dp)
└── mipmap-xxxhdpi/ (192x192 dp)
12.2 Sample Wildlife Data
wildlife_sample.json:

JSON

{
  "wildlife": [
    {
      "id": "bengal_tiger",
      "name": "Bengal Tiger",
      "scientificName": "Panthera tigris tigris",
      "category": "mammals",
      "description": "The Bengal tiger is the most numerous tiger subspecies in Asia and has been listed as Endangered on the IUCN Red List since 2008. It is one of the largest wild cats alive today.",
      "habitat": "Bengal tigers live in various habitats, including tropical and subtropical rainforests, mangroves, grasslands, and even some mountainous regions. They prefer dense vegetation cover and proximity to water sources.",
      "conservationStatus": "Endangered",
      "conservationDetails": "Population has declined due to poaching and habitat loss. Protected in national parks and reserves across Karnataka.",
      "imageUrl": "file:///android_asset/wildlife/bengal_tiger.jpg",
      "soundUrl": "file:///android_asset/sounds/tiger_roar.mp3",
      "locationsInKarnataka": [
        "Bandipur National Park",
        "Nagarhole National Park",
        "Bhadra Wildlife Sanctuary",
        "Dandeli Wildlife Sanctuary"
      ],
      "characteristics": {
        "Average Weight": "180-260 kg (male), 100-160 kg (female)",
        "Average Length": "2.7-3.1 m (male), 2.4-2.7 m (female)",
        "Lifespan": "10-15 years in wild, up to 20 in captivity",
        "Diet": "Carnivore - deer, wild boar, buffalo",
        "Gestation Period": "93-112 days",
        "Cubs per Litter": "2-4"
      },
      "funFacts": [
        "Tigers are the largest cat species in the world",
        "Each tiger has a unique pattern of stripes, like human fingerprints",
        "Tigers are excellent swimmers and often cool off in pools during hot days",
        "A tiger's roar can be heard up to 3 kilometers away",
        "Unlike most cats, tigers are enthusiastic about water"
      ],
      "createdAt": 1704067200000,
      "updatedAt": 1704067200000
    }
  ]
}
12.3 Firebase Rules
Firestore Security Rules:

JavaScript

rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Alerts collection
    match /alerts/{alertId} {
      // Anyone can read alerts
      allow read: if true;
      
      // Only authenticated users can create alerts
      allow create: if request.auth != null
                    && request.resource.data.reportedBy == request.auth.uid
                    && request.resource.data.timestamp == request.time;
      
      // Only the creator can update their alerts
      allow update: if request.auth != null
                    && resource.data.reportedBy == request.auth.uid;
      
      // Only the creator or admin can delete
      allow delete: if request.auth != null
                    && (resource.data.reportedBy == request.auth.uid
                        || get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin');
    }
    
    // Users collection
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Wildlife updates collection (admin only)
    match /wildlife_updates/{updateId} {
      allow read: if true;
      allow write: if request.auth != null
                   && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
  }
}
Storage Rules:

JavaScript

rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    
    // Alert images
    match /alerts/{alertId}/{fileName} {
      allow read: if true;
      allow write: if request.auth != null
                   && request.resource.size < 5 * 1024 * 1024  // 5MB limit
                   && request.resource.contentType.matches('image/.*');
    }
    
    // Wildlife images (admin only)
    match /wildlife/{wildlifeId}/{fileName} {
      allow read: if true;
      allow write: if request.auth != null
                   && get(/firestore/databases/(default)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
  }
}
13. Troubleshooting Guide
13.1 Common Issues
Issue: Room database not found

Kotlin

// Solution: Check if asset is in correct location
// assets/database/karunada_vanya.db

// Verify database creation
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): KarunadaVanyaDatabase {
    return Room.databaseBuilder(
        context,
        KarunadaVanyaDatabase::class.java,
        KarunadaVanyaDatabase.DATABASE_NAME
    )
        .createFromAsset("database/karunada_vanya.db")
        .fallbackToDestructiveMigration()
        .build()
}
Issue: Firebase not receiving real-time updates

Kotlin

// Solution: Check Firestore settings
val firestore = Firebase.firestore.apply {
    firestoreSettings = firestoreSettings {
        isPersistenceEnabled = true
        cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
    }
}

// Verify listener is attached
val listener = alertsCollection
    .addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.e("Firestore", "Listen failed: ${error.message}")
            return@addSnapshotListener
        }
        // Handle updates
    }
Issue: Audio not playing

Kotlin

// Solution: Check permissions and file path
// Verify INTERNET permission for remote URLs
// Verify file exists for local files

// Debug logging
override suspend fun play(url: String, onPlaybackStateChanged: (Boolean) -> Unit) {
    Log.d("AudioPlayer", "Attempting to play: $url")
    
    try {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    } catch (e: Exception) {
        Log.e("AudioPlayer", "Error playing audio", e)
        onPlaybackStateChanged(false)
    }
}
Issue: MotionLayout animations not working

Kotlin

// Solution: Verify MotionScene is correctly referenced
// Check XML scene file exists in res/xml/

// Ensure correct MotionLayout setup
MotionLayout(
    modifier = modifier,
    motionScene = MotionScene(
        content = getMotionSceneContent()
    )
) {
    // Content
}
13.2 Performance Optimization
Database Query Optimization:

Kotlin

// Use indexes for frequently queried fields
@Entity(
    tableName = "wildlife",
    indices = [
        Index(value = ["category"]),
        Index(value = ["name"])
    ]
)
data class WildlifeEntity(...)

// Limit query results
@Query("SELECT * FROM wildlife LIMIT :limit")
fun getWildlifeLimit(limit: Int): Flow<List<WildlifeEntity>>

// Use paging for large lists
@Query("SELECT * FROM wildlife ORDER BY name ASC")
fun getWildlifePaged(): PagingSource<Int, WildlifeEntity>
Image Loading Optimization:

Kotlin

// Configure Coil for better performance
@Provides
@Singleton
fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(50 * 1024 * 1024) // 50MB
                .build()
        }
        .respectCacheHeaders(false)
        .build()
}

// Use in Composable
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(wildlife.imageUrl)
        .crossfade(true)
        .size(Size.ORIGINAL) // or specific size
        .build(),
    contentDescription = wildlife.name,
    modifier = Modifier.fillMaxWidth()
)