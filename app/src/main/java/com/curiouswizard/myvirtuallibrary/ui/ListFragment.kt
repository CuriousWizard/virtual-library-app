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
import com.curiouswizard.myvirtuallibrary.databinding.FragmentListBinding

class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    // RecyclerView Adapter for converting a list of Asteroids to scrollable list.
    private var adapter: BookGridAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        val application = requireActivity().application
        val viewModelFactory = ListViewModel.ListViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewModel = viewModel

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToAddFragment()
            )
        }

        // Handle if user clicked on an asteroid and navigate to DetailFragment
        viewModel.navigateToDetails.observe(viewLifecycleOwner, { book ->
            book?.let {
                this.findNavController().navigate(
                    ListFragmentDirections.toDetailFragment(book)
                )
                viewModel.doneNavigating()
            }
        })

        adapter = BookGridAdapter(BookListener { viewModel.displayBookDetails(it) })

        binding.booksGrid.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.books.observe(viewLifecycleOwner, { list ->
            list?.apply {
                adapter?.books = list
                invalidateNoData()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        invalidateNoData()
    }

    private fun invalidateNoData(){
        if (viewModel.books.value.isNullOrEmpty()){
            binding.noDataImage.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImage.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }
}