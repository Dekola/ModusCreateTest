package com.example.interviewexercise.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.databinding.ActivityMovieBinding
import com.example.interviewexercise.networking.common.Resource
import com.example.interviewexercise.networking.common.Status
import com.example.interviewexercise.views.gallery.GalleryViewModel
import com.example.interviewexercise.views.gallery.MovieAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModels()
    private val moviePagingDataAdapter: MovieAdapter by lazy { MovieAdapter(::onMovieItemClicked) }
    lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setViews()
    }

    private fun setViews() {
        with(binding) {
            list.adapter = moviePagingDataAdapter
            list.layoutManager = GridLayoutManager(this@MovieActivity, 2)
            list.addItemDecoration(DividerItemDecoration(this@MovieActivity,
                android.widget.LinearLayout.VERTICAL))
        }
    }

    private fun setObservers() {
        with(viewModel) {
            getPopularMovies(1)
            toastLiveData.observe(this@MovieActivity) { toastWrapper ->
                showToast(getString(toastWrapper.message), toastWrapper.length)
            }
            moviesLiveData.observe(this@MovieActivity) { movieResource ->
                binding.progressBar.visibility = View.GONE
                processMovieResource(movieResource)
            }
            showError.observe(this@MovieActivity) { errorMessage ->
                showError(errorMessage)
            }
        }
    }

    private fun showError(errorMessage: String) {
        ErrorDialogFragment.newInstance(errorMessage).run {
            isCancelable = false
            show(childFragmentManager, null)
        }
    }

    private fun processMovieResource(movieResource: Resource<List<MoviePresentation>>) {
        when (movieResource.status) {
            Status.SUCCESS -> {
                movieResource.data?.let { movieList ->
                    moviePagingDataAdapter.setData(movieList)
                    binding.progressBar.visibility = View.GONE
                }
            }
            Status.ERROR -> {
                showError(movieResource.message ?: "An error occurred while fetching movies")
                binding.progressBar.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun onMovieItemClicked(moviePresentation: MoviePresentation) {
        Snackbar.make(binding.root, moviePresentation.title.orEmpty(), Snackbar.LENGTH_SHORT).show()
    }
}
