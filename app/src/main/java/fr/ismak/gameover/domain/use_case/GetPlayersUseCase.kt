package fr.ismak.gameover.domain.use_case

import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.repository.PlayerRepository
import fr.ismak.gameover.domain.util.OrderType
import fr.ismak.gameover.domain.util.PlayerOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPlayersUseCase(
    private  val repository: PlayerRepository
) {
    // Use cases should have 1 single function or purpose that is an operator function
    // which makes this class called as a function itself
    operator fun invoke(
        playerOrder: PlayerOrder = PlayerOrder.Name(OrderType.Ascending)
    ): Flow<List<Player>> {
        return repository.getPlayers().map {
             when(playerOrder.orderType) {
                 is OrderType.Ascending -> {
                     when(playerOrder) {
                         is PlayerOrder.Name -> it.sortedBy { it.name.lowercase() }
                         is PlayerOrder.Score -> it.sortedBy { it.score }
                         is PlayerOrder.Symbol -> it.sortedBy { it.symbol.lowercase() }
                     }
                 }
                 is OrderType.Descending -> {
                     when(playerOrder) {
                         is PlayerOrder.Name -> it.sortedByDescending { it.name.lowercase() }
                         is PlayerOrder.Score -> it.sortedByDescending { it.score }
                         is PlayerOrder.Symbol -> it.sortedByDescending { it.symbol.lowercase() }
                     }
                 }
             }
        }
    }
}