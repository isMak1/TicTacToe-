package fr.ismak.gameover.presentation.players

import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.util.PlayerOrder

sealed class EventPlayers {
    data class Order(val playerOrder: PlayerOrder): EventPlayers()
    data class DeletePlayer(val player: Player): EventPlayers()
    object RestorePlayer: EventPlayers()
    object ToggleOrderSection: EventPlayers()
}
