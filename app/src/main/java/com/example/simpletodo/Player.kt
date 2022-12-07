package com.example.simpletodo

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Players")
class Player : ParseObject() {
    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser){
        put(KEY_USER, user)
    }

    fun getPImage(): ParseFile? {
        return getParseFile(KEY_IMAGE)
    }

    fun setPImage(parsefile: ParseFile) {
        put(KEY_IMAGE, parsefile)
    }


    fun getAccuracy(): Number? {
        return getNumber(KEY_ACCURACY)
    }

    fun setAccuracy(Accuracy: Number){
        put(KEY_ACCURACY, Accuracy)
    }

    fun getTotal(): Number? {
        return getNumber(KEY_TOTAL)
    }

    fun setTotal(points: Number){
        put(KEY_TOTAL, points)
    }

    fun getSpeed(): Number? {
        return getNumber(KEY_SPEED)
    }

    fun setSpeed(speed: Number){
        put(KEY_SPEED, speed)
    }

    fun getBio(): String? {
        return getString(KEY_BIO)
    }

    fun setBio(bio: String){
        put(KEY_BIO, bio)
    }

    fun getGamesPlayed(): Number? {
        return getNumber(KEY_GAMES)
    }

    fun setGamesPlayed(games: Number){
        put(KEY_GAMES, games)
    }
    fun getUsername(): String? {
        return getString(KEY_USERNAME)
    }

    fun setUsername(username: String){
        put(KEY_USERNAME, username)
    }

    companion object {
        const val KEY_USER = "user"
        const val KEY_USERNAME = "user"
        const val KEY_IMAGE = "profilePic"
        const val KEY_ACCURACY = "averageAcc"
        const val KEY_TOTAL = "totalPoints"
        const val KEY_BIO = "bio"
        const val KEY_GAMES = "gamesPlayed"
        const val KEY_SPEED = "averageSpeed"
    }
}