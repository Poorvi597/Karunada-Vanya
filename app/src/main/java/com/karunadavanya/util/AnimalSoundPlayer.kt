package com.karunadavanya.util

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.karunadavanya.R
import com.karunadavanya.domain.Wildlife

class AnimalSoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(wildlifeId: String) {
        val sound = soundResourceFor(wildlifeId) ?: return
        play(sound)
    }

    fun play(animal: Wildlife) {
        try {
            MediaPlayer.create(context, animal.soundResId ?: R.raw.elephant)?.apply {
                start(this)
            }
        } catch (_: Exception) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun play(@RawRes sound: Int) {
        try {
            MediaPlayer.create(context, sound)?.apply {
                start(this)
            }
        } catch (_: Exception) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun start(player: MediaPlayer) {
        mediaPlayer?.release()
        mediaPlayer = player
        player.setOnCompletionListener {
            it.release()
            if (mediaPlayer === it) mediaPlayer = null
        }
        player.setOnErrorListener { erroredPlayer, _, _ ->
            erroredPlayer.release()
            if (mediaPlayer === erroredPlayer) mediaPlayer = null
            true
        }
        player.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    @RawRes
    private fun soundResourceFor(wildlifeId: String): Int? {
        return when (wildlifeId) {
            "bengal_tiger" -> R.raw.tiger
            "asian_elephant" -> R.raw.elephant
            "indian_leopard" -> R.raw.leopard
            "black_panther" -> R.raw.black_panther
            "great_hornbill" -> R.raw.hornbill
            "mugger_crocodile" -> R.raw.crocodile
            "common_indian_toad" -> R.raw.indian_toad
            "malabar_gliding_frog" -> R.raw.gliding_frog
            "indian_peafowl" -> R.raw.peacock
            "sandalwood", "rosewood", "teak" -> null
            else -> R.raw.forest_sound
        }
    }
}
