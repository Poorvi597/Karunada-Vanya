package com.karunadavanya.data.repository

import com.karunadavanya.R
import com.karunadavanya.data.SampleData
import com.karunadavanya.data.local.WildlifeEntity
import com.karunadavanya.data.local.WildlifeDao
import com.karunadavanya.domain.Wildlife
import com.karunadavanya.domain.WildlifeCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WildlifeRepository(
    private val wildlifeDao: WildlifeDao
) {
    suspend fun seedIfNeeded() {
        wildlifeDao.insertAll(SampleData.wildlife.map { it.withLocalAssets() })
    }

    fun observeWildlife(
        query: String,
        category: WildlifeCategory?
    ): Flow<List<Wildlife>> {
        val source = when {
            query.isNotBlank() -> wildlifeDao.search(query.trim())
            category != null -> wildlifeDao.observeByCategory(category.name)
            else -> wildlifeDao.observeAll()
        }
        return source.map { rows -> rows.map { it.asDomain() } }
    }

    fun observeWildlife(id: String): Flow<Wildlife?> {
        return wildlifeDao.observeById(id).map { it?.asDomain() }
    }
}

private fun WildlifeEntity.withLocalAssets(): WildlifeEntity {
    return when (id) {
        "asian_elephant" -> copy(imageResId = R.drawable.elephant, soundResId = R.raw.elephant)
        "bengal_tiger" -> copy(imageResId = R.drawable.tiger, soundResId = R.raw.tiger)
        "indian_leopard" -> copy(imageResId = R.drawable.leopard, soundResId = R.raw.leopard)
        "black_panther" -> copy(imageResId = R.drawable.black_panther, soundResId = R.raw.black_panther)
        "sloth_bear" -> copy(imageResId = R.drawable.sloth_bear, soundResId = R.raw.sloth_bear)
        "wild_boar" -> copy(imageResId = R.drawable.wild_boar, soundResId = R.raw.wild_boar)
        "gaur" -> copy(imageResId = R.drawable.gaur, soundResId = R.raw.gaur)
        "nilgiri_tahr" -> copy(imageResId = R.drawable.nilgiri_tahr, soundResId = R.raw.nilgiri_tahr)
        "king_cobra" -> copy(imageResId = R.drawable.king_cobra, soundResId = R.raw.king_cobra)
        "mugger_crocodile" -> copy(imageResId = R.drawable.crocodile, soundResId = R.raw.crocodile)
        "giant_squirrel" -> copy(imageResId = R.drawable.giant_squirrel, soundResId = R.raw.giant_squirrel)
        "common_indian_toad" -> copy(imageResId = R.drawable.indian_toad, soundResId = R.raw.indian_toad)
        "malabar_gliding_frog" -> copy(imageResId = R.drawable.gliding_frog, soundResId = R.raw.gliding_frog)
        "great_hornbill" -> copy(imageResId = R.drawable.hornbill, soundResId = R.raw.hornbill)
        "indian_peafowl" -> copy(imageResId = R.drawable.peacock, soundResId = R.raw.peacock)
        "sandalwood" -> copy(imageResId = R.drawable.sandalwood, soundResId = null)
        "rosewood" -> copy(imageResId = R.drawable.rosewood, soundResId = null)
        "teak" -> copy(imageResId = R.drawable.teak, soundResId = null)
        else -> when (name.lowercase()) {
            "elephant" -> copy(imageResId = R.drawable.elephant, soundResId = R.raw.elephant)
            "tiger" -> copy(imageResId = R.drawable.tiger, soundResId = R.raw.tiger)
            "leopard" -> copy(imageResId = R.drawable.leopard, soundResId = R.raw.leopard)
            "black panther" -> copy(imageResId = R.drawable.black_panther, soundResId = R.raw.black_panther)
            "sloth bear" -> copy(imageResId = R.drawable.sloth_bear, soundResId = R.raw.sloth_bear)
            "wild boar" -> copy(imageResId = R.drawable.wild_boar, soundResId = R.raw.wild_boar)
            "gaur" -> copy(imageResId = R.drawable.gaur, soundResId = R.raw.gaur)
            "nilgiri tahr" -> copy(imageResId = R.drawable.nilgiri_tahr, soundResId = R.raw.nilgiri_tahr)
            "king cobra" -> copy(imageResId = R.drawable.king_cobra, soundResId = R.raw.king_cobra)
            "mugger crocodile" -> copy(imageResId = R.drawable.crocodile, soundResId = R.raw.crocodile)
            "giant squirrel" -> copy(imageResId = R.drawable.giant_squirrel, soundResId = R.raw.giant_squirrel)
            "common indian toad" -> copy(imageResId = R.drawable.indian_toad, soundResId = R.raw.indian_toad)
            "malabar gliding frog" -> copy(imageResId = R.drawable.gliding_frog, soundResId = R.raw.gliding_frog)
            "great indian hornbill" -> copy(imageResId = R.drawable.hornbill, soundResId = R.raw.hornbill)
            "hornbill" -> copy(imageResId = R.drawable.hornbill, soundResId = R.raw.hornbill)
            "indian peafowl" -> copy(imageResId = R.drawable.peacock, soundResId = R.raw.peacock)
            "peacock" -> copy(imageResId = R.drawable.peacock, soundResId = R.raw.peacock)
            "sandalwood" -> copy(imageResId = R.drawable.sandalwood, soundResId = null)
            "rosewood" -> copy(imageResId = R.drawable.rosewood, soundResId = null)
            "teak" -> copy(imageResId = R.drawable.teak, soundResId = null)
            else -> copy(soundResId = R.raw.forest_sound)
        }
    }
}
