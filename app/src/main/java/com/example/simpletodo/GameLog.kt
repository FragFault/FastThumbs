package com.example.simpletodo

import com.example.simpletodo.Player.Companion.KEY_POINTS
import com.parse.Parse
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser



// Points Earned : Number
// WPM(Words per minute)/speed : Number
// User who played game : User ptr
// Competitive or not : Boolean
// Accuracy : Number
// Daily Challenge or not : Boolean
// Category if applicable : String

@ParseClassName("GameLogs")
class GameResults : ParseObject(){
    fun getPoints(): Number? {
        return getNumber(KEY_POINTS)
    }
    fun setPoints(points: Number) {
        put(KEY_POINTS, points)
    }
    fun getSpeed(): Number? {
        return getNumber(KEY_SPEED)
    }
    fun setSpeed(speed: Number) {
        put(KEY_SPEED, speed)
    }
    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }
    fun setUser(user: ParseUser) {
        put (KEY_USER, user)
    }
    fun getCompetitive(): Boolean? {
        return getBoolean(KEY_COMPETITIVE)
    }
    fun setCompetitive(competitive: Boolean) {
        put(KEY_COMPETITIVE, competitive)
    }
    fun getAccuracy(): Number? {
        return getNumber(KEY_ACCURACY)
    }
    fun setAccuracy(accuracy: Number) {
        put(KEY_ACCURACY, accuracy)
    }
    fun getDaily(): Boolean? {
        return getBoolean(KEY_DAILY)
    }
    fun setDaily(daily: Boolean) {
        put(KEY_DAILY, daily)
    }
    fun getCategory(): String? {
        return getString(KEY_CATEGORY)
    }
    fun setCategory(category: String) {
        put(KEY_CATEGORY, category)
    }

    companion object {
        const val KEY_POINTS = "points"
        const val KEY_SPEED = "speed"
        const val KEY_USER = "user"
        const val KEY_COMPETITIVE = "competitive"
        const val KEY_ACCURACY = "accuracy"
        const val KEY_DAILY = "daily"
        const val KEY_CATEGORY = "category"
    }
}