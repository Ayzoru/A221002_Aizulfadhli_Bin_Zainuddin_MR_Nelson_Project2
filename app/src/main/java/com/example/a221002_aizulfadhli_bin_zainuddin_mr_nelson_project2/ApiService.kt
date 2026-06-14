package com.example.a221002_aizulfadhli_bin_zainuddin_mr_nelson_project2


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


//steps for reference
//1.data model: This mirrors the JSON structure sent by the server
data class HealthTipResponse(
    val slip: Slip
)

data class Slip(
    val id: Int,
    val advice: String //this holds actual dynamic health tip text
)

//2.interface: Defines the API endpoint
interface HealthApiService {
    //using a free public advice api endpoint for daily tips
    @GET("advice")
    suspend fun getRandomHealthTip(): HealthTipResponse
}

//3.retrofit client: The engine that initiates the network request
object RetrofitClient {
    private const val BASE_URL = "https://api.adviceslip.com/"

    val apiService: HealthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //parses the JSON automatically
            .build()
            .create(HealthApiService::class.java)
    }
}