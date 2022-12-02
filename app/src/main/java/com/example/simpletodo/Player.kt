package com.example.simpletodo

import android.provider.Settings.Secure.getString
import android.text.method.DateTimeKeyListener
import android.widget.TextView
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.Date

@ParseClassName("Players")
class Player : ParseObject() {
    fun getUsername(): String? {
        return getString(KEY_USER)
    }

    fun setUsername(username: String){
        put(KEY_USER, username)
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

    fun setBio(bio: String){
        put(KEY_DESCRIPTION, bio)
    }

    fun getPoints(): Int? {
        return getInt(KEY_POINTS)
    }
    fun setPoints(points: Int){
        put(KEY_POINTS, points)
    }

    fun getAcc(): Int? {
        return getInt(KEY_Acc)
    }
    fun setAcc(acc: Int){
        put(KEY_Acc, acc)
    }
    fun getSpeed(): Int? {
        return getInt(KEY_Speed)
    }
    fun setSpeed(speed: Int){
        put(KEY_Speed, speed)
    }


    companion object {
        const val KEY_DESCRIPTION = "bio"
        const val KEY_USER = "user"
        const val KEY_PROFILE = "profilePic"
        const val KEY_POINTS = "totalPoints"
        const val KEY_Acc = "averageAcc"
        const val KEY_Speed = "averageSpeed"
    }
}