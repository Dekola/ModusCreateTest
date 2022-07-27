package com.example.interviewexercise.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelProviderFactory(val viewModel: ViewModel) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // return modelClass.getConstructor(ViewModel::class.java).newInstance(viewM)
        return viewModel as T
    }

}