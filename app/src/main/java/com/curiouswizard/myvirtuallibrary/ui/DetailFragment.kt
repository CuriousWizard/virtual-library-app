package com.curiouswizard.myvirtuallibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        // Get selected Book from safe-args navigation
        val book = DetailFragmentArgs.fromBundle(requireArguments()).selectedBook

        // Passing selected asteroid to DataBinding
        binding.book = book

        binding.fabEdit.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.toEditFragment(book))
        }

        return binding.root
    }
}