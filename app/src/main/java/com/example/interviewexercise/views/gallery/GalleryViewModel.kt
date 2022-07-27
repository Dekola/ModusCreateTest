package com.example.interviewexercise.views.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.interviewexercise.R
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.data.wrapper.ToastWrapper
import com.example.interviewexercise.networking.NetworkConnectivity
import com.example.interviewexercise.networking.common.Resource
import com.example.interviewexercise.repository.IMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val movieRepository: IMovieRepository,
    private val networkConnectivity: NetworkConnectivity,
) : ViewModel() {

    private val _showError = MutableLiveData<String>()
    var showError: LiveData<String> = _showError

    private val _toastLiveData = MutableLiveData<ToastWrapper>()
    var toastLiveData: LiveData<ToastWrapper> = _toastLiveData

    private val _moviePaginationLiveData = MutableLiveData<PagingData<MoviePresentation>>()
    val moviePaginationLiveData: LiveData<PagingData<MoviePresentation>> = _moviePaginationLiveData

    private val _loadLiveData = MutableLiveData<Boolean>()
    var loadLiveData: LiveData<Boolean> = _loadLiveData

    fun getPopularMoviesPagination() {
        viewModelScope.launch {
            if (!networkConnectivity.isConnected()) {
                _toastLiveData.postValue(ToastWrapper(R.string.no_internet_connection))
            } else {
                movieRepository.getPopularMoviesPagination().cachedIn(viewModelScope).collect {
                    _moviePaginationLiveData.postValue(it)
                }
            }
        }
    }

    private val _moviesLiveData: MutableLiveData<Resource<List<MoviePresentation>>> =
        MutableLiveData()
    val moviesLiveData: LiveData<Resource<List<MoviePresentation>>> = _moviesLiveData

    fun getPopularMovies(page: Int) {
        viewModelScope.launch {
            if (!networkConnectivity.isConnected()) {
                _toastLiveData.postValue(ToastWrapper(R.string.no_internet_connection))
            } else {
                _moviesLiveData.postValue(Resource.loading())
                val resource = movieRepository.getPopularMovies(page)
                _moviesLiveData.postValue(resource)
            }
        }
    }

    fun manageLoadStates(loadState: CombinedLoadStates) {
        _loadLiveData.postValue(loadState.refresh is LoadState.Loading)
        when {
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            else -> null
        }?.error?.message?.let { errorMessage ->
            _showError.postValue(errorMessage)
        }
    }
}
