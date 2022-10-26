package fr.ismak.gameover.domain.util

sealed class PlayerOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): PlayerOrder(orderType)
    class Score(orderType: OrderType): PlayerOrder(orderType)
    class Symbol(orderType: OrderType): PlayerOrder(orderType)

    fun copy(orderType: OrderType): PlayerOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Score -> Score(orderType)
            is Symbol -> Symbol(orderType)
        }
    }
}
