package com.example.interviewexercise.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.interviewexercise.data.mapper.toPresentation
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.networking.apis.MovieApi
import org.json.JSONObject

const val MOVIE_STARTING_PAGE_INDEX = 1

//Default size is 20: https://www.themoviedb.org/talk/587bea71c3a36846c300ff73
const val NETWORK_PAGE_SIZE = 20

class MoviePagingSource(
    private val movieApi: MovieApi,
) : PagingSource<Int, MoviePresentation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviePresentation> {
        val position = params.key ?: MOVIE_STARTING_PAGE_INDEX
        return try {
            val response =
                movieApi.getPopularMoviesAsync(position)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val items = responseBody.results
                val nextKey = if (items?.isEmpty() == true) {
                    null
                } else {
                    position + (params.loadSize / NETWORK_PAGE_SIZE)
                }
                items?.let {
                    LoadResult.Page(
                        data = items.map { it.toPresentation() },
                        prevKey = if (position == MOVIE_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = nextKey
                    )
                } ?: run {
                    LoadResult.Error(Exception(""))
                }
            } else {
                val responseString = response.errorBody()?.string()
                val errorString = JSONObject(responseString ?: "").getString("message")
                LoadResult.Error(Exception(errorString))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviePresentation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}