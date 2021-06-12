package com.example.coinhakodemo.module.home

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.coinhakodemo.model.BaseDataState
import com.example.coinhakodemo.model.home.CoinResponse
import com.example.coinhakodemo.repository.HomeRemoteDataSource
import com.example.coinhakodemo.repository.HomeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@InternalCoroutinesApi

class HomeViewModelTest {


    private lateinit var repo: HomeRepository
    private var dataSource: HomeRemoteDataSource = Mockito.mock(HomeRemoteDataSource::class.java)
    private lateinit var viewModel: HomeViewModel

    private val coins = listOf(
        CoinResponse("LTC", "", "", "", "LiteCoin", ""),
        CoinResponse("NEO", "", "", "", "Neo", ""),
        CoinResponse("BCH", "", "", "", "Bitcoin Cash", ""),
        CoinResponse("ADA", "", "", "", "Cardano", ""),
        CoinResponse("BNB", "", "", "", "Binance Coin", ""),
    )

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        `when`(dataSource.getCoins("USD")).thenReturn(flow {
            emit(BaseDataState.Success(coins))
        })

        repo = HomeRepository(dataSource)
        viewModel = HomeViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun search_withResult() = runBlocking {

        viewModel.loadData()

        viewModel.onInputSearchChanged("bi")
        delay(1000)

        var searchResult =
            viewModel.coinsState.value as? BaseDataState.Success<List<CoinResponse>>

        assertNotNull(searchResult)

        assertArrayEquals(
            arrayOf(
                CoinResponse("BCH", "", "", "", "Bitcoin Cash", ""),
                CoinResponse("BNB", "", "", "", "Binance Coin", "")
            ),
            searchResult?.data?.toTypedArray()
        )
    }

    @Test
    fun search_notMatch() = runBlocking {

        viewModel.loadData()

        viewModel.onInputSearchChanged("bifi")
        delay(1000)

        var searchResult =
            viewModel.coinsState.value as? BaseDataState.Success<List<CoinResponse>>

        assertTrue(searchResult?.data?.isNullOrEmpty()==true)
    }

    @Test
    fun search_clearSearch() = runBlocking {

        viewModel.loadData()

        viewModel.onInputSearchChanged("")
        delay(1000)

        var searchResult =
            viewModel.coinsState.value as? BaseDataState.Success<List<CoinResponse>>

        assertArrayEquals(
            coins.toTypedArray(),
            searchResult?.data?.toTypedArray()
        )
    }
}