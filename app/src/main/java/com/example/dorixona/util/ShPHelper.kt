package com.example.dorixona.util

import android.content.Context
import android.net.Uri
import com.example.dorixona.model.Book
import com.example.dorixona.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShPHelper private constructor(context: Context) {
    val shared = context.getSharedPreferences("data", 0)
    var edit = shared.edit()
    private val gson = Gson()
    private val type_user = object : TypeToken<User>() {}.type


    companion object {
        private var instance: ShPHelper? = null
        fun getInstance(context: Context): ShPHelper {
            if (instance == null) {
                instance = ShPHelper(context)
            }
            return instance!!
        }
    }

    fun setBook(book: Book) {
        val type = object : TypeToken<List<Book>>() {}.type

        val bookList: MutableList<Book>
        val str = shared.getString("Book", "")

        if (str == "") {
            bookList = mutableListOf()
        } else {
            bookList = gson.fromJson(str, type)
        }
        bookList.add(book)

        val edited = gson.toJson(bookList)
        edit.putString("Book", edited).apply()
    }

    fun removeBook(book: Book) {
        val type = object : TypeToken<List<Book>>() {}.type

        val bookList: MutableList<Book>
        val str = shared.getString("Book", "")

        if (str == "") {
            bookList = mutableListOf()
        } else {
            bookList = gson.fromJson(str, type)
        }
        bookList.remove(book)

        val edited = gson.toJson(bookList)
        edit.putString("Book", edited).apply()
    }

    fun getBooks(): MutableList<Book> {
        val type = object : TypeToken<List<Book>>() {}.type
        val gson = Gson()

        val bookList: MutableList<Book>
        val str = shared.getString("Book", "")

        if (str == "") {
            bookList = mutableListOf()
        } else {
            bookList = gson.fromJson(str, type)
        }
        return bookList
    }


    fun getUser(): User {
        var str = shared!!.getString("user", "")
        var user = User("",-1,"","")
        if (str != ""){
            user = gson.fromJson(str, type_user)
        }
        return user
    }

    fun setUser(user: User) {
        edit = shared!!.edit()
        edit.putString("user", gson.toJson(user)).apply()
    }


    fun setReadBook(book: Book) {
        val type = object : TypeToken<List<Book>>() {}.type

        val bookList: MutableList<Book>
        val str = shared.getString("SendBook", "")

        if (str == "") {
            bookList = mutableListOf()
        } else {
            bookList = gson.fromJson(str, type)
        }
        bookList.add(book)

        val edited = gson.toJson(bookList)
        edit.putString("SendBook", edited).apply()
    }

    fun getReadBooks(): MutableList<Book> {
        val type = object : TypeToken<List<Book>>() {}.type
        val gson = Gson()

        val bookList: MutableList<Book>
        val str = shared.getString("SendBook", "")

        if (str == "") {
            bookList = mutableListOf()
        } else {
            bookList = gson.fromJson(str, type)
        }
        return bookList
    }

}