package com.example.suggestion.data

// Donne les données à l'interface utilisateur et applique les changements de configurations
// le ViewModel est fait pour encapsuler la logique d'accès à la base de données
// écrit par Jean-Guilhem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                onSuccess(user)
            }} else {
                onError("Email incorrect.")
            }
        }
    }
}
