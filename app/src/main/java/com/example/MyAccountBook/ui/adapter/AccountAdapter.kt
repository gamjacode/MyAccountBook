package com.example.myaccountbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.MyAccountBook.R
import com.example.myaccountbook.database.AccountEntity
import com.example.myaccountbook.util.AccountString
import kotlinx.android.synthetic.main.item_money.view.*

class AccountAdapter(val accountItemClick: (AccountEntity) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var accounts: List<AccountEntity> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_money, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AccountViewHolder).bind(accounts[position])
    }

    fun setAccounts(accounts:List<AccountEntity>){
        this.accounts = accounts
        notifyDataSetChanged()
    }

    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val category  = itemView.home_item_category
        private val money = itemView.home_item_money

        fun bind(account:AccountEntity){
            category.text = account.category
            if(category.text == AccountString().INCOME){
                category.background = ContextCompat.getDrawable(itemView.context, R.drawable.income)
                category.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorIncome))
            }else{
                category.background = ContextCompat.getDrawable(itemView.context, R.drawable.pay)
                category.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPay))
            }
            money.text = account.money.toString()

            itemView.setOnClickListener { accountItemClick(account) }
        }
    }
}