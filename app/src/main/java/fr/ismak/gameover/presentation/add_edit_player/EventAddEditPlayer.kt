package fr.ismak.gameover.presentation.add_edit_player

import androidx.compose.ui.focus.FocusState

sealed class EventAddEditPlayer {
    data class EnteredName(val value: String): EventAddEditPlayer()
    data class ChangeNameFocus(val focusState: FocusState): EventAddEditPlayer()
    data class EnteredSymbol(val value: String): EventAddEditPlayer()
    data class ChangeSymbolFocus(val focusState: FocusState): EventAddEditPlayer()
    data class ChangeColor(val color: Int): EventAddEditPlayer()

    object SavePlayerAddEditPlayer: EventAddEditPlayer()
}