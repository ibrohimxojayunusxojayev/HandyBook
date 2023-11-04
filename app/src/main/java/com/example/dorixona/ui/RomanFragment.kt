package com.example.dorixona.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dorixona.R
import com.example.dorixona.adapter.BookGridAdapter
import com.example.dorixona.databinding.FragmentRomanBinding
import com.example.dorixona.model.Book
import com.example.dorixona.model.Filter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RomanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RomanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var listRoman: MutableList<Book>

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
        var binding = FragmentRomanBinding.inflate(inflater, container, false)
        listRoman = mutableListOf()
        val item = arguments?.getSerializable("name") as Filter
        binding.textView11.visibility = View.GONE
        binding.textView.text = item.name
        var adapter = BookGridAdapter(listRoman, object : BookGridAdapter.MyBook {
            override fun onItemClick(book: Book) {
                val bundle = bundleOf("book" to book)
                findNavController().navigate(R.id.action_romanFragment_to_moreFragment, bundle)
            }
        }, requireContext(),false)
        binding.rv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.avatar.setOnClickListener {
            findNavController().navigate(R.id.action_romanFragment_to_personalFragment)
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
         * @return A new instance of fragment RomanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RomanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}