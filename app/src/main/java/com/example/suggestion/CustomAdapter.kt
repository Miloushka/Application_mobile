// Cet adaptateur personnalisé gère l'affichage des éléments dans un spinner.
// Il associe chaque élément de la liste à une vue contenant une image et un texte,
// permettant de personnaliser l'apparence des options dans le menu déroulant pour ajouter une dépense.

package com.example.suggestion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(
    context: Context,
    customList: ArrayList<CategorieListItems>
) : ArrayAdapter<CategorieListItems>(context, 0, customList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    private fun customView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_categorie_spinner, parent, false)

        val item = getItem(position)

        val imageView: ImageView = view.findViewById(R.id.ivCustomSpinner)
        val textView: TextView = view.findViewById(R.id.tvCustomSpinner)

        item?.let {
            imageView.setImageResource(it.spinnerImage)
            textView.text = it.spinnerText
        }

        return view
    }
}
