package com.curiouswizard.myvirtuallibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        // Setup ViewModel
        val application = requireActivity().application
        val viewModelFactory = DetailViewModel.DetailViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        // Get selected book id from safe-args navigation
        viewModel.bookId.value = DetailFragmentArgs.fromBundle(requireArguments()).id

        viewModel.bookId.observe(viewLifecycleOwner, {
            // Search book in database
            viewModel.getBookById(it)
        })

        viewModel.book.observe(viewLifecycleOwner, {
            // Passing selected book to DataBinding
            binding.book = it
        })

        binding.fabEdit.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.toEditFragment(viewModel.book.value!!))
        }

        return binding.root
    }
}