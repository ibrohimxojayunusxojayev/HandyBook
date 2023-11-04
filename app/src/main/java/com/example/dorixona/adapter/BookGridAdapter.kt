package com.example.dorixona.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dorixona.R
import com.example.dorixona.databinding.BookItemBinding
import com.example.dorixona.databinding.BookItemGridBinding
import com.example.dorixona.model.Book
import com.example.dorixona.util.ShPHelper


class BookGridAdapter(
    var bookList: MutableList<Book>,
    var myBook: MyBook,
    var context: Context,
    var state: Boolean
) : RecyclerView.Adapter<BookGridAdapter.MyHolder>() {

    class MyHolder(binding: BookItemGridBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var img = binding.image
        var author = binding.author
        var saved_btn = binding.savedBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            BookItemGridBinding.inflate(
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
        if (!state) holder.saved_btn.visibility = View.GONE
        holder.saved_btn.setOnClickListener{
            bookList.remove(book)
            ShPHelper.getInstance(context).removeBook(book)
            notifyDataSetChanged()
        }
        holder.author.text = book.author
        holder.itemView.setOnClickListener {
            myBook.onItemClick(book)
        }


    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface MyBook {
        fun onItemClick(book: Book)
    }
}