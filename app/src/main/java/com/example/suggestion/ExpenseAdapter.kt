package com.example.suggestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(
    private val items: List<DisplayableItem>,
    private val isAnnualView: Boolean,
    private val isMonthFragment: Boolean
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var onExpenseClickListener: ((ExpenseApp) -> Unit)? = null

    fun setOnExpenseClickListener(listener: (ExpenseApp) -> Unit) {
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
        val (color, icon) = CategoryUtils.getCategoryAttributes(item.getTitle())

        holder.cardView.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
        holder.icon.setImageResource(icon)

        if (isMonthFragment) {
            if (item is ExpenseApp) {
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
                if (item is ExpenseApp) {
                    holder.title.text = item.getDetails()
                    holder.price.text = item.getSubtitle()
                    holder.date?.text = formatDate(item.getDateExpense())
                }
            }
        }

        // Ajouter le gestionnaire de clic sur chaque carte de dépense
        holder.cardView.setOnClickListener {
            if (item is ExpenseApp) {
                // Si un gestionnaire de clic est défini, invoquez-le avec l'élément de dépense
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
