package com.example.accountsmanager.ui

import com.example.accountsmanager.ui.db.entity.entity.AccountsItem

interface AddBottomDialogueListener {

    fun onAddButtonClicked(item: AccountsItem)
}