package com.curiouswizard.myvirtuallibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.databinding.BookListItemBinding
import com.curiouswizard.myvirtuallibrary.model.Book

/**
 * A ListAdapter to show asteroids with RecycleView
 */
class BookGridAdapter(private val clickListener: BookListener) :
    ListAdapter<Book, BookGridAdapter.BookViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }
    }

    var books: List<Book> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val withDataBinding: BookListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            BookViewHolder.LAYOUT,
            parent,
            false)
        return BookViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.binding.also {
            it.book = books[position]
            it.clickListener = clickListener
        }
    }

    override fun getItemCount() = books.size

    class BookViewHolder(val binding: BookListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.book_list_item
        }
    }
}

/**
 * A listener for Books. When user taps on the selected book, it will handle it
 */
class BookListener(val clickListener: (selected: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}