package com.example.accountsmanager.ui.db.entity.entity

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [AccountsItem::class],
    version = 1
)
abstract class AccountsDatabase : RoomDatabase(){

    abstract fun getAccountsDao() : AccountsDao

    companion object{
        @Volatile
        private var instance : AccountsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,
                AccountsDatabase::class.java,"Accountsdb.db").build()

    }

}