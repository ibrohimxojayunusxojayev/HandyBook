package com.example.dorixona.model

import android.net.Uri
import java.io.Serializable

data class User(
    val access_token: String,
    val id: Int,
    val fullname: String,
    val username: String
) : Serializable