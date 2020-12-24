package com.example.accountsmanager.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.accountsmanager.R
import com.example.accountsmanager.databinding.BottomDialogueAccountItemBinding
import com.example.accountsmanager.ui.db.entity.entity.AccountsDatabase
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem
import com.example.accountsmanager.ui.db.entity.entity.repository.AccountsRepository
import com.example.accountsmanager.ui.viewmodel.AccountsViewModel
import com.example.accountsmanager.ui.viewmodel.AccountsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.properties.Delegates


class AddAccountItemBottomSheet(context: Context, var addBottomDialogListener: AddBottomDialogueListener) : BottomSheetDialogFragment() {


    lateinit var binding : BottomDialogueAccountItemBinding
    lateinit var trb : String
    lateinit var tt : String
    lateinit var from : String
     var openingbalvar : String = ""
     var closingingbalvar : String = ""


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialogue_account_item,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomDialogueAccountItemBinding.bind(view)


        binding.etAmount2.isEnabled = false
        binding.tvAmount2.isEnabled = false


        val database = context?.let { AccountsDatabase(it) }
        val repository = database?.let { AccountsRepository(it) }
        val factory = repository?.let { AccountsViewModelFactory(it) }


        val viewModel = factory?.let { ViewModelProvider(this, it).get(AccountsViewModel::class.java) }




        val branches = arrayOf("-","KRR","TRY","PKT","TNJ","KUM","KRL","PDO","CMD","SPT","PAR","VPN","VLR","TML","BLR","HOS","SLM","NKL","ERD","VKL","TPR","CBE",
        "POL","DGL","MDU","SVK","TVL")
        val arrAdapter = ArrayAdapter(this.requireActivity(),android.R.layout.simple_spinner_dropdown_item,branches)
        binding.spTargetBranch.adapter = arrAdapter
        binding.spTargetBranch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterview: AdapterView<*>?, view: View?, position: Int, id: Long) {
                trb =  adapterview?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val frombranches = arrayOf("-","KRR","TRY","PKT","TNJ","KUM","KRL","PDO","CMD","SPT","PAR","VPN","VLR","TML","BLR","HOS","SLM","NKL","ERD","VKL","TPR","CBE",
                "POL","DGL","MDU","SVK","TVL")
        val arrAdapterfrom = ArrayAdapter(this.requireActivity(),android.R.layout.simple_spinner_dropdown_item,frombranches)
        binding.spFrom.adapter = arrAdapterfrom
        binding.spFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterview: AdapterView<*>?, view: View?, position: Int, id: Long) {
                from =  adapterview?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val transtype = arrayOf("-","Initial Amt","MT In","MT Out","BT In","BT Out","Loan IN","Loan Out","Share In","Share Out","Bank In","Bank Out")
        val arrAdapter1 = ArrayAdapter(this.requireActivity(),android.R.layout.simple_spinner_dropdown_item,transtype)
        binding.spTransactionType.adapter = arrAdapter1
        binding.spTransactionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterview: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tt =  adapterview?.getItemAtPosition(position).toString()


                if (tt.equals("MT Out")|| tt.equals("MT In")){
                    binding.etAmount2.isEnabled = true
                    binding.etAmount2.visibility = View.VISIBLE
                    binding.tvAmount2.isEnabled = true
                    binding.tvAmount2.visibility = View.VISIBLE


                }else{
                    binding.etAmount2.isEnabled = false
                    binding.etAmount2.visibility = View.GONE
                    binding.tvAmount2.isEnabled = false
                    binding.tvAmount2.visibility = View.GONE
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        binding.btnSubmit
        .setOnClickListener {

            var ttType = tt
            var tBranch = trb
            var fBranch = from
            var amount1 = binding.etamount1.text.toString()
            var amount2 = binding.etAmount2.text.toString()




            if (tt.equals("Initial Amt")) {
                                if (fBranch.isEmpty() || amount1.isEmpty() || fBranch.equals("-")) {
                                    Toast.makeText(context, "Please fill all Details", Toast.LENGTH_SHORT).show()
                                    return@setOnClickListener
                                }else if (amount1.contains(".")){
                                openingbalvar = amount1
                                closingingbalvar = amount1

                                val item = AccountsItem(ttType, tBranch, amount1, amount2, openingbalvar, closingingbalvar)
                                addBottomDialogListener.onAddButtonClicked(item)
                                senddata()
                                dismiss()
                                    }
                                else{
                                    Toast.makeText(context, "Enter in Decimal", Toast.LENGTH_SHORT).show()
                                    return@setOnClickListener

                                }

            } else {


                if (tt.equals("-") || from.equals("-")) {

                    Toast.makeText(context, "Enter Valid Type or From", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else {

                    if (ttType.isEmpty() || tBranch.isEmpty() || amount1.isEmpty()) {
                        Toast.makeText(context, "Please fill all Details", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else if (amount1.contains(".")) {

                        if (tt.equals("MT Out") || tt.equals("MT In")) {
                            if (amount2.contains(".")) {


                                if (tt.equals("MT In")){
                                    viewModel?.getAllAccountItems()?.observe(viewLifecycleOwner , Observer {



                                        closingingbalvar=  it.last().closingBalance
                                        closingingbalvar = roundOffDecimal(amount1.toDouble() + amount2.toDouble() + closingingbalvar.toDouble())
                                        openingbalvar = it.first().openingBalance
                                        val item = AccountsItem(ttType, tBranch, amount1, amount2, openingbalvar ,closingingbalvar)
                                        addBottomDialogListener.onAddButtonClicked(item)
                                        senddata()
                                        dismiss()

                                    })

                                }else{

                                    viewModel?.getAllAccountItems()?.observe(viewLifecycleOwner , Observer {



                                        closingingbalvar=  it.last().closingBalance
                                        closingingbalvar = roundOffDecimal(closingingbalvar.toDouble() - (amount1.toDouble() + amount2.toDouble()) )
                                        Log.d("Query","${amount1.toDouble()}")
                                        Log.d("Query","${amount2.toDouble()}")
                                        openingbalvar = it.first().openingBalance
                                        val item = AccountsItem(ttType, tBranch, amount1, amount2, openingbalvar ,closingingbalvar)
                                        addBottomDialogListener.onAddButtonClicked(item)
                                        senddata()
                                        dismiss()

                                    })

                                }




                            } else {
                                Toast.makeText(context, "Enter Amount 2 in Decimal", Toast.LENGTH_LONG).show()
                                return@setOnClickListener
                            }

                        } else {


                            if (tt.equals("BT In") || tt.equals("Loan In") || tt.equals("Share In") || tt.equals("Bank In")) {
                                viewModel?.getAllAccountItems()?.observe(viewLifecycleOwner, Observer {


                                    closingingbalvar = it.last().closingBalance
                                    closingingbalvar = roundOffDecimal(closingingbalvar.toDouble() + amount1.toDouble())
                                    openingbalvar = it.first().openingBalance
                                    val item = AccountsItem(ttType, tBranch, amount1, amount2, openingbalvar, closingingbalvar)
                                    addBottomDialogListener.onAddButtonClicked(item)
                                    senddata()
                                    dismiss()

                                })


                            }

                            else{
                                viewModel?.getAllAccountItems()?.observe(viewLifecycleOwner, Observer {


                                    closingingbalvar = it.last().closingBalance
                                    closingingbalvar = roundOffDecimal(closingingbalvar.toDouble() - amount1.toDouble())
                                    openingbalvar = it.first().openingBalance
                                    val item = AccountsItem(ttType, tBranch, amount1, amount2, openingbalvar, closingingbalvar)
                                    addBottomDialogListener.onAddButtonClicked(item)
                                    senddata()
                                    dismiss()

                                })

                            }
                        }

                    } else {
                        Toast.makeText(context, "Enter Amount 1 Decimal", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }


            }

        }

        binding.cancel.setOnClickListener {
            dismiss()
        }





    }
    private fun senddata(){
        var ttType = tt
        var tBranch = trb
        var amount1 = binding.etamount1.text.toString()
        var amount2 = binding.etAmount2.text.toString()




        val ref = FirebaseDatabase.getInstance().getReference("Transaction Details")



        val id = ref.push().key

        val userdata = id.let { AccountsItem(ttType,tBranch,amount1,amount2,openingbalvar,closingingbalvar) }


            ref.child(id.toString()).setValue(userdata).addOnCompleteListener{
               // Toast.makeText(requireContext(),"",Toast.LENGTH_SHORT).show()
        }

    }
    fun roundOffDecimal(number: Double) : String {
        val rounded = String.format("%.2f", number)
        return rounded
    }
}

