package com.curiouswizard.myvirtuallibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.curiouswizard.myvirtuallibrary.Constants.SCANNED_ISBN_EXTRA
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.camera.CameraActivity
import com.curiouswizard.myvirtuallibrary.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar

class AddFragment : Fragment() {
    private lateinit var viewModel: AddViewModel
    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)

        val application = requireActivity().application
        val viewModelFactory = AddViewModel.AddViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddViewModel::class.java)

        binding.viewModel = viewModel

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        val startScanningIsbn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.scannedIsbn.value = it.data?.getStringExtra(SCANNED_ISBN_EXTRA).toString()
        }

        binding.scanButton.setOnClickListener {
            val scanIntent = Intent(requireContext(), CameraActivity::class.java)
            startScanningIsbn.launch(scanIntent)
        }

        viewModel.scannedIsbn.observe(viewLifecycleOwner, { isbn ->
            viewModel.getBookInfo(isbn)
        })

        viewModel.scannedBook.observe(viewLifecycleOwner, { book ->
            viewModel.setBookInfo(book)
        })

        viewModel.snackbarMessage.observe(viewLifecycleOwner, { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        })

        viewModel.years.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()){
                val adapter = ArrayAdapter(requireContext(), R.layout.year_list_item, it)
                (binding.yearListView as? AutoCompleteTextView)?.setAdapter(adapter)
                binding.yearListView.setText(it[0].substring(0,2))
            }
        })

        viewModel.navigateBack.observe(viewLifecycleOwner,{
            it?.let {
                findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })

        binding.isbnTextField.setEndIconOnClickListener {
            // Respond to help icon press
            displayIsbnExplanationDialog()
        }

        binding.fabSave.setOnClickListener {
            viewModel.saveBook(requireContext())
        }

        return binding.root
    }

    private fun displayIsbnExplanationDialog(){
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.isbn_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}