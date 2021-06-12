package com.example.coinhakodemo.module.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinhakodemo.model.BaseDataState
import com.example.coinhakodemo.model.home.CoinResponse
import com.example.coinhakodemo.repository.HomeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@InternalCoroutinesApi
class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    //viewmodel state
    private val coinsFlow: MutableStateFlow<BaseDataState> = MutableStateFlow(BaseDataState.Idle)
    private val searchQueryFlow: MutableStateFlow<String> = MutableStateFlow("")
    private var searchJob: Job? = null
    private var fetchDataJob: Job? = null


    //ui state
    private val _coinsState: MutableStateFlow<BaseDataState> = MutableStateFlow(BaseDataState.Idle)
    val coinsState = _coinsState

    init {
        viewModelScope.launch {
            combine(coinsFlow, searchQueryFlow) { coinsState, search ->
                return@combine when (coinsState) {
                    is BaseDataState.Success<*> -> {
                        var result = emptyList<CoinResponse>()
                        (coinsState.data as? List<CoinResponse>)?.let {
                            result = it.filter {
                                it.name.contains(search, true) || it.base.contains(
                                    search, true
                                )
                            }
                        }
                        BaseDataState.Success(result)
                    }
                    else -> coinsState
                }
            }.collect {
                _coinsState.value = it
            }
        }
    }

    fun loadData() {
        fetchDataJob = viewModelScope.launch {
            repository.getCoins()
                .collect {
                    coinsFlow.value = it
                }
        }
    }

    fun stopFetchData() {
        fetchDataJob?.cancel()
    }

    fun onInputSearchChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            search(query)
        }
    }

    private fun search(query: String) {
        searchQueryFlow.value = query
    }

}