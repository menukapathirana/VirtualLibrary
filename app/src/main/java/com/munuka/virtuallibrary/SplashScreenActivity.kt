package com.munuka.virtuallibrary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    private var mDelayHandler:Handler?=null
    private var SplashDelay:Long=2000
    private var uri: Uri? = null
    private var vv: VideoView? = null

    internal var mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        vv = findViewById(R.id.videoView) as VideoView
        val uriPath = "https://media.giphy.com/media/U8MeoaBrKiPZkZgNOb/giphy.mp4" //update package name
        uri = Uri.parse(uriPath)

        vv!!.setVideoURI(uri)
        vv!!.start()



        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable,SplashDelay)
    }

    public override fun onDestroy() {
        if(mDelayHandler!=null)
            mDelayHandler!!.removeCallbacks(mRunnable)
        super.onDestroy()
    }


}
