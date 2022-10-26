package fr.ismak.gameover.domain.use_case
// Contains the business logic (manipulating the database)
data class PlayerUseCases(
    val getPlayersUseCase: GetPlayersUseCase,
    val deletePlayerUseCase: DeletePlayerUseCase,
    val addPlayerUseCase: AddPlayerUseCase,
    val getPlayerUseCase: GetPlayerUseCase
)