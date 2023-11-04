package com.example.dorixona.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.dorixona.R
import com.example.dorixona.databinding.FragmentMoreBinding
import com.example.dorixona.model.Book

import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import com.example.dorixona.util.ShPHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment : Fragment() {
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
        val binding = FragmentMoreBinding.inflate(inflater, container, false)
        val id = arguments?.getSerializable("book") as Int
        var item : Book? = null

        val api = APIClient.getInstance().create(APIService::class.java)
        var books = ShPHelper.getInstance(requireContext()).getBooks()

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> // the delay of the extension of the FAB is set for 12 items    if (scrollY > oldScrollY + 12 && binding.floatingActionButton.isShown) {
            binding.floatingActionButton.hide()
            binding.oqish.hide()
            // the delay of the extension of the FAB is set for 12 items
            if (scrollY < oldScrollY - 12 && !binding.floatingActionButton.isShown) {
                binding.floatingActionButton.show()
                binding.oqish.show()
            }
            // if the nestedScrollView is at the first item of the list then the    // floating action should be in show state
            if (scrollY == 0) {
                binding.floatingActionButton.show()
                binding.oqish.show()
            }
        })

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        api.getBook(id).enqueue(object : Callback<Book>{
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful){
                    item = response.body()!!
                    binding.image.load(response.body()!!.image)
                    binding.name.text = response.body()!!.name
                    binding.author.text = response.body()!!.author
                    binding.description.text = response.body()!!.description
                    if (books.contains(item)) {
                        binding.saved.setImageResource(R.drawable.saved_filled)
                    } else {
                        binding.saved.setImageResource(R.drawable.saved)
                    }
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "DNX" + " $t")
            }

        })
        binding.oqish.setOnClickListener {
            api.getBook(id).enqueue(object : Callback<Book>{
                override fun onResponse(call: Call<Book>, response: Response<Book>) {
                    if (response.isSuccessful){
                        ShPHelper.getInstance(requireContext()).setReadBook(response.body()!!)

                    }
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    Log.d("TAG", "DNX" + " $t")
                }

            })

            Log.d("DDNX", "${ShPHelper.getInstance(requireContext()).getBooks().joinToString { it.name }}")
            //findNavController().navigate(R.id.action_moreFragment_to_pdfViewerFragment, bundleOf("book" to id))
        }

        binding.saved.setOnClickListener {
            if (!books.contains(item)) {
                binding.saved.setImageResource(R.drawable.saved_filled)
                ShPHelper.getInstance(requireContext()).setBook(item!!)
                books = ShPHelper.getInstance(requireContext()).getBooks()
            } else {
                binding.saved.setImageResource(R.drawable.saved)
                ShPHelper.getInstance(requireContext()).removeBook(item!!)
                books = ShPHelper.getInstance(requireContext()).getBooks()
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val bundle = bundleOf("book" to item!!.id)
            findNavController().navigate(R.id.action_moreFragment_to_commentFragment, bundle)
        }

        binding.AudioBook.setOnClickListener {
            binding.AudioBook.setBackgroundResource(R.drawable.back_choose_dark_blue)
            binding.EBook.setBackgroundColor(Color.TRANSPARENT)
            val bundle = bundleOf("book" to item!!.id)
            findNavController().navigate(R.id.action_moreFragment_to_playerFragment, bundle)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}