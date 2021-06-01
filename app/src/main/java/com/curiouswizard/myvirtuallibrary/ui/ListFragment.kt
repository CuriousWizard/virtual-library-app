package com.curiouswizard.myvirtuallibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.curiouswizard.myvirtuallibrary.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToAddFragment()
            )
        }

        return binding.root
    }
}