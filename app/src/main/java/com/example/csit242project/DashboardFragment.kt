package com.example.csit242project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {

    private val databaseHelper = DatabaseHelper()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        val tvCurrentBalance = view.findViewById<TextView>(R.id.tvCurrentBalance)
        val tvLastMonthBalance = view.findViewById<TextView>(R.id.tvLastMonthBalance)
        val rvRecentTransactions = view.findViewById<RecyclerView>(R.id.rvRecentTransactions)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)

        // Set dynamic date
        val sdf = java.text.SimpleDateFormat("EEEE, d MMMM, yyyy", java.util.Locale.getDefault())
        tvDate.text = sdf.format(java.util.Date())

        // Setup RecyclerView
        transactionAdapter = TransactionAdapter(emptyList())
        rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        rvRecentTransactions.adapter = transactionAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = currentUser?.displayName ?: "User"
        tvGreeting.text = getString(R.string.welcome_format, name)

        // Fetch data from Database
        databaseHelper.getDashboardData { currentBalance, lastMonthBalance, recentTransactions ->
            if (!isAdded) return@getDashboardData

            tvCurrentBalance.text = getString(R.string.currency_format, currentBalance)
            tvLastMonthBalance.text = getString(R.string.currency_format, lastMonthBalance)
            
            // Update last month color based on balance
            if (lastMonthBalance >= 0) {
                tvLastMonthBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_positive))
            } else {
                tvLastMonthBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_negative))
            }

            transactionAdapter.updateData(recentTransactions)
        }

        view.findViewById<Button>(R.id.btnAddTransaction).setOnClickListener {
            startActivity(Intent(requireContext(), DataEntryActivity::class.java))
        }

        view.findViewById<TextView>(R.id.seeAllHistory).setOnClickListener {
            (activity as? MainActivity)?.navigateToHistory()
        }

        return view
    }
}
