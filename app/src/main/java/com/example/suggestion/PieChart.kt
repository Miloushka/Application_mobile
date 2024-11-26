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
import kotlin.math.min

class PieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var data: List<CategoryTotal> = emptyList()

    fun setData(data: List<CategoryTotal>) {
        this.data = data
        invalidate()
    }
    private var centerText: String = ""

    fun setCenterText(text: String) {
        this.centerText = text
        invalidate()
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK // Couleur du texte
        textSize = 48f // Taille du texte (ajustez selon vos besoins)
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

        // Dessiner le texte au centre
        val textY = centerY - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(centerText, centerX, textY, textPaint)
    }

}
