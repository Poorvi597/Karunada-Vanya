package com.karunadavanya.data

import com.karunadavanya.R
import com.karunadavanya.data.local.AlertEntity
import com.karunadavanya.data.local.WildlifeEntity
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.WildlifeCategory

object SampleData {
    val wildlife = listOf(
        WildlifeEntity(
            id = "asian_elephant",
            name = "Elephant",
            scientificName = "Elephas maximus",
            category = WildlifeCategory.Mammals.name,
            description = "The Asian elephant is a keystone species that opens forest paths, disperses large seeds, and maintains water access used by many other animals. Karnataka forms part of one of India's most important elephant landscapes.",
            habitat = "Evergreen forests, moist deciduous forests, dry deciduous forests, scrub forests, grasslands, and riverine corridors.",
            conservationStatus = "Endangered",
            locations = "Nagarhole Tiger Reserve|Bandipur Tiger Reserve|Cauvery Wildlife Sanctuary|BRT Tiger Reserve",
            funFacts = "Elephants communicate with low-frequency rumbles humans may not hear|Matriarchs guide herds using long memory of routes and water|Calves are protected closely by the entire family group",
            imageResId = R.drawable.elephant,
            soundResId = R.raw.elephant,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "bengal_tiger",
            name = "Tiger",
            scientificName = "Panthera tigris tigris",
            category = WildlifeCategory.Mammals.name,
            description = "The tiger is Karnataka's apex forest predator and a flagship species for connected protected landscapes. In reserves such as Bandipur, Nagarhole, Bhadra, and Kali, tigers regulate herbivore populations and indicate the health of prey-rich forests.",
            habitat = "Moist deciduous forests, dry deciduous forests, teak forests, grassland mosaics, bamboo patches, and riparian corridors.",
            conservationStatus = "Endangered",
            locations = "Bandipur Tiger Reserve|Nagarhole Tiger Reserve|Bhadra Tiger Reserve|Kali Tiger Reserve",
            funFacts = "Every tiger has a unique stripe pattern|Tigers are strong swimmers and often use water to cool down|A tiger's roar can carry for several kilometres in quiet forest",
            imageResId = R.drawable.tiger,
            soundResId = R.raw.tiger,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "indian_leopard",
            name = "Leopard",
            scientificName = "Panthera pardus fusca",
            category = WildlifeCategory.Mammals.name,
            description = "Leopards are adaptable big cats that move through forests, rocky scrub, plantations, and village edges. In Karnataka they are often seen near forest-fringe landscapes where livestock and dogs can attract them close to settlements.",
            habitat = "Dry deciduous forest, rocky scrub, plantations, ravines, and forest-edge farmland.",
            conservationStatus = "Vulnerable",
            locations = "Bannerghatta National Park|BRT Tiger Reserve|Mysuru forest fringes|Ramanagara rocky scrub",
            funFacts = "Leopards can carry heavy prey into trees|They are mostly active at night|Their rosette patterns help them disappear in dappled shade",
            imageResId = R.drawable.leopard,
            soundResId = R.raw.leopard,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "black_panther",
            name = "Black Panther",
            scientificName = "Panthera pardus fusca",
            category = WildlifeCategory.Mammals.name,
            description = "Black panthers in Karnataka are melanistic leopards, carrying the same stealth, strength, and adaptability as spotted leopards with a dark coat that helps them disappear in dense forest shade.",
            habitat = "Evergreen forests, moist deciduous forests, dense plantations, rocky slopes, and forest corridors.",
            conservationStatus = "Vulnerable",
            locations = "Kabini backwaters|Nagarhole Tiger Reserve|Dandeli forests|Western Ghats forest corridors",
            funFacts = "Black panthers are leopards with melanism|Their rosette patterns can still be visible in strong light|They are elusive and mostly active at dusk, night, and dawn",
            imageResId = R.drawable.black_panther,
            soundResId = R.raw.black_panther,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "sloth_bear",
            name = "Sloth Bear",
            scientificName = "Melursus ursinus",
            category = WildlifeCategory.Mammals.name,
            description = "Sloth bears are shaggy, insect-loving bears of dry forests and scrublands. Karnataka's Daroji Sloth Bear Sanctuary is one of the best known landscapes for this species, where bears forage for termites, fruits, honey, and mahua flowers around rocky outcrops.",
            habitat = "Dry deciduous forest, scrub forest, rocky hills, and fruiting tree patches.",
            conservationStatus = "Vulnerable",
            locations = "Daroji Sloth Bear Sanctuary|BRT Tiger Reserve|Bannerghatta National Park|Kappatagudda landscape",
            funFacts = "Sloth bears can vacuum termites through a gap in their teeth|Mothers often carry cubs on their backs|They love honey and mahua flowers",
            imageResId = R.drawable.sloth_bear,
            soundResId = R.raw.sloth_bear,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "wild_boar",
            name = "Wild Boar",
            scientificName = "Sus scrofa cristatus",
            category = WildlifeCategory.Mammals.name,
            description = "The Indian wild boar is one of Karnataka's most common mammals. It is hardy, fast-breeding, and usually active at night, moving through fields, scrub, and forest edges in search of roots, tubers, and cultivated crops.",
            habitat = "Agricultural edges, dry scrub, moist deciduous forest, and forest-farm boundaries.",
            conservationStatus = "Least Concern",
            locations = "Bandipur buffer villages|Malenadu farm edges|Cauvery Wildlife Sanctuary fringe|Bengaluru rural scrublands",
            funFacts = "Wild boars can dig deeply with their snouts|They often move in small groups called sounders|They adapt quickly to crops and disturbed habitats",
            imageResId = R.drawable.wild_boar,
            soundResId = R.raw.wild_boar,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "gaur",
            name = "Gaur",
            scientificName = "Bos gaurus",
            category = WildlifeCategory.Mammals.name,
            description = "Gaur, also called Indian bison, are powerful wild cattle found in Karnataka's forested hill ranges. Large herds may graze near forest roads and plantations, so people should keep distance and avoid sudden movement.",
            habitat = "Moist deciduous forest, evergreen forest edges, grassy clearings, and hill slopes.",
            conservationStatus = "Vulnerable",
            locations = "Bandipur Tiger Reserve|Nagarhole Tiger Reserve|Bhadra Tiger Reserve|Kudremukh landscape",
            funFacts = "Gaur are the largest wild cattle in the world|Adults have a high shoulder ridge and pale stockings|They usually move in herds led by experienced females",
            imageResId = R.drawable.gaur,
            soundResId = R.raw.gaur,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "nilgiri_tahr",
            name = "Nilgiri Tahr",
            scientificName = "Nilgiritragus hylocrius",
            category = WildlifeCategory.Mammals.name,
            description = "A sturdy mountain goat endemic to the high-altitude shola-grassland mosaic of the Western Ghats. They are incredible rock climbers found on steep cliffs.",
            habitat = "High altitude grasslands and rocky cliffs.",
            conservationStatus = "Endangered",
            locations = "Mullaianagiri|Baba Budangiri|Brahmagiri hills",
            funFacts = "They are excellent rock climbers|They live in social groups called herds|They are endemic to the Western Ghats",
            imageResId = R.drawable.nilgiri_tahr,
            soundResId = R.raw.nilgiri_tahr,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "king_cobra",
            name = "King Cobra",
            scientificName = "Ophiophagus hannah",
            category = WildlifeCategory.Reptiles.name,
            description = "The world's longest venomous snake, strongly associated with the Western Ghats. Sightings should be reported to trained rescue teams. It is the only snake that builds a nest for its eggs.",
            habitat = "Rainforests, bamboo thickets, plantations, and forest edges near streams.",
            conservationStatus = "Vulnerable",
            locations = "Agumbe Rainforest|Kudremukh National Park|Dandeli forests",
            funFacts = "It mainly eats other snakes|It can raise a third of its body when threatened|A single bite can be fatal to an elephant",
            imageResId = R.drawable.king_cobra,
            soundResId = R.raw.king_cobra,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "mugger_crocodile",
            name = "Mugger Crocodile",
            scientificName = "Crocodylus palustris",
            category = WildlifeCategory.Reptiles.name,
            description = "The Mugger Crocodile is a broad-snouted freshwater crocodile found in Karnataka's rivers, reservoirs, lakes, and marshes. It is an important wetland predator that helps keep aquatic ecosystems balanced.",
            habitat = "Freshwater rivers, reservoirs, irrigation tanks, marshes, and slow-moving wetland edges.",
            conservationStatus = "Vulnerable",
            locations = "Ranganathittu Bird Sanctuary|Cauvery river stretches|Kabini backwaters|Tungabhadra reservoir",
            funFacts = "Muggers bask on riverbanks to regulate body temperature|They can stay motionless for long periods while hunting|Mothers guard nests and young hatchlings closely",
            imageResId = R.drawable.crocodile,
            soundResId = R.raw.crocodile,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "giant_squirrel",
            name = "Giant Squirrel",
            scientificName = "Ratufa indica",
            category = WildlifeCategory.Mammals.name,
            description = "The Malabar giant squirrel is a large, colorful tree squirrel endemic to the forests of peninsular India. It is known for its spectacular leaps and vibrant multi-colored fur.",
            habitat = "Evergreen and moist deciduous forest canopies.",
            conservationStatus = "Least Concern",
            locations = "Bhadra Wildlife Sanctuary|Kudremukh National Park|Kodagu forests",
            funFacts = "They can leap up to 6 meters between trees|They build multiple spherical nests|They are mostly active in the mornings and evenings",
            imageResId = R.drawable.giant_squirrel,
            soundResId = R.raw.giant_squirrel,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "common_indian_toad",
            name = "Common Indian Toad",
            scientificName = "Duttaphrynus melanostictus",
            category = WildlifeCategory.Amphibians.name,
            description = "The Common Indian Toad is a hardy amphibian often seen around gardens, farms, and forest edges after rain. It feeds on insects and is a helpful natural pest controller in human-dominated landscapes.",
            habitat = "Moist gardens, agricultural fields, forest edges, ponds, drains, and temporary rain pools.",
            conservationStatus = "Least Concern",
            locations = "Bengaluru urban wetlands|Malenadu villages|Western Ghats foothills|Farm ponds across Karnataka",
            funFacts = "Toads absorb moisture through their skin|They emerge in large numbers during monsoon nights|Their calls help males find mates near breeding pools",
            imageResId = R.drawable.indian_toad,
            soundResId = R.raw.indian_toad,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "malabar_gliding_frog",
            name = "Malabar Gliding Frog",
            scientificName = "Rhacophorus malabaricus",
            category = WildlifeCategory.Amphibians.name,
            description = "The Malabar Gliding Frog is a vivid green tree frog of the Western Ghats, famous for webbed feet that let it glide between branches. It breeds near forest pools during the monsoon.",
            habitat = "Evergreen forest canopy, streamside vegetation, monsoon pools, and humid Western Ghats forests.",
            conservationStatus = "Least Concern",
            locations = "Agumbe Rainforest|Kudremukh National Park|Sharavathi Valley|Kodagu forests",
            funFacts = "It can glide using large webbed feet|Foam nests are built above water|Its bright green color blends with wet leaves",
            imageResId = R.drawable.gliding_frog,
            soundResId = R.raw.gliding_frog,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "great_hornbill",
            name = "Great Indian Hornbill",
            scientificName = "Buceros bicornis",
            category = WildlifeCategory.Birds.name,
            description = "The Great Indian Hornbill is one of the Western Ghats' most striking forest birds, known for its massive yellow casque, sweeping wingbeats, and vital role in dispersing seeds across mature forests.",
            habitat = "Evergreen forests, moist deciduous forests, tall fruiting trees, and protected Western Ghats canopy habitats.",
            conservationStatus = "Vulnerable",
            locations = "Dandeli forests|Anshi-Kali Tiger Reserve|Bhadra Wildlife Sanctuary|Kudremukh landscape",
            funFacts = "Hornbills disperse large forest seeds|Their wingbeats can sound like a distant train|Females seal themselves inside tree cavities while nesting",
            imageResId = R.drawable.hornbill,
            soundResId = R.raw.hornbill,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "indian_peafowl",
            name = "Indian Peafowl",
            scientificName = "Pavo cristatus",
            category = WildlifeCategory.Birds.name,
            description = "The Indian Peafowl is India's national bird and a familiar sight in scrublands, village edges, farms, and protected forests. Its loud calls often announce rain, predators, or territorial activity.",
            habitat = "Dry deciduous forest, scrubland, agricultural edges, village groves, and open woodland.",
            conservationStatus = "Least Concern",
            locations = "Ranganathittu Bird Sanctuary|Bannerghatta National Park|Mysuru countryside|Cauvery Wildlife Sanctuary",
            funFacts = "Only males grow the long colorful train|Peafowl roost in trees at night|Their loud calls can warn other animals of predators",
            imageResId = R.drawable.peacock,
            soundResId = R.raw.peacock,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "sandalwood",
            name = "Sandalwood",
            scientificName = "Santalum album",
            category = WildlifeCategory.Trees.name,
            description = "Sandalwood is one of Karnataka's most culturally and economically important native trees, valued for its fragrant heartwood and essential oil. It grows slowly and is protected because illegal cutting has severely reduced wild populations.",
            habitat = "Dry deciduous forests, scrub forests, rocky slopes, and well-drained red or sandy soils.",
            conservationStatus = "Vulnerable",
            locations = "Mysuru region|Chamarajanagar forests|Mandya dry zones|Eastern Karnataka scrub forests",
            funFacts = "Karnataka is historically famous for sandalwood|The fragrance comes mainly from mature heartwood|Sandalwood is partly root-parasitic and draws nutrients from nearby plants",
            imageResId = R.drawable.sandalwood,
            soundResId = null,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "rosewood",
            name = "Rosewood",
            scientificName = "Dalbergia latifolia",
            category = WildlifeCategory.Trees.name,
            description = "Indian Rosewood is a large hardwood tree of the Western Ghats and moist deciduous forests. Its dense dark timber is prized, but living trees are far more valuable as habitat, shade, carbon storage, and seed sources for forest recovery.",
            habitat = "Moist deciduous forests, semi-evergreen forest edges, river valleys, and mixed Western Ghats woodland.",
            conservationStatus = "Vulnerable",
            locations = "Dandeli forests|Kodagu forests|Bhadra Wildlife Sanctuary|Kali Tiger Reserve",
            funFacts = "Rosewood timber is naturally dark and durable|Its flowers support insects and pollinators|Old trees provide cavities and shade for forest wildlife",
            imageResId = R.drawable.rosewood,
            soundResId = null,
            soundUrl = null
        ),
        WildlifeEntity(
            id = "teak",
            name = "Teak",
            scientificName = "Tectona grandis",
            category = WildlifeCategory.Trees.name,
            description = "Teak is a tall deciduous tree known for broad leaves and durable timber. In Karnataka's forests, teak stands provide shade, leaf litter, nesting structure, and seasonal resources for insects, birds, and mammals.",
            habitat = "Dry and moist deciduous forests, forest plantations, hill slopes, and well-drained alluvial or lateritic soils.",
            conservationStatus = "Least Concern",
            locations = "Dandeli forests|Bandipur landscape|Bhadra Tiger Reserve|Cauvery Wildlife Sanctuary",
            funFacts = "Teak sheds its large leaves in the dry season|Its timber contains natural oils that resist decay|Teak flowers attract many small pollinators",
            imageResId = R.drawable.teak,
            soundResId = null,
            soundUrl = null
        )
    )

    val alerts = listOf(
        AlertEntity(
            id = "alert_elephant_hassan",
            title = "Elephant movement near estate",
            type = AlertType.Sighting.name,
            location = "Sakleshpur, Hassan",
            description = "A small herd reported near estate boundary. Avoid night travel.",
            reportedAt = System.currentTimeMillis() - 3600000,
            priority = AlertPriority.Medium.name
        ),
        AlertEntity(
            id = "alert_leopard_mysuru",
            title = "Leopard sighting",
            type = AlertType.Conflict.name,
            location = "Hunsur, Mysuru",
            description = "Forest staff informed. Secure livestock indoors.",
            reportedAt = System.currentTimeMillis() - 7200000,
            priority = AlertPriority.High.name
        )
    )
}
