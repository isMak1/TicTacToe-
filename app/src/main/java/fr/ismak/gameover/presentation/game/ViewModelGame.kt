package fr.ismak.gameover.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.use_case.PlayerUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGame @Inject constructor(
    private val playerUseCases: PlayerUseCases
) : ViewModel() {

    var state by mutableStateOf(GameState())

    init {
        getSelectedPlayers()
    }

    private var getSelectedPLayersJob: Job? = null
    private var unselectPLayersJob: Job? = null

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    fun onEvent(event: EventGame) {
        when (event) {
            is EventGame.BoardTapped -> {
                addValueToBoard(event.cellNo)
            }
            EventGame.NewGame -> {
                gameReset()
            }
            EventGame.Exit -> {
                unselectPlayers()
                getSelectedPLayersJob?.cancel()
            }
        }
    }

    private fun hasBoardFull(): Boolean {
        if (boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }

    private fun getSelectedPlayers() {
        getSelectedPLayersJob = playerUseCases.getPlayersUseCase()
            .onEach { players ->
                state = state.copy(
                    playerX = players.first { it.symbol == "X" && it.isSelected},
                    playerXName = players.first { it.symbol == "X" && it.isSelected}.name,
                    playerXScore = players.first { it.symbol == "X" && it.isSelected}.score,
                    playerO = players.first { it.symbol == "O" && it.isSelected},
                    playerOName = players.first { it.symbol == "O" && it.isSelected}.name,
                    playerOScore = players.first { it.symbol == "O" && it.isSelected}.score,
                )
            }.launchIn(viewModelScope)
    }

    private fun unselectPlayers() {
        unselectPLayersJob = playerUseCases.getPlayersUseCase()
            .onEach { players ->
                for (player in players) {
                    playerUseCases.addPlayerUseCase(
                        Player(
                            name = player.name,
                            symbol = player.symbol,
                            score = player.score,
                            isSelected = false,
                            color = player.color,
                            id = player.id
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun updatePlayerScore(player: Player) {
        viewModelScope.launch {
            playerUseCases.addPlayerUseCase(
                Player(
                    name = player.name,
                    symbol = player.symbol,
                    score = player.score + 1,
                    isSelected = player.isSelected,
                    color = player.color,
                    id = player.id
                )
            )
        }
    }

    private fun gameReset() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        state = state.copy(
            hintText = "${state.playerOName}'s turn",
            currentTurn = BoardCellValue.CIRCLE,
            victoryType = VictoryType.NONE,
            backGroundColor = Color.LightGray,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            if (checkForVictory(BoardCellValue.CIRCLE)) {
                updatePlayerScore(state.playerO)
                state = state.copy(
                    hintText = "${state.playerOName} Won",
                    currentTurn = BoardCellValue.NONE,
                    backGroundColor = Color(state.playerO.color),
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1,
                    backGroundColor = Color.LightGray
                )
            } else {
                state = state.copy(
                    hintText = "${state.playerXName}'s turn",
                    currentTurn = BoardCellValue.CROSS,
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            if (checkForVictory(BoardCellValue.CROSS)) {
                updatePlayerScore(state.playerX)
                state = state.copy(
                    hintText = "${state.playerXName} Won",
                    backGroundColor = Color(state.playerX.color),
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1,
                    backGroundColor = Color.LightGray,
                )
            } else {
                state = state.copy(
                    hintText = "${state.playerOName}'s turn",
                    currentTurn = BoardCellValue.CIRCLE,
                )
            }
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }

}