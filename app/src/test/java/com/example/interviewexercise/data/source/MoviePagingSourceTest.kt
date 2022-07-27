package com.example.interviewexercise.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.example.interviewexercise.data.mapper.toPresentation
import com.example.interviewexercise.data.model.Movie
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.data.model.PopularMoviesResponse
import com.example.interviewexercise.networking.apis.MovieApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class MoviePagingSourceTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var movieApi: MovieApi

    private lateinit var moviePagingSource: MoviePagingSource

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxed = true)
        moviePagingSource = MoviePagingSource(movieApi)
    }

    @Test
    fun `when api service response throws exception`() {
        runTest {
            val httpException = HttpException(
                Response.error<PopularMoviesResponse>(
                    500, "Test Server Error"
                        .toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )

            coEvery { (movieApi.getPopularMoviesAsync(any())) } throws httpException

            val expectedResult =
                PagingSource.LoadResult.Error<Int, MoviePresentation>(httpException)

            assertEquals(expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ))
        }
    }

    @Test
    fun `when api service response runs successfully`() {
        runTest {
            val response =
                PopularMoviesResponse(arrayListOf(Movie(), Movie()))

            coEvery {
                (movieApi.getPopularMoviesAsync(any()))
            } returns Response.success(response)

            val expectedResult = PagingSource.LoadResult.Page(
                data = response.results!!.map { it.toPresentation() },
                prevKey = null,
                nextKey = 2
            )

            assertEquals(expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
            )
        }
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
