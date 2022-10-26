package fr.ismak.gameover.presentation.add_edit_player

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ismak.gameover.domain.model.InvalidPlayerException
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.use_case.PlayerUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAddEditPlayer @Inject constructor(
    private val playersUseCases: PlayerUseCases,
    // inject arguments with hilt
    savedStateHandle: SavedStateHandle
): ViewModel() {

    //we separate the states because each text field recompose the whole UI
    private val _playerName = mutableStateOf(StatePlayerTextField(
        hint = "Enter You name..."
    ))
    val playerName: State<StatePlayerTextField> = _playerName

    private val _playerSymbol = mutableStateOf(StatePlayerTextField(
        hint = "Choose The X or O team by typing the associated character"
    ))
    val playerSymbol: State<StatePlayerTextField> = _playerSymbol

    private val _playerColor = mutableStateOf(Player.playerColors.random().toArgb())
    val playerColor: State<Int> = _playerColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("playerId")?.let { playerId ->
            if(playerId != -1) {
                viewModelScope.launch {
                    playersUseCases.getPlayerUseCase(playerId)?.also { player ->
                        currentPlayerId = player.id
                        _playerName.value = playerName.value.copy(
                            text = player.name,
                            isHintVisible = false
                        )
                        _playerSymbol.value = playerSymbol.value.copy(
                            text = player.symbol,
                            isHintVisible = false
                        )
                        _playerColor.value = player.color
                    }
                }
            }
        }
    }

    private var currentPlayerId: Int? = null

    fun onEvent(event: EventAddEditPlayer) {
        when(event) {
            is EventAddEditPlayer.EnteredName -> {
                _playerName.value = playerName.value.copy(
                    text = event.value
                )
            }
            is EventAddEditPlayer.ChangeNameFocus -> {
                _playerName.value = playerName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            playerName.value.text.isBlank()
                )
            }
            is EventAddEditPlayer.EnteredSymbol -> {
                _playerSymbol.value = playerSymbol.value.copy(
                    text = event.value
                )
            }
            is EventAddEditPlayer.ChangeSymbolFocus -> {
                _playerSymbol.value = playerSymbol.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            playerSymbol.value.text.isBlank()
                )
            }
            is EventAddEditPlayer.ChangeColor -> {
                _playerColor.value = event.color
            }
            EventAddEditPlayer.SavePlayerAddEditPlayer -> {
                viewModelScope.launch {
                    try {
                        playersUseCases.addPlayerUseCase(
                            Player(
                                name = playerName.value.text,
                                symbol = playerSymbol.value.text.uppercase(),
                                score = 0,
                                isSelected = false,
                                color = playerColor.value,
                                id = currentPlayerId
                            )
                        )
                        _eventFlow.emit(UiEvent.SavePlayer)
                    } catch (e: InvalidPlayerException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save player"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SavePlayer: UiEvent()
    }
}