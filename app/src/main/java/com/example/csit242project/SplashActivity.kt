package com.example.csit242project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
            val destination = if (isLoggedIn) MainActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, destination))
            finish()
        }, 2000) // 2 seconds delay
    }
}
