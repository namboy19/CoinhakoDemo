package com.example.coinhakodemo.repository

import com.example.coinhakodemo.model.BaseDataState
import com.example.coinhakodemo.model.home.CoinResponse
import kotlinx.coroutines.flow.Flow
import java.util.*

open class HomeRepository(
    private val homeRemoteDataSource: HomeRemoteDataSource
) {
    fun getCoins(currency: String = "USD"): Flow<BaseDataState> =
        homeRemoteDataSource.getCoins(currency)
}