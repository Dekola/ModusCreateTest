package com.example.interviewexercise.views.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.interviewexercise.R
import com.example.interviewexercise.networking.Network
import com.example.interviewexercise.repository.MovieRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var galleryViewModel: GalleryViewModel

    @MockK
    private lateinit var network: Network

    @MockK
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxed = true)
        every { network.isConnected() } returns true
        galleryViewModel = GalleryViewModel(repository, network)
    }

    @Test
    fun test_no_internet_getPopularMoviesPagination() = runTest {
        every { network.isConnected() } returns false
        galleryViewModel.getPopularMoviesPagination()
        assertEquals(R.string.no_internet_connection,
            galleryViewModel.toastLiveData.value?.message)
    }

    @Test
    fun test_no_internet_getPopularMovies() {
        every { network.isConnected() } returns false
        galleryViewModel.getPopularMovies(1)
        assertEquals(R.string.no_internet_connection,
            galleryViewModel.toastLiveData.value?.message)
    }

    @Test
    fun test_true_loading_state() {
        val loadState = CombinedLoadStates(LoadState.Loading,
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        galleryViewModel.manageLoadStates(loadState)

        assertEquals(true, galleryViewModel.loadLiveData.value)
    }

    @Test
    fun test_false_loading_state() {
        val loadState = CombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        galleryViewModel.manageLoadStates(loadState)

        assertEquals(false, galleryViewModel.loadLiveData.value)
    }


    @Test
    fun test_error_prepend_loading_state() {
        val errorMessage = "prepend error message"
//        val loadState2 = CombinedLoadStates(null, null, null, null, null)

        val loadState = CombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.Error(Throwable(errorMessage)),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        galleryViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, galleryViewModel.showError.value)
    }


    @Test
    fun test_error_append_loading_state() {
        val errorMessage = "append error message"

        val loadState = CombinedLoadStates(
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.Error(Throwable(errorMessage)),
            mockk(),
            null)

        galleryViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, galleryViewModel.showError.value)
    }

    @Test
    fun test_error_refresh_loading_state() {
        val errorMessage = "refresh error message"

        val loadState = CombinedLoadStates(LoadState.Error(Throwable(errorMessage)),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        galleryViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, galleryViewModel.showError.value)
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
