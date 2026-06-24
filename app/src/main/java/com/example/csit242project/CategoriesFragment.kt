package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class CategoriesFragment : Fragment() {

    private val categories = mutableListOf("Food", "Transport", "Shopping", "Entertainment", "Health", "Salary", "Gifts")
    private lateinit var adapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_categories, container, false)

        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        adapter = CategoriesAdapter(categories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = adapter

        // Add swipe to delete functionality
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedCategory = categories[position]
                
                categories.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(rvCategories, "$removedCategory removed", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        categories.add(position, removedCategory)
                        adapter.notifyItemInserted(position)
                    }.show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(rvCategories)

        view.findViewById<MaterialButton>(R.id.btnAddCategory).setOnClickListener {
            showAddCategoryDialog()
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Category")

        val input = EditText(requireContext())
        input.hint = "Category Name"
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val categoryName = input.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                categories.add(categoryName)
                adapter.notifyItemInserted(categories.size - 1)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    class CategoriesAdapter(private val items: List<String>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = items[position]
            holder.textView.setTextColor(0xFF000000.toInt()) // Set black for visibility in standard layout
        }

        override fun getItemCount(): Int = items.size
    }
}
