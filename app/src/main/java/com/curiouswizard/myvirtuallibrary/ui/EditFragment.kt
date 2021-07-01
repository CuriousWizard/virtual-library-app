package com.curiouswizard.myvirtuallibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.databinding.FragmentEditBinding
import com.google.android.material.snackbar.Snackbar

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var viewModel: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        // Create EditViewModel using its factory
        val application = requireActivity().application
        val viewModelFactory = EditViewModel.EditViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)

        // Get selected Book from safe-args navigation
        val book = EditFragmentArgs.fromBundle(requireArguments()).selectedBook
        viewModel.book.value = book

        // Handles Snackbar messages
        viewModel.snackbarMessageInt.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        })

        // Navigating back to previous DetailFragment
        viewModel.navigateBack.observe(viewLifecycleOwner,{
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingBack()
            }
        })

        // When user clicks to Save FAB update book in the database
        binding.fabSave.setOnClickListener {
            getEditTexts()
            viewModel.editSelectedBook()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // It sets up the EditTexts and the cover photo
        viewModel.book.observe(viewLifecycleOwner, {
            viewModel.setup()
            setupEditTexts()
            loadCoverPhoto(it.coverPhoto)
        })
    }

    private fun setupEditTexts() {
        binding.titleEditTextField.setText(viewModel.title.value)
        binding.authorEditTextField.setText(viewModel.authors.value)
        binding.yearEditTextField.setText(viewModel.year.value)
        binding.isbnEditTextField.setText(viewModel.isbn.value)
    }

    /**
     * Using this helper function, because using two-way binding did not updated given
     * MutableLiveData.
     */
    private fun getEditTexts(){
        viewModel.title.value = binding.titleEditTextField.text.toString()
        viewModel.authors.value = binding.authorEditTextField.text.toString()
        viewModel.year.value = binding.yearEditTextField.text.toString()
        viewModel.isbn.value = binding.isbnEditTextField.text.toString()
    }

    private fun loadCoverPhoto(cover: String?) {
        if (cover != null) {
            Glide.with(binding.bookCoverImage)
                .load(cover)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_image_animation)
                        .error(R.drawable.ic_broken_image_24)
                        .fitCenter()
                )
                .into(binding.bookCoverImage)
        } else {
            Glide.with(binding.bookCoverImage)
                .load(R.drawable.ic_unknown_book)
                .into(binding.bookCoverImage)
        }
    }
}