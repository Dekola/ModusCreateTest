package com.example.interviewexercise.data.model

import com.google.gson.annotations.SerializedName

data class GenericErrorResponse(
    @SerializedName("status_message")
    val message: String? = null,
    @SerializedName("status_code")
    val statusCode: Int = 0
)