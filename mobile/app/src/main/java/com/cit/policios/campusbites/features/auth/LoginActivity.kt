package com.cit.policios.campusbites.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cit.policios.campusbites.shared.network.ApiClient
import com.cit.policios.campusbites.features.home.HomeActivity
import com.cit.policios.campusbites.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerLink = findViewById<TextView>(R.id.registerLink)
        val tabLogin = findViewById<TextView>(R.id.tabLogin)
        val tabSignUp = findViewById<TextView>(R.id.tabSignUp)
        val termsCheckbox = findViewById<CheckBox>(R.id.termsCheckbox)

        // Tab switcher: Sign Up tab navigates to RegisterActivity
        tabSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter both email and password.")
                return@setOnClickListener
            }

            if (!termsCheckbox.isChecked) {
                showToast("Please agree to the terms of use & privacy policy.")
                return@setOnClickListener
            }

            Thread {
                val result = ApiClient.login(email, password)
                runOnUiThread {
                    if (result.isSuccess) {
                        val user = result.getOrNull()
                        showToast("Login successful")
                        val intent = Intent(this, HomeActivity::class.java)
                        // In the login success block, add:
                        intent.putExtra("user_id", user?.id)
                        intent.putExtra("user_name", "${user?.firstName ?: "User"} ${user?.lastName ?: ""}".trim())
                        intent.putExtra("user_email", user?.email)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Login failed: ${result.exceptionOrNull()?.message}")
                    }
                }
            }.start()
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}