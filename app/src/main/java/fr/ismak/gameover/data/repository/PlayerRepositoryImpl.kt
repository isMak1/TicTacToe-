package fr.ismak.gameover.data.repository

import fr.ismak.gameover.data.data_source.PlayerDao
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(
    private val dao: PlayerDao
) : PlayerRepository {
    override fun getPlayers(): Flow<List<Player>> {
        return dao.getPlayers()
    }

    override suspend fun getPlayerById(id: Int): Player? {
        return dao.getPlayerById(id)
    }

    override suspend fun insertPlayer(player: Player) {
        return dao.insertPlayer(player)
    }

    override suspend fun deletePlayer(player: Player) {
        return dao.deletePlayer(player)
    }
}