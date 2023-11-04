package com.example.dorixona.ui

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.dorixona.databinding.FragmentPdfViewerBinding
import com.example.dorixona.model.Book
import com.example.dorixona.networking.APIClient
import com.example.dorixona.networking.APIService
import com.github.barteksc.pdfviewer.PDFView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class PdfViewerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"
    lateinit var pdfView: PDFView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
    ): View? {
        val binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        val api = APIClient.getInstance().create(APIService::class.java)
        val id = arguments?.getInt("book")
        Log.d("TAG", "onCreateView: $id")
        pdfView = binding.idPDFView
        api.getBook(id!!).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful && response.body() != null) {
                    pdfUrl = response.body()!!.file
                    RetrievePDFFromURL(pdfView).execute(pdfUrl)
                }
                else{
                    pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"
                    RetrievePDFFromURL(pdfView).execute(pdfUrl)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "DNX" + " $t")
            }
        })



        return binding.root
    }

    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        var mypdfView: PDFView = pdfView

        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.
            var inputStream: InputStream? = null
            try {
                val url = URL(params.get(0))


                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection


                if (urlConnection.responseCode == 200) {

                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: Exception) {

                e.printStackTrace()
                return null;
            }

            return inputStream;
        }

        override fun onPostExecute(result: InputStream?) {

            mypdfView.fromStream(result).load()

        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PdfViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}