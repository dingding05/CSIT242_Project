package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryFragment : Fragment() {

    private val databaseHelper = DatabaseHelper()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        
        // Setup RecyclerView with the same adapter used in Dashboard
        transactionAdapter = TransactionAdapter(emptyList())
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = transactionAdapter

        databaseHelper.getTransactions { transactions ->
            if (!isAdded) return@getTransactions // fragment may have been detached
            transactionAdapter.updateData(transactions)
        }

        return view
    }
}
