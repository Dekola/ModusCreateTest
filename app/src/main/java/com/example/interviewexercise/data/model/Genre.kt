package com.example.interviewexercise.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Long? = null,
    val name: String? = null
) : Parcelable