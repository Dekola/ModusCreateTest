package com.example.interviewexercise.views

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.databinding.ActivityMovieBinding
import com.example.interviewexercise.views.gallery.GalleryViewModel
import com.example.interviewexercise.views.gallery.MoviePagingDataAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviePaginationActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModels()
    private val moviePagingDataAdapter: MoviePagingDataAdapter by lazy { MoviePagingDataAdapter(::onMovieItemClicked) }
    lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moviePagingDataAdapter.addLoadStateListener { loadState ->
            viewModel.manageLoadStates(loadState)
        }

        setObservers()
        setViews()
    }

    private fun setViews() {
        with(binding) {
            list.adapter = moviePagingDataAdapter
            list.layoutManager = GridLayoutManager(this@MoviePaginationActivity, 2)
            list.addItemDecoration(DividerItemDecoration(this@MoviePaginationActivity,
                LinearLayout.VERTICAL))
        }
    }

    private fun setObservers() {
        with(viewModel) {
            getPopularMoviesPagination()
            toastLiveData.observe(this@MoviePaginationActivity) { toastWrapper ->
                showToast(getString(toastWrapper.message), toastWrapper.length)
            }
            moviePaginationLiveData.observe(this@MoviePaginationActivity) { movieResponse ->
                binding.progressBar.visibility = View.GONE
                moviePagingDataAdapter.submitData(lifecycle, movieResponse)
            }
            loadLiveData.observe(this@MoviePaginationActivity) { load ->
                binding.progressBar.visibility = if (load) View.VISIBLE else View.GONE
            }
            showError.observe(this@MoviePaginationActivity) { errorMessage ->
                showToast(errorMessage)
            }
        }
    }

    private fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

    private fun onMovieItemClicked(moviePresentation: MoviePresentation) {
        Snackbar.make(binding.root, moviePresentation.title.orEmpty(), Snackbar.LENGTH_SHORT).show()
    }
}
