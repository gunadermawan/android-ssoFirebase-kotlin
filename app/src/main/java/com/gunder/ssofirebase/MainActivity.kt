package com.gunder.ssofirebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.gunder.ssofirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.hide()
        requestLogin()
    }

    private fun requestLogin() {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Handler().postDelayed({
            if (user != null) {
                val profileIntent = Intent(this, ProfileActivity::class.java)
                startActivity(profileIntent)
            } else {
                val signIntent = Intent(this, SignActivity::class.java)
                startActivity(signIntent)
            }
        }, 2000)
    }
}