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

        val application = requireActivity().application
        val viewModelFactory = EditViewModel.EditViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)

        // Get selected Book from safe-args navigation
        val book = EditFragmentArgs.fromBundle(requireArguments()).selectedBook
        viewModel.book.value = book

        viewModel.snackbarMessageInt.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        })

        viewModel.navigateBack.observe(viewLifecycleOwner,{
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingBack()
            }
        })

        binding.fabSave.setOnClickListener {
            viewModel.editSelectedBook()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.book.observe(viewLifecycleOwner, {
            viewModel.setup()
            setupEditTexts()
            viewModel.photo.value = it.coverPhoto
        })
    }

    private fun setupEditTexts() {
        binding.titleEditTextField.setText(viewModel.title.value)
        binding.authorEditTextField.setText(viewModel.authors.value)
        binding.yearEditTextField.setText(viewModel.year.value)
        binding.isbnEditTextField.setText(viewModel.isbn.value)
    }
}