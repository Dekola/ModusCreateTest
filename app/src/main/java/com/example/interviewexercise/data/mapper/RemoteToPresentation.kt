package com.example.interviewexercise.data.mapper

import com.example.interviewexercise.data.model.Movie
import com.example.interviewexercise.data.model.MoviePresentation

fun Movie.toPresentation(): MoviePresentation {
    return MoviePresentation(id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage)
}
