package com.example.dorixona.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorixona.R
import com.example.dorixona.adapter.CommentAdapter
import com.example.dorixona.databinding.FragmentCommentBinding
import com.example.dorixona.model.Comment
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val binding = FragmentCommentBinding.inflate(inflater, container, false)

        var api = APIClient.getInstance().create(APIService::class.java)
        val id = arguments?.getSerializable("book") as Int

        binding.comments.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        api.getComments(id).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    binding.comments.adapter =
                        CommentAdapter(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })


        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> // the delay of the extension of the FAB is set for 12 items    if (scrollY > oldScrollY + 12 && binding.floatingActionButton.isShown) {
            binding.yozish.hide()
            // the delay of the extension of the FAB is set for 12 items
            if (scrollY < oldScrollY - 12 && !binding.yozish.isShown) {
                binding.yozish.show()
            }
            // if the nestedScrollView is at the first item of the list then the    // floating action should be in show state
            if (scrollY == 0) {
                binding.yozish.show()
            }
        })

        binding.yozish.setOnClickListener {
            val bundle = bundleOf()
            bundle.putInt("book", id)
            findNavController().navigate(R.id.action_commentFragment_to_rateFragment, bundle)
        }
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
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
         * @return A new instance of fragment CommentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}