package com.karunadavanya.presentation.localization

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.presentation.WildlifeDetailItem
import com.karunadavanya.presentation.WildlifeListItem

enum class AppLanguage {
    English,
    Kannada
}

@Stable
class LanguageController(
    val language: AppLanguage,
    val setLanguage: (AppLanguage) -> Unit
)

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.English }
val LocalLanguageController = staticCompositionLocalOf {
    LanguageController(AppLanguage.English) {}
}

@Composable
fun ProvideAppLanguage(content: @Composable () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("karunada_language", Context.MODE_PRIVATE)
    }
    var language by remember {
        mutableStateOf(
            if (prefs.getString("language", AppLanguage.English.name) == AppLanguage.Kannada.name) {
                AppLanguage.Kannada
            } else {
                AppLanguage.English
            }
        )
    }
    val controller = remember(language) {
        LanguageController(language) { next ->
            language = next
            prefs.edit().putString("language", next.name).apply()
        }
    }

    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalLanguageController provides controller,
        content = content
    )
}

@Composable
fun KannadaLanguageToggle() {
    val controller = LocalLanguageController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "ಕನ್ನಡ",
            fontSize = 11.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF064E3B)
        )
        Switch(
            modifier = Modifier.height(36.dp),
            checked = controller.language == AppLanguage.Kannada,
            onCheckedChange = { checked ->
                controller.setLanguage(if (checked) AppLanguage.Kannada else AppLanguage.English)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1B4332),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFC1C8C2)
            )
        )
    }
}

fun tr(text: String, language: AppLanguage): String {
    if (language == AppLanguage.English) return text
    return uiTranslations[text] ?: text
}

fun speciesName(name: String, language: AppLanguage): String {
    if (language == AppLanguage.English) return name
    return speciesNames[name] ?: name
}

fun WildlifeListItem.localized(language: AppLanguage): WildlifeListItem {
    if (language == AppLanguage.English) return this
    val info = wildlifeText[rawId]
    return copy(
        name = info?.name ?: speciesName(name, language),
        category = uiTranslations[category] ?: category,
        description = info?.description ?: description
    )
}

fun WildlifeDetailItem.localized(language: AppLanguage): WildlifeDetailItem {
    if (language == AppLanguage.English) return this
    val info = wildlifeText[rawId]
    return copy(
        name = info?.name ?: speciesName(name, language),
        description = info?.description ?: description,
        habitat = info?.habitat ?: habitat,
        conservationStatus = uiTranslations[conservationStatus] ?: conservationStatus,
        funFact = info?.funFact ?: funFact,
        locations = locations.map { uiTranslations[it] ?: it }
    )
}

fun alertTitle(alert: CommunityAlert, language: AppLanguage): String {
    if (language == AppLanguage.English) return alert.title.ifBlank { "${alert.animalType} Movement" }
    val animal = speciesName(alert.animalType, language)
    return if (animal == alert.animalType) {
        tr(alert.title.ifBlank { "${alert.animalType} Movement" }, language)
    } else {
        "$animal ಚಲನೆ / ಕಾಣಿಕೆ"
    }
}

fun alertDescription(alert: CommunityAlert, language: AppLanguage): String {
    if (language == AppLanguage.English) return alert.description
    val animal = speciesName(alert.animalType, language)
    return if (animal == alert.animalType) {
        tr(alert.description, language)
    } else {
        "$animal ಬಗ್ಗೆ ಮಾಹಿತಿ ಬಂದಿದೆ. ಸುರಕ್ಷಿತ ಅಂತರ ಕಾಯ್ದುಕೊಳ್ಳಿ ಮತ್ತು ಅಗತ್ಯವಿದ್ದರೆ ಅರಣ್ಯ ಇಲಾಖೆಗೆ ತಿಳಿಸಿ."
    }
}

fun relativeTimeText(reportedAt: Long, language: AppLanguage): String {
    val elapsed = (System.currentTimeMillis() - reportedAt).coerceAtLeast(0L)
    val hours = elapsed / 3_600_000L
    val minutes = (elapsed % 3_600_000L) / 60_000L
    return if (language == AppLanguage.English) {
        when {
            hours >= 1L -> "${hours} hours ago"
            minutes >= 1L -> "${minutes} min ago"
            else -> "Just now"
        }
    } else {
        when {
            hours >= 1L -> "${hours} ಗಂಟೆಗಳ ಹಿಂದೆ"
            minutes >= 1L -> "${minutes} ನಿಮಿಷಗಳ ಹಿಂದೆ"
            else -> "ಈಗಷ್ಟೇ"
        }
    }
}

private data class WildlifeKn(
    val name: String,
    val description: String,
    val habitat: String,
    val funFact: String
)

private val speciesNames = mapOf(
    "Elephant" to "ಆನೆ",
    "Tiger" to "ಹುಲಿ",
    "Leopard" to "ಚಿರತೆ",
    "Black Panther" to "ಕಪ್ಪು ಚಿರತೆ",
    "Sloth Bear" to "ಕರಡಿ",
    "Wild Boar" to "ಕಾಡು ಹಂದಿ",
    "Gaur" to "ಕಾಡೆಮ್ಮೆ",
    "Gaur (Indian Bison)" to "ಕಾಡೆಮ್ಮೆ",
    "Nilgiri Tahr" to "ನೀಲಗಿರಿ ತಾರ್",
    "King Cobra" to "ಕಾಳಿಂಗ ಸರ್ಪ",
    "Mugger Crocodile" to "ಮಗ್ಗರ್ ಮೊಸಳೆ",
    "Giant Squirrel" to "ದೊಡ್ಡ ಅಳಿಲು",
    "Common Indian Toad" to "ಭಾರತೀಯ ಕಪ್ಪೆ",
    "Malabar Gliding Frog" to "ಮಲಬಾರ್ ಗ್ಲೈಡಿಂಗ್ ಕಪ್ಪೆ",
    "Great Indian Hornbill" to "ದೊಡ್ಡ ಕೊಂಬುಕೊಕ್ಕರೆ",
    "Indian Peafowl" to "ನವಿಲು",
    "Sandalwood" to "ಶ್ರೀಗಂಧ",
    "Rosewood" to "ಬೀಟೆ ಮರ",
    "Teak" to "ತೇಗ"
)

private val uiTranslations = mapOf(
    "Home" to "ಮನೆ",
    "Explore" to "ಅನ್ವೇಷಿಸಿ",
    "Alerts" to "ಎಚ್ಚರಿಕೆಗಳು",
    "Report" to "ವರದಿ",
    "Wildlife Wonder" to "ವನ್ಯಜೀವಿ ವಿಸ್ಮಯ",
    "Community Safety" to "ಸಮುದಾಯ ಸುರಕ್ಷತೆ",
    "Explore interactive educational modules about Karnataka's native fauna and habitat corridors." to "ಕರ್ನಾಟಕದ ಸ್ಥಳೀಯ ವನ್ಯಜೀವಿಗಳು ಮತ್ತು ಅವುಗಳ ವಾಸಸ್ಥಳಗಳ ಬಗ್ಗೆ ತಿಳಿಯಿರಿ.",
    "Real-time alerts for local wildlife movements to ensure peaceful coexistence." to "ಸ್ಥಳೀಯ ವನ್ಯಜೀವಿ ಚಲನೆಗಳ ಬಗ್ಗೆ ತಕ್ಷಣದ ಎಚ್ಚರಿಕೆಗಳು.",
    "Live Conservation" to "ಜೀವಂತ ಸಂರಕ್ಷಣೆ",
    "Preserving Karnataka's Natural Heritage" to "ಕರ್ನಾಟಕದ ನೈಸರ್ಗಿಕ ಪರಂಪರೆಯ ಸಂರಕ್ಷಣೆ",
    "Experience the untamed beauty of our forests while contributing to vital local species protection programs." to "ನಮ್ಮ ಕಾಡಿನ ಸೌಂದರ್ಯವನ್ನು ಅನುಭವಿಸಿ ಮತ್ತು ಸ್ಥಳೀಯ ಜೀವಿಗಳ ಸಂರಕ್ಷಣೆಗೆ ಕೈಜೋಡಿಸಿ.",
    "NATIONAL PRIDE" to "ರಾಷ್ಟ್ರೀಯ ಹೆಮ್ಮೆ",
    "\"Guardians of the Western Ghats, Stewards of the Wild.\"" to "\"ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ರಕ್ಷಕರು, ವನ್ಯಲೋಕದ ಪಾಲಕರು.\"",
    "COMMUNING WITH NATURE..." to "ಪ್ರಕೃತಿಯೊಂದಿಗೆ ಸಂಪರ್ಕ...",
    "Government of Karnataka - Wildlife Division" to "ಕರ್ನಾಟಕ ಸರ್ಕಾರ - ವನ್ಯಜೀವಿ ವಿಭಾಗ",
    "Explore Wildlife" to "ವನ್ಯಜೀವಿ ಅನ್ವೇಷಣೆ",
    "The heart of Karnataka's forests" to "ಕರ್ನಾಟಕದ ಕಾಡುಗಳ ಹೃದಯ",
    "Offline-Ready" to "ಆಫ್‌ಲೈನ್ ಸಿದ್ಧ",
    "All" to "ಎಲ್ಲಾ",
    "Mammals" to "ಸ್ತನಧಾರಿಗಳು",
    "Birds" to "ಪಕ್ಷಿಗಳು",
    "Reptiles" to "ಸರೀಸೃಪಗಳು",
    "Amphibians" to "ಉಭಯಚರಗಳು",
    "Trees" to "ಮರಗಳು",
    "Play Forest Sound" to "ಕಾಡಿನ ಧ್ವನಿ ಪ್ಲೇ ಮಾಡಿ",
    "Play Animal Sound" to "ಪ್ರಾಣಿಯ ಧ್ವನಿ ಪ್ಲೇ ಮಾಡಿ",
    "Description" to "ವಿವರಣೆ",
    "Habitat" to "ವಾಸಸ್ಥಳ",
    "Conservation Status" to "ಸಂರಕ್ಷಣಾ ಸ್ಥಿತಿ",
    "Found in Karnataka" to "ಕರ್ನಾಟಕದಲ್ಲಿ ಕಂಡುಬರುವುದು",
    "Fun Fact" to "ಆಸಕ್ತಿದಾಯಕ ವಿಷಯ",
    "Did you know? (Powered by Gemini)" to "ನಿಮಗೆ ಗೊತ್ತೇ? (Gemini)",
    "Endangered" to "ಅಪಾಯದಲ್ಲಿದೆ",
    "Vulnerable" to "ಸಂವೇದನಾಶೀಲ",
    "Least Concern" to "ಕಡಿಮೆ ಕಾಳಜಿ",
    "Near Threatened" to "ಅಪಾಯದ ಸಮೀಪ",
    "Report Sighting" to "ಕಾಣಿಕೆ ವರದಿ",
    "Share verified details so nearby communities and forest teams can respond quickly." to "ಹತ್ತಿರದ ಜನರು ಮತ್ತು ಅರಣ್ಯ ತಂಡಗಳು ತಕ್ಷಣ ಪ್ರತಿಕ್ರಿಯಿಸಲು ಸರಿಯಾದ ವಿವರಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ.",
    "Species" to "ಜಾತಿ",
    "Description" to "ವಿವರಣೆ",
    "Landmark" to "ಗುರುತು ಸ್ಥಳ",
    "Date and Time" to "ದಿನಾಂಕ ಮತ್ತು ಸಮಯ",
    "Describe what you saw, group size, direction, and urgency..." to "ನೀವು ಕಂಡದ್ದು, ಗುಂಪಿನ ಗಾತ್ರ, ದಿಕ್ಕು ಮತ್ತು ತುರ್ತುತನ ವಿವರಿಸಿ...",
    "Or enter nearest landmark..." to "ಅಥವಾ ಹತ್ತಿರದ ಗುರುತು ಸ್ಥಳ ನಮೂದಿಸಿ...",
    "Select species" to "ಜಾತಿ ಆಯ್ಕೆಮಾಡಿ",
    "Pick date and time" to "ದಿನಾಂಕ ಮತ್ತು ಸಮಯ ಆಯ್ಕೆಮಾಡಿ",
    "Submit Sighting Report" to "ಕಾಣಿಕೆ ವರದಿ ಸಲ್ಲಿಸಿ",
    "Use Current Location" to "ಪ್ರಸ್ತುತ ಸ್ಥಳ ಬಳಸಿ",
    "Near Karnataka Forest Range" to "ಕರ್ನಾಟಕ ಅರಣ್ಯ ಪ್ರದೇಶದ ಬಳಿ",
    "Community Response" to "ಸಮುದಾಯ ಪ್ರತಿಕ್ರಿಯೆ",
    "Report with care. Keep distance. Help everyone stay safe." to "ಜಾಗರೂಕತೆಯಿಂದ ವರದಿ ಮಾಡಿ. ಅಂತರ ಕಾಯ್ದುಕೊಳ್ಳಿ. ಎಲ್ಲರ ಸುರಕ್ಷತೆಗೆ ಸಹಾಯ ಮಾಡಿ.",
    "Co-existence Guide" to "ಸಹಬಾಳ್ವೆ ಮಾರ್ಗದರ್ಶಿ",
    "Community Safety Guidelines" to "ಸಮುದಾಯ ಸುರಕ್ಷತಾ ಮಾರ್ಗಸೂಚಿಗಳು",
    "Observe From Distance" to "ದೂರದಿಂದ ಗಮನಿಸಿ",
    "Do not approach, feed, surround, photograph with flash, or block the animal's path." to "ಹತ್ತಿರ ಹೋಗಬೇಡಿ, ಆಹಾರ ಕೊಡಬೇಡಿ, ಸುತ್ತುವರಿಯಬೇಡಿ, ಫ್ಲ್ಯಾಶ್ ಬಳಸಿ ಫೋಟೋ ತೆಗೆಯಬೇಡಿ ಅಥವಾ ದಾರಿ ತಡೆಯಬೇಡಿ.",
    "Move As A Group" to "ಗುಂಪಾಗಿ ಚಲಿಸಿ",
    "Keep children and livestock indoors, avoid isolated travel, and alert nearby residents calmly." to "ಮಕ್ಕಳು ಮತ್ತು ಪಶುಗಳನ್ನು ಒಳಗೆ ಇರಿಸಿ, ಒಬ್ಬರೇ ಪ್ರಯಾಣ ತಪ್ಪಿಸಿ, ಹತ್ತಿರದ ಜನರಿಗೆ ಶಾಂತವಾಗಿ ತಿಳಿಸಿ.",
    "Call Forest Support" to "ಅರಣ್ಯ ಸಹಾಯಕ್ಕೆ ಕರೆ ಮಾಡಿ",
    "For immediate danger, contact the forest helpline or local authorities with landmark and species details." to "ತಕ್ಷಣದ ಅಪಾಯ ಇದ್ದರೆ ಗುರುತು ಸ್ಥಳ ಮತ್ತು ಜಾತಿ ವಿವರಗಳೊಂದಿಗೆ ಅರಣ್ಯ ಸಹಾಯವಾಣಿ ಅಥವಾ ಸ್ಥಳೀಯ ಅಧಿಕಾರಿಗಳನ್ನು ಸಂಪರ್ಕಿಸಿ.",
    "Elephant in your field?" to "ನಿಮ್ಮ ಹೊಲದಲ್ಲಿ ಆನೆ ಇದೆಯೇ?",
    "Maintain a distance of at least 50-100 meters." to "ಕನಿಷ್ಠ 50-100 ಮೀಟರ್ ಅಂತರ ಕಾಯ್ದುಕೊಳ್ಳಿ.",
    "Avoid using bright camera flashes or loud honking." to "ತೀವ್ರ ಫ್ಲ್ಯಾಶ್ ಅಥವಾ ಜೋರಾದ ಹಾರ್ನ್ ತಪ್ಪಿಸಿ.",
    "Check for calves; mothers are extremely protective." to "ಮರಿಗಳಿವೆಯೇ ನೋಡಿ; ತಾಯಿ ಆನೆಗಳು ತುಂಬಾ ರಕ್ಷಣಾತ್ಮಕವಾಗಿರುತ್ತವೆ.",
    "Predator Encounter (Tiger/Leopard)" to "ಹುಲಿ/ಚಿರತೆ ಎದುರಾದರೆ",
    "Do not run. Back away slowly while facing the animal." to "ಓಡಬೇಡಿ. ಪ್ರಾಣಿಯನ್ನು ನೋಡುತ್ತಲೇ ನಿಧಾನವಾಗಿ ಹಿಂದೆ ಸರಿಯಿರಿ.",
    "Make yourself look larger by raising your arms." to "ಕೈಗಳನ್ನು ಎತ್ತಿ ನಿಮ್ಮನ್ನು ದೊಡ್ಡದಾಗಿ ಕಾಣುವಂತೆ ಮಾಡಿ.",
    "Make noise in a calm, firm voice; do not scream." to "ಶಾಂತ ಆದರೆ ದೃಢ ಧ್ವನಿಯಲ್ಲಿ ಮಾತಾಡಿ; ಕಿರುಚಬೇಡಿ.",
    "Local Awareness" to "ಸ್ಥಳೀಯ ಜಾಗೃತಿ",
    "Respect Their Space" to "ಅವುಗಳ ಸ್ಥಳವನ್ನು ಗೌರವಿಸಿ",
    "Essential Safety Quick-Tips" to "ಮುಖ್ಯ ಸುರಕ್ಷತಾ ಸಲಹೆಗಳು",
    "Dim Lights" to "ಬೆಳಕು ಕಡಿಮೆ ಮಾಡಿ",
    "Keep Quiet" to "ಶಾಂತವಾಗಿರಿ",
    "Stay in Groups" to "ಗುಂಪಿನಲ್ಲಿರಿ",
    "Helpline: 1926" to "ಸಹಾಯವಾಣಿ: 1926",
    "Real-time Alert Feed" to "ತಕ್ಷಣದ ಎಚ್ಚರಿಕೆಗಳು",
    "Active sightings from the last 6 hours. Stay informed and ensure the safety of both wildlife and community." to "ಕಳೆದ 6 ಗಂಟೆಗಳ ಸಕ್ರಿಯ ಕಾಣಿಕೆಗಳು. ವನ್ಯಜೀವಿ ಮತ್ತು ಸಮುದಾಯದ ಸುರಕ್ಷತೆಗೆ ಮಾಹಿತಿ ಹೊಂದಿರಿ.",
    "URGENT" to "ತುರ್ತು",
    "No active alerts in your area" to "ನಿಮ್ಮ ಪ್ರದೇಶದಲ್ಲಿ ಸಕ್ರಿಯ ಎಚ್ಚರಿಕೆಗಳಿಲ್ಲ",
    "The feed will update as verified sightings arrive." to "ಪರಿಶೀಲಿಸಿದ ಕಾಣಿಕೆಗಳು ಬಂದಂತೆ ಪಟ್ಟಿ ನವೀಕರಿಸುತ್ತದೆ.",
    "Alerts are automatically removed after 6 hours to ensure feed relevance." to "ಪ್ರಸ್ತುತತೆಯನ್ನು ಕಾಪಾಡಲು ಎಚ್ಚರಿಕೆಗಳು 6 ಗಂಟೆಗಳ ನಂತರ ಸ್ವಯಂ ತೆಗೆದುಹಾಕಲಾಗುತ್ತವೆ.",
    "Alert Details" to "ಎಚ್ಚರಿಕೆ ವಿವರಗಳು",
    "Location" to "ಸ್ಥಳ",
    "Reported At" to "ವರದಿ ಸಮಯ",
    "Reporter" to "ವರದಿಗಾರ",
    "Safety Advice" to "ಸುರಕ್ಷತಾ ಸಲಹೆ",
    "Priority Alert" to "ಆದ್ಯತಾ ಎಚ್ಚರಿಕೆ",
    "Please maintain a safe distance and do not attempt to approach the animal. Notify local authorities if the situation escalates." to "ಸುರಕ್ಷಿತ ಅಂತರ ಕಾಯ್ದುಕೊಳ್ಳಿ ಮತ್ತು ಪ್ರಾಣಿಯ ಹತ್ತಿರ ಹೋಗಬೇಡಿ. ಪರಿಸ್ಥಿತಿ ಗಂಭೀರವಾದರೆ ಸ್ಥಳೀಯ ಅಧಿಕಾರಿಗಳಿಗೆ ತಿಳಿಸಿ.",
    "High" to "ಹೆಚ್ಚು",
    "Medium" to "ಮಧ್ಯಮ",
    "Low" to "ಕಡಿಮೆ",
    "Community Reporter" to "ಸಮುದಾಯ ವರದಿಗಾರ",
    "Sighting alert posted to Firebase." to "ಕಾಣಿಕೆ ಎಚ್ಚರಿಕೆ ಸಲ್ಲಿಸಲಾಗಿದೆ.",
    "Complete all report fields before submitting." to "ಸಲ್ಲಿಸುವ ಮೊದಲು ಎಲ್ಲಾ ವಿವರಗಳನ್ನು ತುಂಬಿ.",
    "Could not post alert. Check Firebase configuration." to "ಎಚ್ಚರಿಕೆ ಸಲ್ಲಿಸಲಾಗಲಿಲ್ಲ. Firebase ಸೆಟ್ಟಿಂಗ್ ಪರಿಶೀಲಿಸಿ."
)

private val wildlifeText = mapOf(
    "asian_elephant" to WildlifeKn("ಆನೆ", "ಆನೆ ಕಾಡಿನ ದಾರಿಗಳನ್ನು ತೆರೆಯುವ, ಬೀಜಗಳನ್ನು ಹರಡುವ ಮತ್ತು ನೀರಿನ ಮೂಲಗಳನ್ನು ಉಳಿಸುವ ಪ್ರಮುಖ ಜೀವಿ.", "ಸದಾಕಾಲ ಹಸಿರು ಕಾಡು, ಎಲೆಬೀಳು ಕಾಡು, ಹುಲ್ಲುಗಾವಲು ಮತ್ತು ನದಿತೀರದ ದಾರಿಗಳು.", "ಆನೆಗಳು ಕಡಿಮೆ ಆವೃತ್ತಿಯ ಧ್ವನಿಗಳಿಂದ ಪರಸ್ಪರ ಸಂಪರ್ಕಿಸುತ್ತವೆ."),
    "bengal_tiger" to WildlifeKn("ಹುಲಿ", "ಹುಲಿ ಕರ್ನಾಟಕದ ಪ್ರಮುಖ ಅರಣ್ಯ ಬೇಟೆಗಾರ. ಆರೋಗ್ಯಕರ ಕಾಡಿನ ಸಮತೋಲನವನ್ನು ಕಾಪಾಡಲು ಇದು ಮುಖ್ಯ.", "ಎಲೆಬೀಳು ಕಾಡು, ಹುಲ್ಲುಗಾವಲು, ಬಿದಿರು ಪ್ರದೇಶಗಳು ಮತ್ತು ನೀರಿನ ದಾರಿಗಳು.", "ಪ್ರತಿ ಹುಲಿಯ ಪಟ್ಟೆಗಳ ಮಾದರಿ ವಿಭಿನ್ನವಾಗಿರುತ್ತದೆ."),
    "indian_leopard" to WildlifeKn("ಚಿರತೆ", "ಚಿರತೆಗಳು ಕಾಡು, ಕಲ್ಲಿನ ಪ್ರದೇಶ, ತೋಟಗಳು ಮತ್ತು ಗ್ರಾಮ ಅಂಚುಗಳಲ್ಲೂ ಬದುಕಬಲ್ಲ ಚುರುಕು ದೊಡ್ಡ ಬೆಕ್ಕುಗಳು.", "ಒಣ ಕಾಡು, ಕಲ್ಲಿನ ಗುಡ್ಡ, ತೋಟಗಳು ಮತ್ತು ಗ್ರಾಮ ಅಂಚುಗಳು.", "ಚಿರತೆಗಳು ಭಾರವಾದ ಬೇಟೆಯನ್ನು ಮರಕ್ಕೆ ಎತ್ತಿಕೊಂಡು ಹೋಗಬಲ್ಲವು."),
    "black_panther" to WildlifeKn("ಕಪ್ಪು ಚಿರತೆ", "ಕಪ್ಪು ಚಿರತೆಗಳು ಗಾಢ ಬಣ್ಣದ ಚಿರತೆಗಳು. ದಟ್ಟ ಕಾಡಿನ ನೆರಳಿನಲ್ಲಿ ಅವು ಸುಲಭವಾಗಿ ಮರೆಯಾಗುತ್ತವೆ.", "ದಟ್ಟ ಕಾಡು, ತೇವ ಕಾಡು, ತೋಟಗಳು ಮತ್ತು ಅರಣ್ಯ ದಾರಿಗಳು.", "ಬೆಳಕಿನಲ್ಲಿ ಅವುಗಳ ಚುಕ್ಕೆ ಮಾದರಿ ಇನ್ನೂ ಕಾಣಬಹುದು."),
    "sloth_bear" to WildlifeKn("ಕರಡಿ", "ಕರಡಿಗಳು ಒಣ ಕಾಡು ಮತ್ತು ಕಲ್ಲಿನ ಪ್ರದೇಶಗಳಲ್ಲಿ ಇರುವ, ಕೀಟ ಮತ್ತು ಹಣ್ಣುಗಳನ್ನು ತಿನ್ನುವ ಜೀವಿಗಳು.", "ಒಣ ಎಲೆಬೀಳು ಕಾಡು, ಪೊದೆ ಕಾಡು ಮತ್ತು ಕಲ್ಲಿನ ಗುಡ್ಡಗಳು.", "ತಾಯಿ ಕರಡಿಗಳು ಮರಿಗಳನ್ನು ಬೆನ್ನಿನ ಮೇಲೆ ಹೊತ್ತುಕೊಂಡು ಹೋಗುತ್ತವೆ."),
    "wild_boar" to WildlifeKn("ಕಾಡು ಹಂದಿ", "ಕಾಡು ಹಂದಿ ಸಾಮಾನ್ಯವಾಗಿ ರಾತ್ರಿ ಚಟುವಟಿಕೆಯಿಂದಿರುತ್ತದೆ ಮತ್ತು ಹೊಲ-ಕಾಡು ಅಂಚುಗಳಲ್ಲಿ ಆಹಾರ ಹುಡುಕುತ್ತದೆ.", "ಕೃಷಿ ಅಂಚು, ಪೊದೆ ಪ್ರದೇಶ ಮತ್ತು ಕಾಡು-ಹೊಲ ಗಡಿಗಳು.", "ಕಾಡು ಹಂದಿಗಳು ಮೂಗಿನ ಸಹಾಯದಿಂದ ನೆಲವನ್ನು ಆಳವಾಗಿ ಅಗೆಬಲ್ಲವು."),
    "gaur" to WildlifeKn("ಕಾಡೆಮ್ಮೆ", "ಕಾಡೆಮ್ಮೆ ದೊಡ್ಡ ಮತ್ತು ಬಲಿಷ್ಠ ಕಾಡು ಎಮ್ಮೆ. ಹತ್ತಿರ ಕಂಡರೆ ಸುರಕ್ಷಿತ ಅಂತರ ಕಾಯ್ದುಕೊಳ್ಳಬೇಕು.", "ತೇವ ಕಾಡು, ಸದಾಕಾಲ ಹಸಿರು ಕಾಡಿನ ಅಂಚು ಮತ್ತು ಹುಲ್ಲಿನ ತೆರವು ಪ್ರದೇಶಗಳು.", "ಕಾಡೆಮ್ಮೆ ವಿಶ್ವದ ಅತಿ ದೊಡ್ಡ ಕಾಡು ಎಮ್ಮೆಗಳಲ್ಲಿ ಒಂದು."),
    "nilgiri_tahr" to WildlifeKn("ನೀಲಗಿರಿ ತಾರ್", "ಪಶ್ಚಿಮ ಘಟ್ಟದ ಎತ್ತರದ ಹುಲ್ಲುಗಾವಲು ಮತ್ತು ಬಂಡೆ ಪ್ರದೇಶಗಳಿಗೆ ಹೊಂದಿಕೊಂಡಿರುವ ಪರ್ವತ ಜೀವಿ.", "ಎತ್ತರದ ಹುಲ್ಲುಗಾವಲು ಮತ್ತು ಕಲ್ಲಿನ ಬಂಡೆಗಳು.", "ಇವುಗಳು ಅತ್ಯುತ್ತಮ ಬಂಡೆ ಏರುವ ಜೀವಿಗಳು."),
    "king_cobra" to WildlifeKn("ಕಾಳಿಂಗ ಸರ್ಪ", "ಕಾಳಿಂಗ ಸರ್ಪ ವಿಶ್ವದ ಅತಿ ಉದ್ದದ ವಿಷಸರ್ಪ. ಇದನ್ನು ಕಂಡರೆ ತರಬೇತಿ ಪಡೆದ ರಕ್ಷಣಾ ತಂಡಕ್ಕೆ ತಿಳಿಸಬೇಕು.", "ಮಳೆಕಾಡು, ಬಿದಿರು ಪ್ರದೇಶ, ತೋಟಗಳು ಮತ್ತು ಹೊಳೆ ಅಂಚುಗಳು.", "ಇದು ಮುಖ್ಯವಾಗಿ ಇತರೆ ಹಾವುಗಳನ್ನು ತಿನ್ನುತ್ತದೆ."),
    "mugger_crocodile" to WildlifeKn("ಮಗ್ಗರ್ ಮೊಸಳೆ", "ಮಗ್ಗರ್ ಮೊಸಳೆ ನದಿ, ಕೆರೆ ಮತ್ತು ಜಲಾಶಯಗಳಲ್ಲಿ ಕಂಡುಬರುವ ಪ್ರಮುಖ ಜಲಚರ ಬೇಟೆಗಾರ.", "ತಾಜಾ ನೀರಿನ ನದಿ, ಕೆರೆ, ಜಲಾಶಯ ಮತ್ತು ಸೊಂಪು ಪ್ರದೇಶಗಳು.", "ಮೊಸಳೆಗಳು ಬೇಟೆಯ ವೇಳೆ ಬಹಳ ಹೊತ್ತು ನಿಶ್ಚಲವಾಗಿರುತ್ತವೆ."),
    "giant_squirrel" to WildlifeKn("ದೊಡ್ಡ ಅಳಿಲು", "ಮಲಬಾರ್ ದೊಡ್ಡ ಅಳಿಲು ಬಣ್ಣದ ಮರ ಅಳಿಲು. ಇದು ಮರದಿಂದ ಮರಕ್ಕೆ ದೂರ ಜಿಗಿಯಲು ಪ್ರಸಿದ್ಧ.", "ಸದಾಕಾಲ ಹಸಿರು ಮತ್ತು ತೇವ ಎಲೆಬೀಳು ಕಾಡಿನ ಮೇಲ್ಚಾವಣಿ.", "ಇವು ಮರಗಳ ನಡುವೆ ಆರು ಮೀಟರ್‌ವರೆಗೆ ಜಿಗಿಯಬಹುದು."),
    "common_indian_toad" to WildlifeKn("ಭಾರತೀಯ ಕಪ್ಪೆ", "ಸಾಮಾನ್ಯ ಭಾರತೀಯ ಕಪ್ಪೆ ಮಳೆಯ ನಂತರ ತೋಟ, ಹೊಲ ಮತ್ತು ಕಾಡಿನ ಅಂಚುಗಳಲ್ಲಿ ಕಾಣುತ್ತದೆ.", "ತೇವ ತೋಟಗಳು, ಹೊಲಗಳು, ಕೆರೆಗಳು ಮತ್ತು ಮಳೆನೀರು ಸೇರುವ ಪ್ರದೇಶಗಳು.", "ಮಳೆಗಾಲದ ರಾತ್ರಿ ಇವುಗಳ ಧ್ವನಿ ಹೆಚ್ಚು ಕೇಳಿಸುತ್ತದೆ."),
    "malabar_gliding_frog" to WildlifeKn("ಮಲಬಾರ್ ಗ್ಲೈಡಿಂಗ್ ಕಪ್ಪೆ", "ಪಶ್ಚಿಮ ಘಟ್ಟದ ಹಸಿರು ಮರಕಪ್ಪೆ. ಜಾಲದಂತಿರುವ ಪಾದಗಳಿಂದ ಕೊಂಬೆಗಳ ನಡುವೆ ಗ್ಲೈಡ್ ಮಾಡುತ್ತದೆ.", "ತೇವ ಸದಾಕಾಲ ಹಸಿರು ಕಾಡು, ಹೊಳೆ ಅಂಚು ಮತ್ತು ಮಳೆಕಾಲದ ಕೊಳಗಳು.", "ಇವು ನೀರಿನ ಮೇಲಿರುವ ಕೊಂಬೆಗಳಲ್ಲಿ ನುರೆಯ ಗೂಡು ಮಾಡುತ್ತವೆ."),
    "great_hornbill" to WildlifeKn("ದೊಡ್ಡ ಕೊಂಬುಕೊಕ್ಕರೆ", "ದೊಡ್ಡ ಕೊಂಬುಕೊಕ್ಕರೆ ಪಶ್ಚಿಮ ಘಟ್ಟದ ಗಮನ ಸೆಳೆಯುವ ಪಕ್ಷಿ. ಕಾಡಿನ ಬೀಜ ಹರಡುವಿಕೆಯಲ್ಲಿ ಬಹಳ ಮುಖ್ಯ.", "ಸದಾಕಾಲ ಹಸಿರು ಕಾಡು, ತೇವ ಕಾಡು ಮತ್ತು ದೊಡ್ಡ ಹಣ್ಣು ಮರಗಳು.", "ಇವುಗಳ ರೆಕ್ಕೆ ಬಡಿಯುವ ಧ್ವನಿ ದೂರದಿಂದಲೂ ಕೇಳಿಸುತ್ತದೆ."),
    "indian_peafowl" to WildlifeKn("ನವಿಲು", "ನವಿಲು ಭಾರತದ ರಾಷ್ಟ್ರೀಯ ಪಕ್ಷಿ. ಗ್ರಾಮ ಅಂಚು, ಹೊಲ ಮತ್ತು ಕಾಡುಗಳಲ್ಲಿ ಸಾಮಾನ್ಯವಾಗಿ ಕಾಣುತ್ತದೆ.", "ಒಣ ಕಾಡು, ಪೊದೆ ಪ್ರದೇಶ, ಕೃಷಿ ಅಂಚು ಮತ್ತು ಗ್ರಾಮ ತೋಪುಗಳು.", "ಗಂಡು ನವಿಲುಗಳಿಗಷ್ಟೇ ಉದ್ದವಾದ ಬಣ್ಣದ ಬಾಲ ಇರುತ್ತದೆ."),
    "sandalwood" to WildlifeKn("ಶ್ರೀಗಂಧ", "ಶ್ರೀಗಂಧ ಕರ್ನಾಟಕದ ಸಾಂಸ್ಕೃತಿಕ ಮತ್ತು ಆರ್ಥಿಕವಾಗಿ ಮಹತ್ವದ ಮರ. ಸುಗಂಧ ಮರದ ಭಾಗಕ್ಕಾಗಿ ಪ್ರಸಿದ್ಧ.", "ಒಣ ಎಲೆಬೀಳು ಕಾಡು, ಪೊದೆ ಪ್ರದೇಶ, ಕಲ್ಲಿನ ಇಳಿಜಾರು ಮತ್ತು ನೀರು ಸರಿಯುವ ಮಣ್ಣು.", "ಶ್ರೀಗಂಧದ ಸುಗಂಧ ಮುಖ್ಯವಾಗಿ ಪಕ್ವ ಹೃದಯಕಟ್ಟೆಯಿಂದ ಬರುತ್ತದೆ."),
    "rosewood" to WildlifeKn("ಬೀಟೆ ಮರ", "ಬೀಟೆ ಮರ ಪಶ್ಚಿಮ ಘಟ್ಟದ ದೊಡ್ಡ ಮರ. ಬದುಕಿರುವ ಮರಗಳು ನೆರಳು, ವಾಸಸ್ಥಳ ಮತ್ತು ಕಾಡಿನ ಪುನರುಜ್ಜೀವನಕ್ಕೆ ಮುಖ್ಯ.", "ತೇವ ಎಲೆಬೀಳು ಕಾಡು, ನದಿ ಕಣಿವೆಗಳು ಮತ್ತು ಮಿಶ್ರ ಕಾಡು.", "ಹಳೆಯ ಮರಗಳು ಪಕ್ಷಿ ಮತ್ತು ಪ್ರಾಣಿಗಳಿಗೆ ಆಶ್ರಯ ಕೊಡುತ್ತವೆ."),
    "teak" to WildlifeKn("ತೇಗ", "ತೇಗ ದೊಡ್ಡ ಎಲೆಗಳಿರುವ ಎಲೆಬೀಳು ಮರ. ಕಾಡಿನಲ್ಲಿ ನೆರಳು, ಎಲೆಪದರ ಮತ್ತು ಜೀವಿಗಳಿಗೆ ಆಶ್ರಯ ನೀಡುತ್ತದೆ.", "ಒಣ ಮತ್ತು ತೇವ ಎಲೆಬೀಳು ಕಾಡು, ಗುಡ್ಡದ ಇಳಿಜಾರು ಮತ್ತು ಉತ್ತಮ ನೀರು ಸರಿಯುವ ಮಣ್ಣು.", "ತೇಗದ ಮರದಲ್ಲಿ ಕುಳುಚುವಿಕೆ ಕಡಿಮೆ ಮಾಡುವ ನೈಸರ್ಗಿಕ ಎಣ್ಣೆ ಇರುತ್ತದೆ.")
)
