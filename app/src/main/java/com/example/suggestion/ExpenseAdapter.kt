package com.example.suggestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.expense_card)
        val icon: ImageView = view.findViewById(R.id.category_icon)
        val description: TextView = view.findViewById(R.id.expense_description)
        val price: TextView = view.findViewById(R.id.expense_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val (color, icon) = getCategoryAttributes(expense.category)

        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, color))
        holder.icon.setImageResource(icon)
        holder.description.text = expense.description
        holder.price.text = "${expense.price}€"
    }

    override fun getItemCount() = expenses.size
}
