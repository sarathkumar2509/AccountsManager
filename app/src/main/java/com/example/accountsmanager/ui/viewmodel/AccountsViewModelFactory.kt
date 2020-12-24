package com.example.accountsmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.accountsmanager.ui.db.entity.entity.repository.AccountsRepository


@Suppress("UNCHECKED_CAST")
class AccountsViewModelFactory (
        private val repository: AccountsRepository
        ) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountsViewModel(repository) as T
    }
}