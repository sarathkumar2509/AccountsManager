package com.example.accountsmanager.ui

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountsmanager.R
import com.example.accountsmanager.databinding.ActivityHomeBinding
import com.example.accountsmanager.ui.db.entity.entity.AccountsDatabase
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem
import com.example.accountsmanager.ui.db.entity.entity.repository.AccountsRepository
import com.example.accountsmanager.ui.network.NetworkConnectionStatus
import com.example.accountsmanager.ui.others.AccountItemAdapter
import com.example.accountsmanager.ui.viewmodel.AccountsViewModel
import com.example.accountsmanager.ui.viewmodel.AccountsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(){

    private lateinit var binding : ActivityHomeBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val networkConnectionStatus = NetworkConnectionStatus(applicationContext)
        networkConnectionStatus.observe(this, Observer { isConnected ->

            if (isConnected){

                binding.llunpluggedhome.visibility = View.GONE
                binding.llhomeLayout.visibility = View.VISIBLE



            }else{
                binding.llunpluggedhome.visibility = View.VISIBLE
                binding.llhomeLayout.visibility = View.GONE

                binding.llnodata.visibility = View.GONE


            }
        })


        val database = AccountsDatabase(this)
        val repository = AccountsRepository(database)
        val factory = AccountsViewModelFactory(repository)


        auth = FirebaseAuth.getInstance()
        val viewModel = ViewModelProvider(this,factory).get(AccountsViewModel::class.java)

        val adapter = AccountItemAdapter()

        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvRecentTransactions.adapter = adapter




        viewModel.getAllAccountItems().observe(this , Observer {


            if (it.isEmpty()){
                binding.rvRecentTransactions.visibility = View.GONE
                binding.llInitialamt.visibility = View.VISIBLE
                binding.llnodata.visibility = View.GONE
            }else{
                if (it.size == 1){

                    networkConnectionStatus.observe(this, Observer { isConnected ->

                        if (isConnected){
                            binding.rvRecentTransactions.visibility = View.GONE
                            binding.llnodata.visibility = View.VISIBLE
                            binding.llInitialamt.visibility = View.GONE
                            binding.llunpluggedhome.visibility = View.GONE
                            binding.Closingbalance.text = it.last().closingBalance
                            binding.openbal.text = it.first().closingBalance

                        }else{

                            binding.llunpluggedhome.visibility = View.VISIBLE
                            binding.llnodata.visibility = View.GONE

                        }
                    })

                }else{
                    binding.rvRecentTransactions.visibility = View.VISIBLE
                    binding.llnodata.visibility = View.GONE
                    binding.Closingbalance.text = it.last().closingBalance.toString()
                    binding.openbal.text = it.first().closingBalance.toString()
                }

            }



            adapter.differ.submitList(it.toList())
            adapter.notifyDataSetChanged()

        })




        binding.fabAdd.setOnClickListener{
            //Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show()
            AddAccountItemBottomSheet(this,
                    object : AddBottomDialogueListener {
                        override fun onAddButtonClicked(item: AccountsItem) {

                            viewModel.insert(item)

                            Snackbar.make(it,"Added", Snackbar.LENGTH_SHORT).show()
                        }
                    }).show(supportFragmentManager,"BottomSheetDialog")
        }

        binding.delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Yes"){ _,_ ->
                viewModel.deleteAll()
            }
            builder.setNegativeButton("Cancel"){_,_ ->}
            builder.setTitle("Delete All")
            builder.setMessage("Are you sure you want to delete All ?")
            builder.create().show()


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
        R.id.logout -> {
            auth.signOut()
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        }
        return super.onOptionsItemSelected(item)
    }

}