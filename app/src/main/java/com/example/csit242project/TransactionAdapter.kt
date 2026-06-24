package com.example.csit242project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private var transactions: List<ExpenseEntry>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTransactionTitle)
        val tvSubtitle: TextView = view.findViewById(R.id.tvTransactionSubtitle)
        val tvAmount: TextView = view.findViewById(R.id.tvTransactionAmount)
        val ivIcon: ImageView = view.findViewById(R.id.ivTransactionIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tvTitle.text = transaction.category
        holder.tvSubtitle.text = transaction.description.ifBlank { transaction.type }
        
        // Dynamic Icon based on category
        val iconRes = when (transaction.category.lowercase()) {
            "food" -> android.R.drawable.ic_menu_today
            "transport" -> android.R.drawable.ic_menu_directions
            "shopping" -> android.R.drawable.ic_menu_manage
            "salary" -> android.R.drawable.ic_menu_save
            else -> android.R.drawable.ic_menu_edit
        }
        holder.ivIcon.setImageResource(iconRes)

        val context = holder.itemView.context
        if (transaction.type.equals("Income", ignoreCase = true)) {
            holder.tvAmount.text = context.getString(R.string.income_format, transaction.amount)
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.green_positive))
        } else {
            holder.tvAmount.text = context.getString(R.string.expense_format, transaction.amount)
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.red_negative))
        }
    }

    override fun getItemCount() = transactions.size

    fun updateData(newTransactions: List<ExpenseEntry>) {
        this.transactions = newTransactions
        notifyDataSetChanged()
    }
}
