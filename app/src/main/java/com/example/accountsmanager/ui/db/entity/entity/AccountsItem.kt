package com.example.accountsmanager.ui.db.entity.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts_items")
data class AccountsItem(
    @ColumnInfo(name = "account_transaction_type")
    var type : String,
    @ColumnInfo(name = "account_target_branch")
    var branch : String,
    @ColumnInfo(name = "amount1")
    var amount1 : String,
    @ColumnInfo(name = "amount2")
    var amount2: String,
    @ColumnInfo(name = "opening_balance")
    var openingBalance : String,
    @ColumnInfo(name = "closing_balance")
    var closingBalance : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}