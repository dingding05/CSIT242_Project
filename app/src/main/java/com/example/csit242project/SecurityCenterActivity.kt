package com.example.csit242project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SecurityCenterActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_center)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val etNewPassword = findViewById<TextInputEditText>(R.id.etNewPassword)
        val btnUpdatePassword = findViewById<MaterialButton>(R.id.btnUpdatePassword)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        btnUpdatePassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()
            if (newPassword.isEmpty() || newPassword.length < 6) {
                Toast.makeText(this, "Please enter a valid password (min 6 chars)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.currentUser?.updatePassword(newPassword)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        etNewPassword.text?.clear()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
