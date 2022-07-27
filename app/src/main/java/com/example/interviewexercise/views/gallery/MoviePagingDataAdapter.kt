package com.example.interviewexercise.views.gallery


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.databinding.ListMoviesBinding

class MoviePagingDataAdapter(private val onItemClicked: (MoviePresentation) -> Unit) :
    PagingDataAdapter<MoviePresentation, MovieViewHolder>(MovieListDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListMoviesBinding.inflate(LayoutInflater.from(parent.context))
        return MovieViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    object MovieListDiff : DiffUtil.ItemCallback<MoviePresentation>() {
        override fun areItemsTheSame(
            oldItem: MoviePresentation,
            newItem: MoviePresentation,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MoviePresentation,
            newItem: MoviePresentation,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
