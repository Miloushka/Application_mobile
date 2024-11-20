package com.example.suggestion

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import java.util.*

object SelectDateHelper {

    fun showDatePicker(
        context: Context,
        onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formater la date en format JJ/MM/AAAA
                val formattedDate = formatSelectedDate(selectedYear, selectedMonth, selectedDay)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun formatSelectedDate(year: Int, month: Int, day: Int): String {
        return "$day/${month + 1}/$year"
    }
}
