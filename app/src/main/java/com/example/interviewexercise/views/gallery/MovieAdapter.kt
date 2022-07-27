package com.example.interviewexercise.views.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.databinding.ListMoviesBinding

class MovieAdapter(private val onMovieSelected: (MoviePresentation) -> Unit) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private val items = mutableListOf<MoviePresentation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListMoviesBinding.inflate(LayoutInflater.from(parent.context))
        return MovieViewHolder(binding, onMovieSelected)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(movieList: List<MoviePresentation>) {
        this.items.addAll(movieList)
        notifyDataSetChanged()
    }

}
