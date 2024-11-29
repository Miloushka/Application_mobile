package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Calendar

class DatePickerFragment : Fragment() {

    private lateinit var yearSpinner: Spinner
    private lateinit var monthSpinner: Spinner
    private lateinit var selectedDateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_picker, container, false)

        yearSpinner = view.findViewById(R.id.spinner_year)
        monthSpinner = view.findViewById(R.id.spinner_month)
        selectedDateTextView = view.findViewById(R.id.selected_date)

        setupSpinners()

        return view
    }

    private fun setupSpinners() {
        // Configuration du spinner pour l'année
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (1900..currentYear).toList().reversed()
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        // Configuration du spinner pour le mois
        val months = listOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre")
        monthSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)

        // Gestion des changements de sélection
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelectedDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelectedDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateSelectedDate() {
        val selectedYear = yearSpinner.selectedItem as Int
        val selectedMonth = monthSpinner.selectedItemPosition + 1 // car position commence à 0

        // Formattage selon le fragment : mois + année ou juste l'année
        selectedDateTextView.text = "Date sélectionnée : $selectedMonth/$selectedYear"
    }

    companion object {
        fun newInstance(withMonth: Boolean): DatePickerFragment {
            val fragment = DatePickerFragment()
            val args = Bundle()
            args.putBoolean("withMonth", withMonth)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val withMonth = arguments?.getBoolean("withMonth") ?: true
        monthSpinner.visibility = if (withMonth) View.VISIBLE else View.GONE
    }
}
