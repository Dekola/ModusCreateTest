package com.example.interviewexercise.repository

import androidx.paging.PagingData
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.networking.common.Resource
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    suspend fun getPopularMoviesPagination(): Flow<PagingData<MoviePresentation>>
    suspend fun getPopularMovies(page: Int): Resource<List<MoviePresentation>>
}