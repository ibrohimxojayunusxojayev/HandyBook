package com.example.dorixona.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.dorixona.R
import com.example.dorixona.ui.Search
import com.example.dorixona.adapter.BookAdapter
import com.example.dorixona.adapter.CategoryAdapter
import com.example.dorixona.databinding.FragmentHomeBinding
import com.example.dorixona.model.Book
import com.example.dorixona.model.Category
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val api = APIClient.getInstance().create(APIService::class.java)
        val bundle = bundleOf()


        api.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                Log.d("Tag", response.body()!![0].name.toString())
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("Tag", "Problem")
            }

        })



        binding.search.addTextChangedListener {
            Search.newInstance(it.toString())
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, Search()).commit()
        }
        api.getMainBook().enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    binding.bookImage.load(response.body()!!.image)
                    binding.bookName.text = response.body()!!.name + " asari"
                    bundle.putInt("book", response.body()!!.id)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_moreFragment, bundle)
        }

        binding.mainRv2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.mainRv2.setHasFixedSize(true)
        api.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    binding.mainRv2.adapter =
                        BookAdapter(response.body()!!, object : BookAdapter.MyBook {
                            override fun onItemClick(book: Book) {
                                val bundle = bundleOf("book" to book.id)
                                findNavController().navigate(
                                    R.id.action_main_to_moreFragment,
                                    bundle
                                )
                            }
                        })


                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("Tag", "Ss")
            }

        })
        var list = mutableListOf<Category>()



        binding.mainRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mainRv.setHasFixedSize(true)
        var context = requireContext()
        api.getAllCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                Log.d("Tag", "S")
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    Log.d("Categories", list.joinToString())
                    for (i in response.body()!!) {
                        api.getProductByCategory(i.type_name)
                            .enqueue(object : Callback<List<Book>> {
                                override fun onResponse(
                                    call: Call<List<Book>>,
                                    response: Response<List<Book>>
                                ) {
                                    if (response.body()!!.size != 0) {
                                        Log.d("add", i.type_name)
                                        list.add(i)
                                        Log.d("added", list.joinToString())

                                        binding.mainRv.adapter = CategoryAdapter(
                                            list,
                                            api,
                                            object : CategoryAdapter.MyBook {
                                                override fun onItemClick(book: Book) {
                                                    val bundle = bundleOf("book" to book.id)
                                                    findNavController().navigate(
                                                        R.id.action_main_to_moreFragment,
                                                        bundle
                                                    )
                                                }

                                            },
                                            context
                                        )

                                    }
                                }

                                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                                    Log.d("Tag2", "$t")
                                }

                            })
                    }



                    Log.d("Categories11", list.joinToString())

                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("Tag1", "$t")
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
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}