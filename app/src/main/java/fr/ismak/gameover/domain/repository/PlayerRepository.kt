package fr.ismak.gameover.domain.repository

import fr.ismak.gameover.domain.model.Player
import kotlinx.coroutines.flow.Flow

//we use an interface to be able to make fake versions of it for testing
interface PlayerRepository {

    fun getPlayers(): Flow<List<Player>>

    suspend fun getPlayerById(id: Int): Player?

    suspend fun insertPlayer(player: Player)

    suspend fun deletePlayer(player: Player)
}