package com.example.dorixona.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dorixona.databinding.BookItemBinding
import com.example.dorixona.databinding.FilterItemBinding
import com.example.dorixona.model.Category
import com.example.dorixona.model.Filter

class FilterAdapter(
    var categoryList: List<Category>,
    var myBook: MyBook,
    var context: Context
) : RecyclerView.Adapter<FilterAdapter.MyHolder>() {

    class MyHolder(binding: FilterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var back = binding.back
        var container = binding.container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            FilterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var category = categoryList[position]
        holder.name.text = category.type_name

        holder.itemView.setOnClickListener {
            myBook.onItemClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface MyBook {
        fun onItemClick(category: Category)
    }
}