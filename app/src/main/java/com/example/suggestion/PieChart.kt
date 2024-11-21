package com.example.suggestion

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min

class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var data: List<CategoryTotal> = emptyList()

    // Setter pour les données
    fun setData(data: List<CategoryTotal>) {
        this.data = data
        invalidate() // Redessiner la vue
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        // Calculer le total des montants
        val total = data.sumOf { it.totalAmount }
        var startAngle = 0f

        // Calculer les dimensions du cercle
        val size = min(width, height).toFloat()
        val radius = size / 2f
        val centerX = width / 2f
        val centerY = height / 2f

        // Dessiner les segments du pie chart
        for ((index, category) in data.withIndex()) {
            val sweepAngle = (category.totalAmount / total * 360).toFloat()

            // Utiliser la méthode getCategoryAttributes pour obtenir la couleur de la catégorie
            val (color, _) = getCategoryAttributes(category.getTitle())

            // Définir la couleur associée à la catégorie
            paint.color = ContextCompat.getColor(context, color)

            // Dessiner un segment avec la couleur et l'angle calculé
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle,
                sweepAngle,
                true,
                paint
            )

            // Incrémenter l'angle de départ pour le prochain segment
            startAngle += sweepAngle
        }
    }

    // Utiliser la même fonction que dans ExpenseAdapter pour récupérer les attributs de catégorie
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
