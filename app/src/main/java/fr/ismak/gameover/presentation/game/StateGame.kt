package fr.ismak.gameover.presentation.game

import androidx.compose.ui.graphics.Color
import fr.ismak.gameover.domain.model.Player

data class GameState(
    val playerX: Player = Player(0,"","",0,0,false,),
    val playerXName: String = "",
    val playerXScore: Int = 0,

    val playerO: Player = Player(0,"","",0,0,false,),
    val playerOName: String = "",
    val playerOScore: Int = 0,

    val drawCount: Int = 0,

    val hintText: String = "",
    val currentTurn: BoardCellValue = BoardCellValue.CIRCLE,
    val victoryType: VictoryType = VictoryType.NONE,
    val backGroundColor: Color = Color.LightGray,
    val hasWon: Boolean = false
)

enum class BoardCellValue {
    CIRCLE,
    CROSS,
    NONE
}

enum class VictoryType {
    HORIZONTAL1,
    HORIZONTAL2,
    HORIZONTAL3,
    VERTICAL1,
    VERTICAL2,
    VERTICAL3,
    DIAGONAL1,
    DIAGONAL2,
    NONE
}