package fr.ismak.gameover.domain.use_case

import fr.ismak.gameover.domain.model.InvalidPlayerException
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.repository.PlayerRepository

class AddPlayerUseCase(
    private val repository: PlayerRepository
) {

    @Throws(InvalidPlayerException::class)
    suspend operator fun invoke(player: Player) {
        if(player.name.isBlank()) {
            throw InvalidPlayerException("You need to Enter a Name.")
        }
        if(player.symbol.isBlank()) {
            throw InvalidPlayerException("You need to pick a Team")
        }
        repository.insertPlayer(player)
    }
}