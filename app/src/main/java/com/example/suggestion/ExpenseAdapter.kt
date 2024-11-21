package com.example.suggestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val items: List<DisplayableItem>,
    private val isAnnualView: Boolean ) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.expense_card)
        val description: TextView = view.findViewById(R.id.expense_description)
        val price: TextView = view.findViewById(R.id.expense_price)
        val icon: ImageView = view.findViewById(R.id.category_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = items[position]
        val (color, icon) = getCategoryAttributes(item.getTitle())

        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
        holder.icon.setImageResource(icon)

        if (isAnnualView) {
            holder.description.text = item.getTitle()
            holder.price.text = item.getSubtitle()
        } else {
            if (item is Expense) {
                holder.description.text = item.getDetails()
                holder.price.text = item.getSubtitle()
            }
        }
   }

    override fun getItemCount(): Int = items.size


    private fun getCategoryAttributes(category: String): Pair<Int, Int> {
        return when (category) {
            "Depense quotidienne" -> Pair(R.color.shoppingColor, R.drawable.ic_shopping)
            "Transport" -> Pair(R.color.transportColor, R.drawable.ic_transport)
            "Loisir" -> Pair(R.color.loisirColor, R.drawable.ic_loisir)
            "Maison" -> Pair(R.color.homeColor, R.drawable.ic_home)
            else -> Pair(R.color.defaultColor, R.drawable.ic_add)
        }
    }
}