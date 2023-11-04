package com.example.dorixona.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorixona.R
import com.example.dorixona.databinding.FragmentLoginBinding
import com.example.dorixona.model.User
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import com.example.dorixona.util.ShPHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.model.SignIn

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogIn : Fragment() {
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
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        var api = APIClient.getInstance().create(APIService::class.java)

        binding.textView5.setOnClickListener {
            findNavController().navigate(R.id.action_logIn_to_registration2)
        }
        binding.button.setOnClickListener {
            var userName = binding.email.text.toString()
            var password = binding.parol.text.toString()
            var signIn = SignIn(userName, password)
            api.login(signIn).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful && !response.body()!!.fullname.isNullOrEmpty()){
                        ShPHelper.getInstance(requireContext()).setUser(response.body()!!)
                        findNavController().navigate(R.id.action_logIn_to_main)
                    }
                    else{
                        Toast.makeText(
                            requireContext(),
                            "Login yoki parol noto'g'ri kiritilgan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("TAG", "onFailure: $t")
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
         * @return A new instance of fragment register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogIn().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}