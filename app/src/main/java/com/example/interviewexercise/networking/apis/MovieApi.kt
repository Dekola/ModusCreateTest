package com.example.interviewexercise.networking.apis

import com.example.interviewexercise.data.model.PopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMoviesAsync(
        @Query("page") page: Int,
    ): Response<PopularMoviesResponse>
}
