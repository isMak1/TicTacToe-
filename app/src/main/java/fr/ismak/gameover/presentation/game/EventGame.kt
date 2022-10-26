package fr.ismak.gameover.presentation.game

sealed class EventGame{
    data class BoardTapped(val cellNo: Int): EventGame()
    object NewGame: EventGame()
    object Exit: EventGame()
}
