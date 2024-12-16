// PieChart : Vue personnalisée qui affiche un graphique circulaire (camembert) basé sur des données de catégories.
// Chaque "part" du graphique représente un pourcentage du total des montants des catégories.

package com.example.suggestion

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.suggestion.data.CategoryTotal
import kotlin.math.min

class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var data: List<CategoryTotal> = emptyList()
    private val centerTexts = mutableListOf<Pair<String, Int>>() // Liste des textes avec couleurs


    fun setData(data: List<CategoryTotal>) {
        // Filtrer les catégories pour exclure "revenu"
        this.data = data.filter { it.getTitle().lowercase() != "revenu" }
        invalidate()
    }

    fun setCenterTexts(texts: List<Pair<String, Int>>) {
        centerTexts.clear()
        centerTexts.addAll(texts)
        invalidate()
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 48f // Taille du texte
        textAlign = Paint.Align.CENTER // Centre le texte horizontalement
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val total = data.sumOf { it.totalAmount }
        var startAngle = 0f


        val size = min(width, height).toFloat()
        val radius = size / 2f
        val centerX = width / 2f
        val centerY = height / 2f

        for ((index, category) in data.withIndex()) {
            val (color, _) = CategoryUtils.getCategoryAttributes(category.getTitle())

            val sweepAngle = (category.totalAmount / total * 360).toFloat()
            paint.color = ContextCompat.getColor(context, color)
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
            startAngle += sweepAngle
        }
        val holeRadius = radius * 0.6f // Ajustez la taille du "trou"
        paint.color = Color.WHITE // Couleur du trou
        canvas.drawCircle(centerX, centerY, holeRadius, paint)

        // Dessiner les textes au centre
        val textSize = textPaint.textSize
        val totalTextHeight = centerTexts.size * textSize + (centerTexts.size - 1) * textSize * 0.5f
        var offsetY = centerY - totalTextHeight / 2 + textSize / 2

        for ((text, color) in centerTexts) {
            textPaint.color = color // Applique la couleur pour chaque texte
            canvas.drawText(text, centerX, offsetY, textPaint)
            offsetY += textSize * 1.5f // Espacement vertical entre les lignes
        }
    }
}
