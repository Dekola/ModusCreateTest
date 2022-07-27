package com.example.interviewexercise.views.gallery

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewexercise.data.model.MoviePresentation
import com.example.interviewexercise.databinding.ListMoviesBinding
import com.squareup.picasso.Picasso

class MovieViewHolder(
    private val binding: ListMoviesBinding,
    private val onItemClicked: (MoviePresentation) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MoviePresentation) {
        binding.run {
            root.setOnClickListener {
                onItemClicked(movie)
            }
            titleTv.text = SpannableStringBuilder().append("Title: ")
                .scale(1.1F) { bold { append("${movie.title}") } }

            overviewTv.text = SpannableStringBuilder().append("Overview: ")
                .scale(1.1F) { bold { append("${movie.overview}") } }

            val fullUrl = IMAGE_URL_PREFIX + movie.posterPath
            Picasso.get().load(fullUrl).into(posterImg)

            starImg.visibility = if ((movie.voteAverage ?: 0.0) > 8) View.VISIBLE else View.GONE

        }
    }

}

const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/w300/"
