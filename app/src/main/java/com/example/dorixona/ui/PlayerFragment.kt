package com.example.dorixona.ui

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.dorixona.R
import com.example.dorixona.databinding.FragmentPlayerBinding
import com.example.dorixona.model.Book
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import com.example.dorixona.util.ShPHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {
    private var isPlaying = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        val id = arguments?.getSerializable("book") as Int
        var item: Book? = null

        val api = APIClient.getInstance().create(APIService::class.java)
        var books = ShPHelper.getInstance(requireContext()).getBooks()
        mediaPlayer = MediaPlayer()
        api.getBook(id).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    item = response.body()!!
                    binding.image.load(response.body()!!.image)
                    binding.name.text = response.body()!!.name
                    binding.author.text = response.body()!!.author
                    if (response.body()!!.audio != null) {
                        mediaPlayer =
                            MediaPlayer.create(requireContext(), response.body()!!.audio.toUri())
                    } else {
                        mediaPlayer =
                            MediaPlayer.create(requireContext(), R.raw.daylight)
                    }
                    binding.seekBar.max = mediaPlayer.duration / 1000
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
        binding.back.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.stop()
            requireActivity().onBackPressed()
        }

        val mHandler = Handler()
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val mCurrentPosition: Int = mediaPlayer.currentPosition / 1000
                binding.seekBar.progress = mCurrentPosition
                mHandler.postDelayed(this, 1000)
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.play.setOnClickListener {
            if (!isPlaying) {
                mediaPlayer.start()
                binding.play.setImageResource(R.drawable.baseline_stop_24)
                isPlaying = true
            } else if (isPlaying) {
                mediaPlayer.pause()
                binding.play.setImageResource(R.drawable.baseline_play_arrow_24)
                isPlaying = false
            }
        }

        binding.EBook.setOnClickListener {
            binding.EBook.setBackgroundResource(R.drawable.back_choose_dark_blue)
            binding.AudioBook.setBackgroundColor(Color.TRANSPARENT)
            val bundle = bundleOf("book" to item!!.id)
            findNavController().navigate(R.id.action_playerFragment_to_moreFragment, bundle)

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
         * @return A new instance of fragment PlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}