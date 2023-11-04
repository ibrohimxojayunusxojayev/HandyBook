package com.example.dorixona.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorixona.R
import com.example.dorixona.adapter.BookAdapter
import com.example.dorixona.adapter.BookGridAdapter
import com.example.dorixona.databinding.FragmentPersonalBinding
import com.example.dorixona.model.Book
import com.example.dorixona.model.User
import com.example.dorixona.util.ShPHelper
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img: ImageView
    lateinit var user: User

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var binding = FragmentPersonalBinding.inflate(inflater, container, false)
        user = ShPHelper.getInstance(requireContext()).getUser()
        img = binding.imageView7

        binding.textView12.text = user.fullname
        binding.textView13.text = user.username
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imageView7.setOnClickListener {
            binding.camera.visibility = View.VISIBLE
            binding.file.visibility = View.VISIBLE
        }
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.saved.text = ShPHelper.getInstance(requireContext()).getBooks().count().toString()

        var books = ShPHelper.getInstance(requireContext()).getBooks()

        var d = ShPHelper.getInstance(requireContext()).getReadBooks().toList().distinctBy { it.name }

        binding.read.text = "1"
        binding.readed.text = d.count().toString()


        binding.rv1.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        Log.d("TAG", "onCreateView: ")

        binding.rv1.adapter = BookAdapter(ShPHelper.getInstance(requireContext()).getReadBooks().toList().takeLast(1), object : BookAdapter.MyBook{

            override fun onItemClick(book: Book) {
                val bundle = bundleOf("book" to book.id)
                findNavController().navigate(
                    R.id.action_personalFragment_to_moreFragment,
                    bundle
                )
            }
        })

        binding.rv2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        Log.d("XND", "${d.joinToString { it.name }}")
        binding.rv2.adapter =
            BookAdapter(d,
                object : BookAdapter.MyBook {
                    override fun onItemClick(book: Book) {
                        val bundle = bundleOf("book" to book.id)
                        findNavController().navigate(
                            R.id.action_personalFragment_to_moreFragment,
                            bundle
                        )
                    }
                })


        binding.rv3.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rv3.adapter = BookAdapter(books, object : BookAdapter.MyBook {
            override fun onItemClick(book: Book) {
                val bundle = bundleOf("book" to book.id)
                findNavController().navigate(
                    R.id.action_personalFragment_to_moreFragment,
                    bundle
                )
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
         * @return A new instance of fragment PersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}