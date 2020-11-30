package com.noob.crash.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.noob.crash.MainActivity
import com.noob.crash.backpressed




class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = AndroidApplicationConfiguration()
        initialize(MainActivity(), configuration)
    }



    override fun onBackPressed() {
        backpressed = true

    }
}