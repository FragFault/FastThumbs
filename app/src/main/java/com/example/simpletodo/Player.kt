package com.example.simpletodo

import android.provider.Settings.Secure.getString
import android.text.method.DateTimeKeyListener
import android.widget.TextView
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.Date

@ParseClassName("User")
class Player : ParseObject() {
    fun getUsername(): String? {
        return getString(KEY_USER)
    }

    fun setUsername(name: String){
        put(KEY_USER, name)
    }

    fun getPImage(): ParseFile? {
        return getParseFile(KEY_PROFILE)
    }

    fun setPImage(parsefile: ParseFile) {
        put(KEY_PROFILE, parsefile)
    }

    fun getBio(): String? {
        return getString(KEY_DESCRIPTION)
    }

    fun setBio(name: String){
        put(KEY_DESCRIPTION, name)
    }

    companion object {
        const val KEY_DESCRIPTION = "bio"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "username"
        const val KEY_PROFILE = "profilePic"
        const val KEY_POSTED = "createdAt"
    }
}