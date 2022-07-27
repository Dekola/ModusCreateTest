package com.example.interviewexercise.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.interviewexercise.databinding.FragmentErrorDialogBinding

private const val ARG_ERROR_MESSAGE = "errorMessage"

class ErrorDialogFragment : DialogFragment() {

    private var errorMessage: String? = null
    private lateinit var binding: FragmentErrorDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            errorMessage = it.getString(ARG_ERROR_MESSAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentErrorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        binding.run {
            errorMessageTv.text = errorMessage
            closeImg.setOnClickListener { dismiss() }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(errorMessage: String) =
            ErrorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ERROR_MESSAGE, errorMessage)
                }
            }
    }
}