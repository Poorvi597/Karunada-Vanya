package com.karunadavanya.presentation.screens

import androidx.annotation.DrawableRes
import com.karunadavanya.R

@DrawableRes
internal fun animalDrawableRes(animalText: String): Int {
    val text = animalText.lowercase()
        .replace("_", " ")
        .replace("-", " ")

    return when {
        "black panther" in text -> R.drawable.black_panther
        "wild boar" in text || "boar" in text -> R.drawable.wild_boar
        "sloth bear" in text -> R.drawable.sloth_bear
        "nilgiri tahr" in text -> R.drawable.nilgiri_tahr
        "king cobra" in text -> R.drawable.king_cobra
        "giant squirrel" in text -> R.drawable.giant_squirrel
        "mugger crocodile" in text || "crocodile" in text -> R.drawable.crocodile
        "common indian toad" in text || "indian toad" in text || "toad" in text -> R.drawable.indian_toad
        "malabar gliding frog" in text || "gliding frog" in text || "frog" in text -> R.drawable.gliding_frog
        "great indian hornbill" in text || "hornbill" in text -> R.drawable.hornbill
        "indian peafowl" in text || "peacock" in text || "peafowl" in text -> R.drawable.peacock
        "elephant" in text -> R.drawable.elephant
        "tiger" in text -> R.drawable.tiger
        "leopard" in text -> R.drawable.leopard
        "gaur" in text || "bison" in text -> R.drawable.gaur
        else -> R.drawable.elephant
    }
}

@DrawableRes
internal fun alertAnimalDrawableRes(alert: com.karunadavanya.domain.CommunityAlert): Int {
    return animalDrawableRes("${alert.animalType} ${alert.title} ${alert.description}")
}
