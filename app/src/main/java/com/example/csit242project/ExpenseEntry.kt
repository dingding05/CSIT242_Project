package com.example.csit242project

data class ExpenseEntry(
    val entryId: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val type: String = "Expense", //Tracks if it is "Income" or "Expense"
    val description: String = "", //For the Entry Page requirement
    val customDate: String = "",  //Users can manually change the date
    val timestamp: Long = 0L
)