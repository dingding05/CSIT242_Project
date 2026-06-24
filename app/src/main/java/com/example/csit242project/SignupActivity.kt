package com.example.csit242project

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : AppCompatActivity() {

    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        authHelper = AuthHelper()

        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnSignup = findViewById<MaterialButton>(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSignup.isEnabled = false
            authHelper.registerUser(email, password, fullName) { success, message ->
                btnSignup.isEnabled = true
                if (success) {
                    Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show()
                    finish() // Go back to Login
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            finish() // Go back to Login
        }
    }
}
