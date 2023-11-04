package com.example.dorixona.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dorixona.ui.Main
import com.example.dorixona.R
import com.example.dorixona.databinding.BookItemBinding
import com.example.dorixona.databinding.MainItemBinding
import com.example.dorixona.model.Book
import com.example.dorixona.model.Category
import com.example.dorixona.networking.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryAdapter(
    var categoryList: MutableList<Category>,
    var api: APIService,
    var myBook: MyBook,
    var context: Context,

) :
    RecyclerView.Adapter<CategoryAdapter.MyHolder>() {

    class MyHolder(binding: MainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.category
        var rv = binding.rv


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var category = categoryList[position]

        holder.name.text = category.type_name

        holder.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.rv.setHasFixedSize(true)
        api.getProductByCategory(category.type_name).enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful && !response.body()!!.isNullOrEmpty()) {
                    holder.rv.adapter = BookAdapter(response.body()!!, object : BookAdapter.MyBook {
                        override fun onItemClick(book: Book) {
                            myBook.onItemClick(book)
                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("TAG", "Hello")
            }
        })
    }

    interface MyBook{
        fun onItemClick(book: Book)
    }
}