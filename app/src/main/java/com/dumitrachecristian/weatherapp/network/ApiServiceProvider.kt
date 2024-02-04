package com.dumitrachecristian.weatherapp.network

import com.dumitrachecristian.weatherapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiServiceProvider {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun getClient(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val basicInterceptor = Interceptor { chain ->
            val original = chain.request()

            val url: HttpUrl = original.url.newBuilder().addQueryParameter("appid", BuildConfig.API_KEY).build()

            val newRequest = original
                .newBuilder()
                .url(url)
                .build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(basicInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}