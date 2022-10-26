package fr.ismak.gameover.presentation.util

sealed class Screens(val route: String, ) {
    object PlayersScreens: Screens ("players_screen", )
    object AddEditPlayerScreens: Screens ("add_edit_player_screen")
    object GameScreens: Screens("game_screen")
}
