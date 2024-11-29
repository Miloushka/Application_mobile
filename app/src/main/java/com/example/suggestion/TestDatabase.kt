package com.example.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestion.data.MyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestDatabase : Fragment() {

        private lateinit var database: MyDatabase // Votre base de données Room
        private lateinit var editTextName: EditText // Le champ de texte pour entrer un nom
        private lateinit var buttonAdd: Button // Le bouton pour ajouter l'élément
        private lateinit var recyclerView: RecyclerView // RecyclerView pour afficher les données
        private lateinit var adapter: MyEntityAdapter // L'adaptateur du RecyclerView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val binding = inflater.inflate(R.layout.fragment_month, container, false)

            // Initialiser la base de données Room
            database = MyDatabase.getDatabase(requireContext())

            // Récupérer les vues
            //editTextName = binding.findViewById(R.id.editTextName)
           // buttonAdd = binding.findViewById(R.id.buttonAdd)
           // recyclerView = binding.findViewById(R.id.recyclerView)

            // Initialiser le RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter = MyEntityAdapter()
            recyclerView.adapter = adapter

            // Écouter le clic sur le bouton "Ajouter"
            buttonAdd.setOnClickListener {
                val name = editTextName.text.toString().trim()

                if (name.isNotEmpty()) {
                    // Ajouter l'élément à la base de données et recharger les données
                    addDataAndRefresh(name)
                    editTextName.text.clear() // Vider le champ de saisie
                } else {
                    Toast.makeText(requireContext(), "Le champ ne peut pas être vide", Toast.LENGTH_SHORT).show()
                }
            }

            // Charger les données dès que la vue est créée
            loadData()

            return binding
        }

        private fun addDataAndRefresh(name: String) {
            // Créer un nouvel objet MyEntity
            val newEntity = MyEntity(name = name)

            // Insérer dans la base de données
            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        database.myDao().insert(newEntity) // Insertion de la donnée
                    }

                    // Une fois la donnée insérée, récupérer toutes les données et les afficher
                    loadData()

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erreur d'ajout", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun loadData() {
            // Récupérer les données depuis la base de données et mettre à jour l'adaptateur
            lifecycleScope.launch {
                try {
                    // Récupérer toutes les données depuis la base de données
                    val data = withContext(Dispatchers.IO) {
                        database.myDao().getAll() // Récupérer toutes les entités
                    }

                    // Mettre à jour l'adaptateur avec les nouvelles données
                    adapter.submitList(data)

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
