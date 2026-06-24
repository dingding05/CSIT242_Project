package com.example.csit242project

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class DatabaseHelper {
    //database reference
    private val database = FirebaseDatabase.getInstance("https://expense-tracker-e9e8d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("expenses")
    private val auth = FirebaseAuth.getInstance()

    // Saves an expense entry
    fun addExpense(
        amount: Double,
        category: String,
        type: String = "Expense",
        description: String = "",
        customDate: String = "",
        onComplete: (Boolean) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onComplete(false)
            return
        }

        val userId = currentUser.uid
        val uniqueEntryId = database.child(userId).push().key ?: return

        // Create the entry object
        val newExpense = ExpenseEntry(
            entryId = uniqueEntryId,
            amount = amount,
            category = category,
            type = type,
            description = description,
            customDate = customDate,
            timestamp = System.currentTimeMillis()
        )

        // Save it under the specific user's ID
        database.child(userId).child(uniqueEntryId).setValue(newExpense)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Fetches data and calculates balances for the Dashboard UI
    fun getDashboardData(onResult: (currentBalance: Double, lastMonthBalance: Double, recentTransactions: List<ExpenseEntry>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onResult(0.0, 0.0, emptyList())
            return
        }

        database.child(currentUser.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = mutableListOf<ExpenseEntry>()
                var currentBalance = 0.0
                var lastMonthBalance = 0.0

                // Set up calendar to find last month's time range
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                val lastMonth = calendar.get(Calendar.MONTH)
                val lastMonthYear = calendar.get(Calendar.YEAR)

                for (expenseSnapshot in snapshot.children) {
                    val entry = expenseSnapshot.getValue(ExpenseEntry::class.java)
                    if (entry != null) {
                        transactions.add(entry)

                        // Calculate Current Balance
                        if (entry.type.equals("Income", ignoreCase = true)) {
                            currentBalance += entry.amount
                        } else {
                            currentBalance -= entry.amount // Subtracts if it is an Expense
                        }

                        // Calculate Last Month's Closing Balance
                        val entryCalendar = Calendar.getInstance()
                        entryCalendar.timeInMillis = entry.timestamp
                        val entryMonth = entryCalendar.get(Calendar.MONTH)
                        val entryYear = entryCalendar.get(Calendar.YEAR)

                        if (entryMonth == lastMonth && entryYear == lastMonthYear) {
                            if (entry.type.equals("Income", ignoreCase = true)) {
                                lastMonthBalance += entry.amount
                            } else {
                                lastMonthBalance -= entry.amount
                            }
                        }
                    }
                }

                // Sort by newest first
                transactions.sortByDescending { it.timestamp }

                // Get only the top 3 most recent
                val recentThree = transactions.take(3)

                // Send all calculations back to the UI
                onResult(currentBalance, lastMonthBalance, recentThree)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(0.0, 0.0, emptyList())
            }
        })
    }

    // Retrieves the full transaction history for filtering and sorting
    fun getTransactions(onResult: (List<ExpenseEntry>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onResult(emptyList())
            return
        }

        database.child(currentUser.uid).get().addOnSuccessListener { snapshot ->
            val transactions = mutableListOf<ExpenseEntry>()
            for (expenseSnapshot in snapshot.children) {
                val entry = expenseSnapshot.getValue(ExpenseEntry::class.java)
                if (entry != null) {
                    transactions.add(entry)
                }
            }
            // Send back the full list sorted by date
            transactions.sortByDescending { it.timestamp }
            onResult(transactions)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }
}