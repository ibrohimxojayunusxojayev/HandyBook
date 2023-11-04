package com.example.dorixona.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dorixona.R
import com.example.dorixona.adapter.BookGridAdapter
import com.example.dorixona.adapter.FilterAdapter
import com.example.dorixona.databinding.FragmentSearchBinding
import com.example.dorixona.model.Book
import com.example.dorixona.model.Category
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var listRoman: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val api = APIClient.getInstance().create(APIService::class.java)
        var curr_category = "Barchasi"
        listRoman = mutableListOf()

        binding.rv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        api.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    listRoman = response.body()!!.toMutableList()
                    Log.d("TAG", "${response.body()!!.joinToString { it.name }}")
                    binding.rv.adapter =
                        BookGridAdapter(response.body()!!.toMutableList(), object : BookGridAdapter.MyBook {
                            override fun onItemClick(book: Book) {
                                val bundle = bundleOf("book" to book.id)
                                findNavController().navigate(
                                    R.id.action_main_to_moreFragment,
                                    bundle
                                )
                            }
                        }, requireContext(), false)
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })



        binding.search.addTextChangedListener { it ->

            var books = listRoman
            Log.d("CURRR", "$curr_category")
            if (curr_category != "Barchasi") {
                api.getProductByCategory(curr_category).enqueue(object : Callback<List<Book>> {
                    override fun onResponse(
                        call: Call<List<Book>>,
                        response: Response<List<Book>>
                    ) {
                        if (response.isSuccessful) {
                            books = response.body()!!.toMutableList()
                            Log.d("books", "${response.body()!!.joinToString{it.name}}")
                            Log.d("books", "${books.joinToString{it.name}}")
                        }
                    }

                    override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                        Log.d("DNXX", "$t")
                    }

                })
            }


            val filter = mutableListOf<Book>()
            if (it != null) {
                var fav = books
                for (c in fav) {
                    if (c.name.lowercase().contains(it.toString().lowercase())) {
                        filter.add(c)
                    }
                }
                var adapter2 = BookGridAdapter(filter, object : BookGridAdapter.MyBook {
                    override fun onItemClick(book: Book) {
                        val bundle = bundleOf("book" to book.id)
                        findNavController().navigate(
                            R.id.action_main_to_moreFragment,
                            bundle
                        )
                    }

                }, requireActivity(), false)
                binding.rv.adapter = adapter2
            }
        }


        binding.filterr.setOnClickListener {
            if (binding.categories.isVisible) {
                binding.categories.visibility = View.GONE
                binding.filterr.setImageResource(R.drawable.filter_not)

                var adapter2 = BookGridAdapter(listRoman, object : BookGridAdapter.MyBook {
                    override fun onItemClick(book: Book) {
                        val bundle = bundleOf("book" to book.id)
                        findNavController().navigate(
                            R.id.action_main_to_moreFragment,
                            bundle
                        )
                    }

                }, requireActivity(), false)
                binding.rv.adapter = adapter2
            } else {
                binding.categories.visibility = View.VISIBLE
                binding.filterr.setImageResource(R.drawable.filter)

            }


        }




        api.getAllCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    var list = mutableListOf<Category>()
                    list.add(Category("Barchasi"))
                    for (i in response.body()!!) {
                        list.add(i)
                    }

                    binding.categories.adapter =
                        FilterAdapter(list, object : FilterAdapter.MyBook {
                            override fun onItemClick(category: Category) {

                                curr_category = category.type_name

                                if (category.type_name == "Barchasi") {
                                            Log.d("D", "${listRoman.joinToString { it.name }}")
                                    var adapter2 =
                                        BookGridAdapter(listRoman, object : BookGridAdapter.MyBook {
                                            override fun onItemClick(book: Book) {
                                                val bundle = bundleOf("book" to book.id)
                                                findNavController().navigate(
                                                    R.id.action_main_to_moreFragment,
                                                    bundle
                                                )
                                            }

                                        }, requireActivity(), false)
                                    binding.rv.adapter = adapter2
                                } else {

                                    api.getProductByCategory(category.type_name)
                                        .enqueue(object : Callback<List<Book>> {
                                            override fun onResponse(
                                                call: Call<List<Book>>,
                                                response: Response<List<Book>>
                                            ) {
                                                binding.rv.adapter = BookGridAdapter(
                                                    response.body()!!.toMutableList(),
                                                    object : BookGridAdapter.MyBook {
                                                        override fun onItemClick(book: Book) {
                                                            val bundle = bundleOf("book" to book.id)
                                                            findNavController().navigate(
                                                                R.id.action_main_to_moreFragment,
                                                                bundle
                                                            )
                                                        }

                                                    },
                                                    requireContext(), false
                                                )
                                            }

                                            override fun onFailure(
                                                call: Call<List<Book>>,
                                                t: Throwable
                                            ) {
                                                Log.d("TAG", "onFailure: $t")
                                            }

                                        })
                                }
                            }

                        }, requireContext())
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })
        return binding.root


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Search.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            Search().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}