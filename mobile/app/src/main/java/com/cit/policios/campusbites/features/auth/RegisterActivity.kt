package com.cit.policios.campusbites.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cit.policios.campusbites.shared.network.ApiClient
import com.cit.policios.campusbites.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = findViewById<EditText>(R.id.lastNameInput)
        val emailInput = findViewById<EditText>(R.id.registerEmailInput)
        val passwordInput = findViewById<EditText>(R.id.registerPasswordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        registerButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Please complete all registration fields.")
                return@setOnClickListener
            }

            val user = User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )

            Thread {
                val result = ApiClient.register(user)
                runOnUiThread {
                    if (result.isSuccess) {
                        showToast("Registration completed. Please log in.")
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        showToast("Registration failed: ${result.exceptionOrNull()?.message}")
                    }
                }
            }.start()
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
