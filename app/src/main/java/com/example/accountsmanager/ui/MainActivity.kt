package com.example.accountsmanager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.accountsmanager.R
import com.example.accountsmanager.databinding.ActivityMainBinding
import com.example.accountsmanager.ui.network.NetworkConnectionStatus
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val networkConnectionStatus = NetworkConnectionStatus(applicationContext)
        networkConnectionStatus.observe(this, Observer { isConnected ->

            if (isConnected){

                binding.llunplugged.visibility = View.GONE
                binding.llayoutLogin.visibility = View.VISIBLE


            }else{
                binding.llunplugged.visibility = View.VISIBLE
                binding.llayoutLogin.visibility = View.GONE
            }
        })


        val userId = AnimationUtils.loadAnimation(this, R.anim.user_id)

        val textInputUserId = findViewById<TextInputLayout>(R.id.textInputUserId)
        val textInputPassword = findViewById<TextInputLayout>(R.id.textInputPassword)

        textInputUserId.startAnimation(userId)
        textInputPassword.startAnimation(userId)



        auth = FirebaseAuth.getInstance()
        CheckLogState()
        binding.btnLogin.setOnClickListener {
            loginUser()
        }



    }
    private fun loginUser(){
        val email = binding.userid.text.toString()
        val password = binding.password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,password).await()
                    val intent =  Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
        private fun CheckLogState(){
        if (auth.currentUser != null){
            val intent =  Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}