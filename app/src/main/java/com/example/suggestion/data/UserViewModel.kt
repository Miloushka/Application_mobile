package com.example.suggestion.data

// Donne les données à l'interface utilisateur et applique les changements de configurations
// le ViewModel est fait pour encapsuler la logique d'accès à la base de données
// écrit par Jean-Guilhem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggestion.userConnected
import com.example.suggestion.userIdConnected
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    var currentUser: User? = null

    // Permet de créer un utilisateur
    fun createUser(password: String, email: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                onError("Un utilisateur avec cet email existe déjà.")
            } else {
                val userId = userDao.addUser(User(0, email, password))
                currentUser = User(userId, email, password)
                onSuccess(currentUser!!)
            }
        }
    }

    // fonction permmettant de se connecter
    fun login(email: String, password: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                if(password == user.password){
                    currentUser = user
                    userConnected = user
                    userIdConnected = user.userId
                    onSuccess(user)
                }} else {
                onError("Email incorrect.")
            }
        }
    }

    fun changePassword(id: Long, password: String, newPassword: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserById(id)
            if (user == null) {
                println("Utilisateur non trouvé.")
                onError("Utilisateur non trouvé.")
                return@launch
            }

            println("Utilisateur trouvé: ${user.email} & ${user.password} & ${password} ")

            if (password == user.password) {
                try {
                    userDao.changePassword(user.email, newPassword)
                    userConnected = userConnected.copy(password = newPassword)
                    updateUser()
                    println("Mot de passe modifié avec succès.")
                    onSuccess(user)
                } catch (e: Exception) {
                    println("Erreur lors de la modification du mot de passe: ${e.message}")
                    onError("Erreur lors de la modification du mot de passe: ${e.message}")
                }
            } else {
                println("Mot de passe incorrect.")
                onError("Mot de passe incorrect.")
            }
        }
    }

    fun getUserById(id: Long){
        viewModelScope.launch {
            try {
                val user = userDao.getUserById(id)  // Fonction suspendue appelée dans une coroutine
                if (user != null) {
                    userConnected = user
                } else {
                    println("Utilisateur avec ID $id non trouvé.")
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération de l'utilisateur: ${e.message}")
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            if (userConnected != null) {
                userDao.updateUser(userConnected!!)
                val updatedUser = userDao.getUserById(userIdConnected)  // Cela peut retourner un User? (nullable)

                if (updatedUser != null) {
                    userConnected = updatedUser  // Affecter un User non nul
                } else {
                    println("Utilisateur non trouvé avec l'ID $userIdConnected")
                    // Vous pouvez choisir de gérer cette situation ici
                }
            } else {
                println("userConnected est null, impossible de mettre à jour l'utilisateur.")
            }
        }
    }


}