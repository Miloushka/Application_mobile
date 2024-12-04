package com.example.suggestion.data

import androidx.lifecycle.LiveData

// permet à l'utilisateur d'accéder à plusieurs source de donnée
// recommandé pour les bonnes pratiques d'architectures
//écrit par Jean-Guilhem

class UserRepositoty(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
}