package com.example.simpletodo

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseObject.registerSubclass

class   FastThumbsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(Player::class.java)
        ParseObject.registerSubclass(GameResults::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }
}