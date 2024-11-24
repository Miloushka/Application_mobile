// Cet adaptateur RecyclerView est utilisé pour afficher les éléments de type `DisplayableItem`
// dans différentes vues selon qu'il s'agit d'une vue home, mensuelle ou annuelle. Il gère l'affichage
// des dépenses avec des icônes, des titres, des prix et des descriptions, et personnalise
// l'apparence des cartes selon la catégorie.
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
    private val isAnnualView: Boolean,
    private val isMonthFragment: Boolean
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.expense_card)
        val title: TextView = view.findViewById(R.id.expense_title)
        val price: TextView = view.findViewById(R.id.expense_price)
        val icon: ImageView = view.findViewById(R.id.category_icon)
        val description: TextView? = view.findViewById(R.id.expense_description)
        val detailPrice: TextView? = view.findViewById(R.id.expense_detail_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val layoutResId = if (isMonthFragment) {
            R.layout.item_expense_month
        } else {
            R.layout.item_expense
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = items[position]
        val (color, icon) = CategoryUtils.getCategoryAttributes(item.getTitle())

        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
        holder.icon.setImageResource(icon)

        if (isMonthFragment) {
            if (item is Expense) {
                holder.title.text = item.getTitle()
                holder.price.text = item.getSubtitle()
                holder.description?.text = item.getDetails()
                holder.detailPrice?.text = "${item.detailPrices}"
            }
        } else {
            if (isAnnualView) {
                holder.title.text = item.getTitle()
                holder.price.text = item.getSubtitle()
            } else {
                if (item is Expense) {
                    holder.title.text = item.getDetails()
                    holder.price.text = item.getSubtitle()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
