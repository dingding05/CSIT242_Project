package com.example.csit242project

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class DataEntryActivity : AppCompatActivity() {

    private val databaseHelper = DatabaseHelper()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.entry)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnDate = findViewById<Button>(R.id.btnDate)
        val tvoDate = findViewById<TextView>(R.id.tvoDate)

        val rbIncome = findViewById<RadioButton>(R.id.rbIncome)
        val rbExpense = findViewById<RadioButton>(R.id.rbExpense)

        val etAmount = findViewById<EditText>(R.id.etAmount)
        val spCategory = findViewById<Spinner>(R.id.spCategory)
        val etDescription = findViewById<EditText>(R.id.etDescription)

        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnDate.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val myDatePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, myYear, myMonth, myDay ->
                    selectedDate = "$myDay/${myMonth + 1}/$myYear"
                    tvoDate.text = selectedDate
                },
                year, month, day)
            myDatePicker.show()
        }

        // Category Spinner
        val categories = arrayOf("Food", "Transport", "Shopping", "Entertainment", "Health", "Salary", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter

        btnAdd.setOnClickListener {
            val amountText = etAmount.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val category = spCategory.selectedItem?.toString() ?: categories.first()

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = when {
                rbIncome.isChecked -> "Income"
                rbExpense.isChecked -> "Expense"
                else -> {
                    Toast.makeText(this, "Please select Income or Expense", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            btnAdd.isEnabled = false
            databaseHelper.addExpense(
                amount = amount,
                category = category,
                type = type,
                description = description,
                customDate = selectedDate
            ) { success ->
                btnAdd.isEnabled = true
                if (success) {
                    Toast.makeText(this, "Entry added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save entry. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
