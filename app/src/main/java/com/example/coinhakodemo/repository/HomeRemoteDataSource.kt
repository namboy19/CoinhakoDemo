package com.example.coinhakodemo.repository
import com.example.coinhakodemo.model.BaseApiResponse
import com.example.coinhakodemo.model.BaseDataState
import com.example.coinhakodemo.model.home.CoinResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.GET
import retrofit2.http.Query

open class HomeRemoteDataSource(
    private val homeApi: HomeApi,
    private val refreshIntervalMs: Long = 30000
) {
    open fun getCoins(currency: String) = flow {
        while (true) {
            emit(BaseDataState.Loading)

            delay(1000)

            android.util.Log.d("Api","fetching data...")
            val result = homeApi.fetchCoinsPrice(currency)

            if (result.data!=null){
                emit(BaseDataState.Success(result.data))
            }
            else{
                emit(BaseDataState.Error("unknow exception"))
            }

            delay(refreshIntervalMs)
        }
    }.flowOn(Dispatchers.IO).catch {
        emit(BaseDataState.Error(it.message ?: "unknow exception"))
    }

}

interface HomeApi {

    @GET("v3/price/all_prices_for_mobile")
    suspend fun fetchCoinsPrice(@Query("counter_currency") currency: String): BaseApiResponse<List<CoinResponse>>
}
