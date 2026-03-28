package com.cit.policios.campusbites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val userInfoText = findViewById<TextView>(R.id.userInfoText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val name = intent.getStringExtra("user_name") ?: "CampusBites user"
        val email = intent.getStringExtra("user_email") ?: ""

        welcomeText.text = "Welcome, $name"
        userInfoText.text = "Logged in as: $email"

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
