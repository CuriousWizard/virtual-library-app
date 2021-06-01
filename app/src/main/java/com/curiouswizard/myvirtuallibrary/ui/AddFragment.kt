package com.curiouswizard.myvirtuallibrary.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.curiouswizard.myvirtuallibrary.Constants.SCANNED_ISBN_EXTRA
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.camera.CameraActivity
import com.curiouswizard.myvirtuallibrary.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar

class AddFragment : Fragment() {
    private val viewModel: AddViewModel by viewModels()
    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val startScanningIsbn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.scannedIsbn.value = it.data?.getStringExtra(SCANNED_ISBN_EXTRA).toString()
            Log.d("AddFragment", viewModel.scannedIsbn.value!!)
        }

        binding.scanButton.setOnClickListener {
            val scanIntent = Intent(requireContext(), CameraActivity::class.java)
            startScanningIsbn.launch(scanIntent)
        }

        viewModel.scannedIsbn.observe(viewLifecycleOwner, { isbn ->
            viewModel.getBookInfo(isbn)
            // TODO: Create function for getting edition years and put them into the EditText
        })

        viewModel.snackbarMessage.observe(viewLifecycleOwner, { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        })

        binding.isbnTextField.setEndIconOnClickListener {
            // Respond to help icon press
            // TODO: Create a AlertDialog for explaining what is ISBN
        }

        return binding.root
    }



}