package com.example.suggestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.Expense
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(
    private val items: List<Expense>,
    private val isAnnualView: Boolean,
    private val isMonthFragment: Boolean
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var onExpenseClickListener: ((Expense) -> Unit)? = null

    fun setOnExpenseClickListener(listener: (Expense) -> Unit) {
        onExpenseClickListener = listener
    }

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.expense_card)
        val title: TextView = view.findViewById(R.id.expense_title)
        val price: TextView = view.findViewById(R.id.expense_price)
        val icon: ImageView = view.findViewById(R.id.category_icon)
        val description: TextView? = view.findViewById(R.id.expense_description)
        val detailPrice: TextView? = view.findViewById(R.id.expense_detail_price)
        val date: TextView? = view.findViewById(R.id.date)
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
        val (color, icon) = CategoryUtils.getCategoryAttributes(item.category)

        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
        holder.icon.setImageResource(icon)

        if (isMonthFragment) {
            if (item is Expense) {
                holder.title.text = item.category
                holder.price.text = (item.amount.toString() + "€")
                holder.description?.text = item.description
                holder.detailPrice?.text = "${item.date}"
            }
        } else {
            if (isAnnualView) {
                holder.title.text = item.category
                holder.price.text = (item.amount.toString() + "€")
            } else {
                if (item is Expense) {
                    holder.title.text = item.description
                    holder.price.text = (item.amount.toString() + "€")
                    holder.date?.text = formatDate(item.date)
                }
            }
        }

        // Ajouter le gestionnaire de clic sur chaque carte de dépense
        holder.cardView.setOnClickListener {
            if (item is Expense) {
                expenseCurrent = item
                // Si un gestionnaire de clic est défini, appelle la fonction associé
                // avec un objet Expense en paramètre
                onExpenseClickListener?.invoke(item)
            }
        }
    }

    // Fonction pour formater la date
    private fun formatDate(rawDate: String?): String {
        if (rawDate.isNullOrEmpty()) return ""

        return try {
            val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.FRANCE)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val parsedDate = inputFormat.parse(rawDate)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            rawDate // Retourner la date brute en cas d'erreur
        }
    }
    override fun getItemCount(): Int = items.size
}
