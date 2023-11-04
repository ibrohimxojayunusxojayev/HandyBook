package com.example.dorixona.networking

import com.example.dorixona.model.AddComment
import com.example.dorixona.model.Book
import com.example.dorixona.model.Category
import com.example.dorixona.model.Comment
import com.example.dorixona.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.itschool.handybook.model.SignIn
import uz.itschool.handybook.model.SignUp

interface APIService {
    @GET("/book-api")
    fun getAllBooks():Call<List<Book>>

    @GET("/book-api/view")
    fun getBook(@Query("id") id: Int): Call<Book>

    @GET("/book-api/all-category")
    fun getAllCategories(): Call<List<Category>>

    @GET("/book-api/category")
    fun getProductByCategory(@Query("name") name: String): Call<List<Book>>

    @GET("/book-api/main-book")
    fun getMainBook():Call<Book>

    @GET("/book-api/comment")
    fun getComments(@Query("id") id: Int):Call<List<Comment>>

    @POST("/comment-api/create")
    fun addComment(@Body addComment: AddComment): Call<AddComment>

    @GET("/book-api/category")
    fun getBooksByCategory(@Query("name") categoryName: String):Call<List<Book>>
    @GET("/book-api/search-name")
    fun search(@Query("name") name: String):Call<List<Book>>

    @POST("/book-api/register")
    fun signup(@Body signUp: SignUp): Call<User>

    @POST("/book-api/login")
    fun login(@Body signIn: SignIn): Call<User>
}