package com.example.dorixona.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorixona.R
import com.example.dorixona.databinding.FragmentRegistrationBinding
import com.example.dorixona.model.User
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import com.example.dorixona.util.ShPHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.itschool.handybook.model.SignUp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Registration.newInstance] factory method to
 * create an instance of this fragment.
 */
class Registration : Fragment() {
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
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val shPHelper = ShPHelper.getInstance(requireContext())
        val api = APIClient.getInstance().create(APIService::class.java)
        var signUp: SignUp
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.button.setOnClickListener {
            if (!binding.ism.text.isNullOrEmpty() &&
                !binding.familiya.text.isNullOrEmpty() &&
                !binding.email.text.isNullOrEmpty() &&
                !binding.parol.text.isNullOrEmpty() &&
                !binding.parolRe.text.isNullOrEmpty()
            ) {
                if (binding.parol.text.toString() == (binding.parolRe.text.toString()) && binding.parol.text.toString().length >= 8) {
                    if (binding.email.text.isValidEmail()) {
                        signUp = SignUp(
                            binding.email.text.toString(),
                            "${binding.ism.text.toString()} ${binding.familiya.text.toString()}",
                            binding.email.text.toString(),
                            binding.parol.text.toString()
                        )

                        api.signup(signUp).enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    Log.d("TAG", "onResponse: ${response.body()}")
                                    shPHelper.setUser(response.body()!!)
                                }
                                findNavController().navigate(R.id.action_registration2_to_main)
                                Log.d("TAG", "onResponse: ${response.body()}")
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.d("TAG", "onFailure: $t")
                            }

                        })
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Emailni to'g'ri kiriting!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Parolni qayta kirgazing! Parol 8 ta belgidan iborat bo'lishi kerak", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Iltimos barcha bo'shliqlarni to'ldiring!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Registration.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Registration().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}