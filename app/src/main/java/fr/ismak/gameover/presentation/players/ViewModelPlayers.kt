package fr.ismak.gameover.presentation.players


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.use_case.PlayerUseCases
import fr.ismak.gameover.domain.util.OrderType
import fr.ismak.gameover.domain.util.PlayerOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// players View model is right on top of the UI
// In the View Model we make use of our Business Logic (our Use Cases)
// We basically take the result of our Use Cases and put them in a state for the UI to observe and display

@HiltViewModel
class ViewModelPlayers @Inject constructor(
    private val playerUseCases: PlayerUseCases,
) : ViewModel() {


    private val _state = mutableStateOf(StatePlayers())
    val state: State<StatePlayers> = _state

    private var recentlyDeletedPlayer: Player? = null

    private var getPLayersJob: Job? = null

    init {
        getPlayers(PlayerOrder.Name(OrderType.Ascending))
    }

    fun onEvent(event: EventPlayers) {
        when(event) {
            is EventPlayers.Order -> {
                if(state.value.playerOrder::class == event.playerOrder::class &&
                        state.value.playerOrder.orderType == event.playerOrder.orderType
                ) {
                    return
                }
                for (playerInGame in state.value.playersInGame) {
                    addSelectedToRoom(playerInGame)
                }
                getPlayers(event.playerOrder)
            }
            is EventPlayers.DeletePlayer -> {
                viewModelScope.launch {
                    playerUseCases.deletePlayerUseCase(event.player)
                    recentlyDeletedPlayer = event.player
                }
            }
            EventPlayers.RestorePlayer -> {
                viewModelScope.launch {
                    playerUseCases.addPlayerUseCase(recentlyDeletedPlayer ?: return@launch)
                    recentlyDeletedPlayer = null
                }
            }
            EventPlayers.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getPlayers(playerOrder: PlayerOrder) {
        getPLayersJob?.cancel()
        getPLayersJob = playerUseCases.getPlayersUseCase(playerOrder)
        .onEach { players ->
            _state.value = state.value.copy(
                players = players,
                playerOrder = playerOrder,
                playersInGame = players.filter { it.isSelected }
            )
        }.launchIn(viewModelScope)
    }

    fun addSelectedToRoom(player: Player) {
        viewModelScope.launch {
            playerUseCases.addPlayerUseCase(
                Player(
                    name = player.name,
                    symbol = player.symbol,
                    score = player.score,
                    isSelected = player.isSelected,
                    color = player.color,
                    id = player.id
                )
            )
        }
    }

}



