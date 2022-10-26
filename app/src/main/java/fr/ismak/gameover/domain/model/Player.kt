package fr.ismak.gameover.domain.model

import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.ismak.gameover.ui.theme.*

@Entity
data class Player(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val symbol: String,
    val score: Int,
    val color: Int,
    var isSelected: Boolean = false
) {
    companion object {
        val playerColors = listOf(RedOrange, LightGreen, Violet, RedPink, BabyBlue, Green, Magenta)
    }
}

class InvalidPlayerException(message: String): Exception(message)