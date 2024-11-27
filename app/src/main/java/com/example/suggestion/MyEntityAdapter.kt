package com.example.suggestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.MyEntity
import android.widget.TextView

class MyEntityAdapter : RecyclerView.Adapter<MyEntityAdapter.MyViewHolder>() {
    private var data = listOf<MyEntity>()

    // Méthode pour mettre à jour la liste des données
    fun submitList(newData: List<MyEntity>) {
        data = newData
        notifyDataSetChanged() // Mettre à jour la vue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_entity, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entity = data[position]
        holder.bind(entity)
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewItem)

        fun bind(entity: MyEntity) {
            textView.text = entity.name
        }
    }
}
