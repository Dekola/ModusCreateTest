package com.example.interviewexercise.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.interviewexercise.data.mapper.toPresentation
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.data.source.MoviePagingSource
import com.example.interviewexercise.data.source.NETWORK_PAGE_SIZE
import com.example.interviewexercise.networking.RetrofitFactory
import com.example.interviewexercise.networking.common.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor() : IMovieRepository {

    private val movieApi = RetrofitFactory.getMovieApi()

    override suspend fun getPopularMoviesPagination(): Flow<PagingData<MoviePresentation>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApi)
            }
        ).flow
    }

    override suspend fun getPopularMovies(page: Int): Resource<List<MoviePresentation>> {
        return try {
            val response = movieApi.getPopularMoviesAsync(page)

            if (response.isSuccessful) {
                response.body()?.results?.let { movieList ->
                    Resource.success(movieList.map { it.toPresentation() })
                } ?: run {
                    Resource.error(msg = "An error occurred while fetching movies",
                        errorStatus = response.code(),
                        data = null)
                }
            } else {
                Resource.error(msg = "An error occurred while fetching movies",
                    errorStatus = response.code(),
                    data = null)
            }
        } catch (exception: Exception) {
            Resource.exception(exception = exception, data = null)
        }
    }

}
