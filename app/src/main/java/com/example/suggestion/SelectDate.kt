package com.example.suggestion

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import java.util.*

object SelectDateHelper {

    fun showDatePicker(
        context: Context,
        mode: DatePickerMode,
        onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatSelectedDate(selectedYear, selectedMonth, selectedDay, mode)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        // Personnaliser le DatePicker selon le mode de sélection
        customizeDatePicker(datePickerDialog, mode)

        datePickerDialog.show()
    }

    private fun formatSelectedDate(year: Int, month: Int, day: Int, mode: DatePickerMode): String {
        return when (mode) {
            DatePickerMode.YEAR -> "$year"
            DatePickerMode.MONTH -> "${month + 1}/$year"
            DatePickerMode.FULL_DATE -> "$day/${month + 1}/$year"
        }
    }

    private fun customizeDatePicker(dialog: DatePickerDialog, mode: DatePickerMode) {
        val datePicker = dialog.datePicker

        when (mode) {
            DatePickerMode.YEAR -> {
                hideMonthAndDay(datePicker, dialog.context)
            }
            DatePickerMode.MONTH -> {
                hideDay(datePicker, dialog.context)
            }
            DatePickerMode.FULL_DATE -> {
                // Aucun changement nécessaire, tous les champs sont visibles
            }
        }
    }

    private fun hideMonthAndDay(datePicker: View, context: Context) {
        try {
            // Obtenir dynamiquement les IDs des vues mois et jour
            val monthId = context.resources.getIdentifier("android:id/month", null, null)
            val dayId = context.resources.getIdentifier("android:id/day", null, null)

            datePicker.findViewById<View>(monthId)?.visibility = View.GONE
            datePicker.findViewById<View>(dayId)?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideDay(datePicker: View, context: Context) {
        try {
            // Obtenir dynamiquement l'ID de la vue jour
            val dayId = context.resources.getIdentifier("android:id/day", null, null)

            datePicker.findViewById<View>(dayId)?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

enum class DatePickerMode {
    YEAR, MONTH, FULL_DATE
}
