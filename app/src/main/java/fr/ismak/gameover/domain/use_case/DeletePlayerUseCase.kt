package fr.ismak.gameover.domain.use_case

import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.repository.PlayerRepository

class DeletePlayerUseCase(
    private val repository: PlayerRepository
) {

    suspend operator fun invoke(player: Player) {
        repository.deletePlayer(player)
    }
}