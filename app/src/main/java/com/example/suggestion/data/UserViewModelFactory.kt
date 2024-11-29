package com.example.suggestion.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel inconnue")
    }
}
