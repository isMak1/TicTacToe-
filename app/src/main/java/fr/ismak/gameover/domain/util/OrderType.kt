package fr.ismak.gameover.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
