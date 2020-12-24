package com.example.accountsmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountsmanager.ui.AddAccountItemBottomSheet
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem
import com.example.accountsmanager.ui.db.entity.entity.repository.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountsViewModel (
        private val repository: AccountsRepository
        ) : ViewModel(){

    fun insert(item: AccountsItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(item)
    }

    fun getAllAccountItems() = repository.getAllAccountItems()


    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun getLastRecord() = CoroutineScope(Dispatchers.Main).launch {
        repository.getLastRecord()
    }


}