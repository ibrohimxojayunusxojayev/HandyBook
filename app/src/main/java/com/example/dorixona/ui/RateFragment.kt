package com.example.dorixona.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.dorixona.R
import com.example.dorixona.databinding.FragmentRateBinding
import com.example.dorixona.model.AddComment
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
 * Use the [RateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RateFragment : Fragment() {
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
        val binding = FragmentRateBinding.inflate(layoutInflater, container, false)

        val api = APIClient.getInstance().create(APIService::class.java)
        val id = arguments?.getSerializable("book") as Int
        var book: Book? = null
        var user = ShPHelper.getInstance(requireContext()).getUser()

        api.getBook(id).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                book = response.body()!!
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "onDNXFailure: $t")
            }


        })

        var main_rating = 1.0
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating >= 2.5) {
                binding.emoji.setImageResource(R.drawable.smiling_emoji)
            } else {
                binding.emoji.setImageResource(R.drawable.sad_emoji)
            }
            main_rating = rating.toDouble()
        }

        binding.send.setOnClickListener {
            if (main_rating == 0.0) {
                Toast.makeText(requireContext(), "Reytingni belgilang", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            api.addComment(
                AddComment(
                    id,
                    main_rating.toInt(),
                    binding.commentsss.text.toString(),
                    user.id
                )
            ).enqueue(object : Callback<AddComment> {
                override fun onResponse(call: Call<AddComment>, response: Response<AddComment>) {
                    if (response.isSuccessful) {
                        val dialog = Dialog(requireContext())
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setCancelable(true)
                        dialog.setContentView(R.layout.activity_dialog)
                        var image = dialog.findViewById<ImageView>(R.id.done_tick)
                        image.load("https://cdn.dribbble.com/users/431025/screenshots/5515042/ezgif-1-40072e48cc62.gif")
                        dialog.show()
                        Handler().postDelayed(Runnable { dialog.cancel() } , 1000)
                        findNavController().navigate(R.id.action_rateFragment_to_main)
                    }
                }

                override fun onFailure(call: Call<AddComment>, t: Throwable) {
                    Log.d("TAG", "onDNXFailure: $t")
                }

            })

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
         * @return A new instance of fragment RateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}