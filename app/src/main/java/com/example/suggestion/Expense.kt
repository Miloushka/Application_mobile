// Ce fichier définit l'interface `DisplayableItem` pour uniformiser l'affichage des éléments
// avec un titre, un sous-titre et des détails. Les classes `Expense` et `CategoryTotal` implémentent
// cette interface pour représenter respectivement une dépense individuelle et le total d'une catégorie.

package com.example.suggestion

import android.os.Parcel
import android.os.Parcelable

interface DisplayableItem {
    fun getTitle(): String
    fun getSubtitle(): String
    fun getDetails(): String
    fun getDateExpense(): String
}

data class Expense(
    val id: Int,
    var category: String,
    var price: Double,
    var description: String,
    var date: String
) : DisplayableItem, Parcelable {
    var detailPrices: String? = null
    override fun getTitle(): String = category
    override fun getSubtitle(): String = "$price€"
    override fun getDetails(): String = description
    override fun getDateExpense(): String =date


    // Implémentation de la méthode writeToParcel pour rendre l'objet Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(category)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeString(date)

    }

        // Implémentation de la méthode describeContents
        override fun describeContents(): Int = 0

        // Le CREATOR permet de recréer un objet Expense à partir d'un Parcel
        companion object CREATOR : Parcelable.Creator<Expense> {
            override fun createFromParcel(parcel: Parcel): Expense {
                return Expense(
                    id = parcel.readInt(),
                    category = parcel.readString() ?: "",
                    price = parcel.readDouble(),
                    description = parcel.readString() ?: "",
                    date = parcel.readString() ?: ""
                )
            }
            override fun newArray(size: Int): Array<Expense?> {
                return arrayOfNulls(size)
            }
        }
}

data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double,
) : DisplayableItem {
    override fun getTitle(): String = categoryName
    override fun getSubtitle(): String = "$totalAmount€"
    override fun getDetails(): String = categoryName
    override fun getDateExpense(): String = ""
}
