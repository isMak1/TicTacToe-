package fr.ismak.gameover.domain.use_case

import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.repository.PlayerRepository

class GetPlayerUseCase(
    private val repository: PlayerRepository
) {

    suspend operator fun invoke(id: Int): Player? {
        return repository.getPlayerById(id)
    }
}