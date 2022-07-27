package com.example.interviewexercise.data.wrapper

import android.widget.Toast
import androidx.annotation.StringRes

data class ToastWrapper(@StringRes var message: Int, var length: Int = Toast.LENGTH_SHORT)