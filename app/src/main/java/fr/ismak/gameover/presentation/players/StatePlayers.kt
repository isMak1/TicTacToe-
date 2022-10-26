package fr.ismak.gameover.presentation.players

import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.util.OrderType
import fr.ismak.gameover.domain.util.PlayerOrder

data class StatePlayers(
    val players : List<Player> = emptyList(),
    var playersInGame: List<Player> = emptyList(),
    val playerOrder: PlayerOrder = PlayerOrder.Name(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)
