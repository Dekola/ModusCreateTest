package com.example.interviewexercise.networking

import com.example.interviewexercise.networking.apis.MovieApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


object RetrofitFactory {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "bf718d4dd8b23985d9c3edbcfd440a27"
    private const val NETWORK_LAYER_TAG = "OkHttpNetworkLayer"
    private const val APPLICATION_LAYER_TAG = "OkHttpApplicationLayer"


    private fun getHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val networkLogging = HttpLoggingInterceptor { message ->
            Timber.tag(NETWORK_LAYER_TAG).d(message)
        }
        val appLogging = HttpLoggingInterceptor { message ->
            Timber.tag(APPLICATION_LAYER_TAG).d(message)
        }

        networkLogging.level = HttpLoggingInterceptor.Level.BODY
        appLogging.level = HttpLoggingInterceptor.Level.BODY

        val headerAuthorizationInterceptor = Interceptor { chain ->
            var request = chain.request()
            val originalHttpUrl: HttpUrl = request.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val requestBuilder = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .url(url)

            request = requestBuilder.build()
            chain.proceed(request)
        }
        client.interceptors().add(headerAuthorizationInterceptor)

//        client.interceptors().add(appLogging)
//        client.addNetworkInterceptor(networkLogging)
        client.addInterceptor(networkLogging)

        return client.build()
    }

    private fun <T> builder(
        endpoint: Class<T>
    ): T {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
            .client(getHttpClient())
            .build()
            .create(endpoint)
    }

    private fun getGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    fun getMovieApi(): MovieApi {
        return builder(MovieApi::class.java)
    }

}