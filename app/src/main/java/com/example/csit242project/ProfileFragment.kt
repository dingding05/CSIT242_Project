package com.example.csit242project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val currentUser = FirebaseAuth.getInstance().currentUser

        tvName.text = currentUser?.displayName ?: "User"
        tvEmail.text = currentUser?.email ?: "No Email"

        view.findViewById<ImageView>(R.id.btn_edit_name).setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Edit Name")

            val input = EditText(requireContext())
            input.setText(tvName.text)
            builder.setView(input)

            builder.setPositiveButton("Update") { dialog, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = newName
                    }

                    currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            tvName.text = newName
                            Toast.makeText(requireContext(), "Name updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }

        view.findViewById<LinearLayout>(R.id.row_security_center).setOnClickListener {
            startActivity(Intent(requireContext(), SecurityCenterActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.card_settings).setOnClickListener {
            (activity as? MainActivity)?.navigateToSettings()
        }

        return view
    }
}
