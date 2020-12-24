package com.example.accountsmanager.ui.db.entity.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem

@Dao
interface AccountsDao {

    @Insert
    suspend fun insert(item : AccountsItem)

    @Query("SELECT * FROM accounts_items" )
    fun getAllAccountItems() : LiveData<List<AccountsItem>>

    @Query("DELETE FROM accounts_items WHERE Id < (SELECT MAX(Id) From accounts_items) ")
    suspend fun deleteAll()


    @Query("SELECT * FROM accounts_items WHERE ID = (SELECT MAX(ID) FROM accounts_items) ")
    fun getLastRecord() : AccountsItem

}


