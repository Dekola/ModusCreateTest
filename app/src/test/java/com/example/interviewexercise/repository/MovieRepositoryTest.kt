package com.example.interviewexercise.repository

import com.example.interviewexercise.data.model.PopularMoviesResponse
import com.example.interviewexercise.networking.apis.MovieApi
import com.example.interviewexercise.networking.common.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoException
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    private lateinit var repository: MovieRepository

    @Mock
    lateinit var movieApi: MovieApi
    lateinit var autoCloseable: AutoCloseable

    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(unconfinedTestDispatcher)
        autoCloseable = MockitoAnnotations.openMocks(this)
        repository = MovieRepository()
    }

    @Test
    fun `test when getPopularMovies returns success`() = runTest {
        Mockito.`when`(movieApi.getPopularMoviesAsync(1)).thenReturn(Response.success(
            PopularMoviesResponse()))

        val result = repository.getPopularMovies(1)

        assert(result.status == Status.SUCCESS)
    }

    @Test
    fun `test when getPopularMovies returns failure`() = runTest {
        Mockito.`when`(movieApi.getPopularMoviesAsync(1))
            .thenReturn(Response.error(404, "".toResponseBody()))

        val result = repository.getPopularMovies(1)

        assert(result.status == Status.ERROR)
    }

    @Test
    fun `test when getPopularMovies throws exception`() = runTest {
        Mockito.`when`(movieApi.getPopularMoviesAsync(1))
            .thenThrow(MockitoException(""))

        val result = repository.getPopularMovies(1)

        assert(result.status == Status.ERROR)
    }

    @After
    fun tearDown() {
        autoCloseable.close()
        unconfinedTestDispatcher.cancel()
    }
}