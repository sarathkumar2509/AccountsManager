package com.example.accountsmanager.ui.others

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accountsmanager.R
import com.example.accountsmanager.databinding.AccountItemBinding
import com.example.accountsmanager.ui.db.entity.entity.AccountsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountItemAdapter: RecyclerView.Adapter<AccountItemAdapter.AccountViewHolder>() {




    inner class AccountViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val TTY  = itemView.findViewById<TextView>(R.id.ttTyperesult)
        val TB   = itemView.findViewById<TextView>(R.id.ttBranchResult)
        var A1   = itemView.findViewById<TextView>(R.id.ttAmount1Result)
        var A2   = itemView.findViewById<TextView>(R.id.Amount2Result)

        val cb = itemView.findViewById<TextView>(R.id.closingBal)
        val status = itemView.findViewById<TextView>(R.id.tvstatus)



    }


    private val differCallback = object : DiffUtil.ItemCallback<AccountsItem>(){
        override fun areItemsTheSame(oldItem: AccountsItem, newItem: AccountsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AccountsItem, newItem: AccountsItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_item,parent,false)
        return AccountViewHolder(view)

    }



    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {

        val curentAccountItem = differ.currentList[position]

        holder.TTY.text = curentAccountItem.type
        holder.TB.text = curentAccountItem.branch
        holder.A1.text = curentAccountItem.amount1
        holder.A2.text = curentAccountItem.amount2

        val statustext = curentAccountItem.type

        holder.cb.text =curentAccountItem.closingBalance

        if (statustext.equals("Initial Amt")){
            holder.status.setText("Initial Amount")

        }else if(position == 0){
            holder.status.setText("Yesterday Balance")
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

