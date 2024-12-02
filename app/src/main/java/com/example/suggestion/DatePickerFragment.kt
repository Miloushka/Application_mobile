package com.example.suggestion

import android.content.Context
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

    // Indicateur si le mois doit être inclus ou non
    private var withMonth: Boolean = true

    interface OnDateSelectedListener {
        fun onDateSelected(year: Int, month: Int?)
    }

    private var listener: OnDateSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSelectedListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_picker, container, false)

        yearSpinner = view.findViewById(R.id.spinner_year)
        monthSpinner = view.findViewById(R.id.spinner_month)
        selectedDateTextView = view.findViewById(R.id.selected_date)

        // Récupérer l'argument pour déterminer si le mois doit être inclus
        withMonth = arguments?.getBoolean("withMonth") ?: true

        setupSpinners()

        return view
    }

    private fun setupSpinners() {
        // Récupération de l'année et du mois actuels
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) // Mois courant (0-11)

        // Configuration du spinner pour l'année
        val years = (1900..currentYear).toList().reversed() // Liste des années de 1900 à l'année actuelle
        yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        // Configuration du spinner pour le mois si nécessaire
        if (withMonth) {
            val months = listOf(
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                "Août", "Septembre", "Octobre", "Novembre", "Décembre"
            )
            monthSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
            monthSpinner.visibility = View.VISIBLE

            // Sélectionner le mois et l'année actuels par défaut
            monthSpinner.setSelection(currentMonth) // Mois actuel
            yearSpinner.setSelection(years.indexOf(currentYear)) // Année actuelle
        } else {
            monthSpinner.visibility = View.GONE
            yearSpinner.setSelection(years.indexOf(currentYear)) // Sélectionner l'année actuelle
        }

        // Écouteur pour la sélection de l'année
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelectedDate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Écouteur pour la sélection du mois si nécessaire
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelectedDate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        // Méthode pour créer une instance du fragment avec ou sans mois
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
        // Récupérer l'argument pour afficher ou non le mois
        val withMonth = arguments?.getBoolean("withMonth") ?: true
        monthSpinner.visibility = if (withMonth) View.VISIBLE else View.GONE
    }

    private fun updateSelectedDate() {
        val selectedYear = yearSpinner.selectedItem as Int
        val selectedMonth = if (withMonth) monthSpinner.selectedItemPosition + 1 else null

        // Notifier le fragment parent
        listener?.onDateSelected(selectedYear, selectedMonth)

        // Mettre à jour l'affichage de la date
        selectedDateTextView.text = if (withMonth) {
            "Date sélectionnée : $selectedMonth/$selectedYear"
        } else {
            "Date sélectionnée : $selectedYear"
        }
    }
}