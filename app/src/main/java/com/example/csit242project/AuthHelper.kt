package com.example.csit242project

import com.google.firebase.auth.FirebaseAuth

class AuthHelper {
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, pass: String, fullName: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                        displayName = fullName
                    }
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            onResult(true, "Registration Successful!")
                        } else {
                            onResult(false, "Failed to update profile name")
                        }
                    }
                } else {
                    onResult(false, task.exception?.message ?: "Registration Failed")
                }
            }
    }

    fun loginUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Login Successful!")
                } else {
                    onResult(false, task.exception?.message ?: "Login Failed")
                }
            }
    }
}