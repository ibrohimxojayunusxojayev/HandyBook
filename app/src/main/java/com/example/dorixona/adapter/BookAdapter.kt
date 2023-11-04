package com.example.dorixona.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dorixona.R
import com.example.dorixona.databinding.BookItemBinding
import com.example.dorixona.model.Book

class BookAdapter(
    var bookList: List<Book>,
    var myBook: MyBook
) : RecyclerView.Adapter<BookAdapter.MyHolder>() {

    class MyHolder(binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var img = binding.image
        var author = binding.author
        var rating = binding.rating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var book = bookList[position]
        holder.name.text = book.name
        if (book.image != ""){
            holder.img.load(book.image){
                placeholder(R.drawable.ic_launcher_background)
//                kotlin.error(androidx.appcompat.R.drawable.abc_btn_radio_material)
            }
        }
        holder.author.text = book.author
        holder.itemView.setOnClickListener {
            myBook.onItemClick(book)
        }
        holder.rating.text = book.reyting.toString()

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface MyBook {
        fun onItemClick(book: Book)
    }
}