package com.example.accountsmanager.ui.db.entity.entity.repository

import android.os.AsyncTask
import com.example.accountsmanager.ui.db.entity.entity.AccountsDao
import com.example.accountsmanager.ui.db.entity.entity.AccountsDatabase
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem

class AccountsRepository(
    private val db : AccountsDatabase
) {
    suspend fun insert(item : AccountsItem) = db.getAccountsDao().insert(item)


    fun getAllAccountItems() = db.getAccountsDao().getAllAccountItems()

    suspend fun deleteAll() = db.getAccountsDao().deleteAll()


    fun getLastRecord() = db.getAccountsDao().getLastRecord()



}