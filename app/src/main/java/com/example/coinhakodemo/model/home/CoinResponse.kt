package com.example.coinhakodemo.model.home

data class CoinResponse(
    val base: String,
    val buy_price: String,
    val counter: String,
    val icon: String,
    val name: String,
    val sell_price: String
) {
    fun getBuyPrice(): String {
        return "$buy_price $counter"
    }

    fun getSellPrice(): String {
        return "$sell_price $counter"
    }
}