package com.example.interviewexercise.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularMoviesResponse(
    @SerializedName("results")
    val results: ArrayList<Movie>? = null
) : Parcelable